package org.example.petmatch.Albergue.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Albergue.DTO.AlberguePresentationDTO;
import org.example.petmatch.Albergue.DTO.Auth.AlbergueAuthResponseDto;
import org.example.petmatch.Albergue.DTO.Auth.AlbergueAuthLoginRequestDto;
import org.example.petmatch.Albergue.DTO.Auth.AlbergueAuthRegisterRequestDto;
import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Albergue.Domain.AlbergueService;
import org.example.petmatch.Albergue.Exceptions.ValidationException;
import org.example.petmatch.GoogleApi.GoogleMapsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albergues")
@RequiredArgsConstructor
public class AlbergueController {

    private final AlbergueService albergueService;

    private final GoogleMapsService googleMapsService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<AlberguePresentationDTO>> getAllAlbergues() {
        List<Albergue> Albergues = albergueService.findAll();
        List<AlberguePresentationDTO> alberguedtos = Albergues.stream()
                .map(albergue -> modelMapper.map(albergue, AlberguePresentationDTO.class))
                .toList();
        return ResponseEntity.ok(alberguedtos);
    }

    @GetMapping("/near")
    public ResponseEntity<List<AlberguePresentationDTO>> getAlberguesNear(
            @RequestParam String location,
            @RequestParam(defaultValue = "5") double radiusKm) throws Exception {

        List<Albergue> all = albergueService.findAll();
        var origin = googleMapsService.getCoordinates(location);

        List<AlberguePresentationDTO> nearby = all.stream()
                .filter(a -> {
                    try {
                        if (a.getAddress() == null) return false;
                        var coords = googleMapsService.getCoordinates(a.getAddress());
                        return googleMapsService.distanceKm(origin, coords) <= radiusKm;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(a -> modelMapper.map(a, AlberguePresentationDTO.class))
                .toList();

        return ResponseEntity.ok(nearby);
    }

    /*
    @PostMapping("/nuevo")
    @PreAuthorize("permitAll()")
    public ResponseEntity<NewIdDTO> createAlbergue(@Valid @RequestBody AlbergueRegisterDTO albergue) throws Exception {
        Albergue albergueEntity = albergueService.newAlbergue(albergue);
        return ResponseEntity.status(HttpStatus.CREATED).body(new NewIdDTO(albergueEntity.getId()));
    }
     */

    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<Albergue> deleteAlbergue(@PathVariable String name) throws ValidationException {
        albergueService.deleteAlberguebyName(name);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/mascotas_registradas/{n_mascotas}/{albergue_name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<AlberguePresentationDTO> actualizarAvailableSeats(@PathVariable Integer n_mascotas, @PathVariable String albergue_name) throws Exception{
        Albergue albergue_actualizado = albergueService.actualizarAvailableSeats(n_mascotas, albergue_name);
        return ResponseEntity.ok(modelMapper.map(albergue_actualizado, AlberguePresentationDTO.class));
    }

    @PatchMapping("/actualizacion_address/{new_adrress}/{albergue_name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<AlberguePresentationDTO> actualizaraddress(@PathVariable String new_adrress, @PathVariable String albergue_name) throws Exception{
        Albergue albergue_actualizado = albergueService.actualizardireccion(new_adrress, albergue_name);
        return ResponseEntity.ok(modelMapper.map(albergue_actualizado, AlberguePresentationDTO.class));
    }

    @PatchMapping("/actualizacion_telefono/{telefono}/{albergue_name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<AlberguePresentationDTO> actualizartelefono(@PathVariable Long telefono, @PathVariable String albergue_name) throws Exception{
        Albergue albergue_actualizado = albergueService.actualizartelefono(telefono, albergue_name);
        return ResponseEntity.ok(modelMapper.map(albergue_actualizado, AlberguePresentationDTO.class));
    }

    @PostMapping("/auth/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AlbergueAuthResponseDto> register(
            @Valid @RequestBody AlbergueAuthRegisterRequestDto request) {
        AlbergueAuthResponseDto responseDto = albergueService.registerAlbergue(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AlbergueAuthResponseDto> login(
            @Valid @RequestBody AlbergueAuthLoginRequestDto request) {
        AlbergueAuthResponseDto responseDto = albergueService.loginAlbergue(request);
        return ResponseEntity.ok(responseDto);
    }


}