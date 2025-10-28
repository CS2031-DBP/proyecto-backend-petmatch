package org.example.petmatch.Shelter.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Shelter.DTO.ShelterPresentationDTO;
import org.example.petmatch.Shelter.DTO.Auth.ShelterAuthResponseDto;
import org.example.petmatch.Shelter.DTO.Auth.ShelterAuthLoginRequestDto;
import org.example.petmatch.Shelter.DTO.Auth.ShelterAuthRegisterRequestDto;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Domain.ShelterService;
import org.example.petmatch.Exception.ValidationException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albergues")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;


    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<ShelterPresentationDTO>> getAllAlbergues() {

        List<ShelterPresentationDTO> alberguedtos = shelterService.findAll();
        return ResponseEntity.ok(alberguedtos);
    }

    @GetMapping("/near/{latitude}/{longitude}")
    public ResponseEntity<List<ShelterPresentationDTO>> getAlberguesNear(@PathVariable Double latitude,
                                                                         @PathVariable Double longitude) throws Exception {
        List<ShelterPresentationDTO> alberguedtos = shelterService.findSheltersNear(latitude, longitude);

        return ResponseEntity.ok(alberguedtos);
    }


    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<Shelter> deleteAlbergue(@PathVariable String name) throws ValidationException {
        shelterService.deleteAlberguebyName(name);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/mascotas_registradas/{n_mascotas}/{albergue_name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<ShelterPresentationDTO> updateAvailableSeats(@PathVariable Integer n_mascotas, @PathVariable String albergue_name) throws Exception{
        Shelter updated_shelter = shelterService.updateAvailableSeats(n_mascotas, albergue_name);
        return ResponseEntity.ok(modelMapper.map(updated_shelter, ShelterPresentationDTO.class));
    }

    @PatchMapping("/actualizacion_address/{new_adrress}/{albergue_name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<ShelterPresentationDTO> updateAddress(@PathVariable String new_adrress, @PathVariable String albergue_name) throws Exception{
        Shelter shelter_actualizado = shelterService.updateAddress(new_adrress, albergue_name);
        return ResponseEntity.ok(modelMapper.map(shelter_actualizado, ShelterPresentationDTO.class));
    }

    @PatchMapping("/actualizacion_telefono/{telefono}/{albergue_name}")
    @PreAuthorize("hasRole('ALBERGUE')")
    public ResponseEntity<ShelterPresentationDTO> updatePhone(@PathVariable Long telefono, @PathVariable String albergue_name) throws Exception{
        Shelter shelter_actualizado = shelterService.updatePhone(telefono, albergue_name);
        return ResponseEntity.ok(modelMapper.map(shelter_actualizado, ShelterPresentationDTO.class));
    }

    @PostMapping("/auth/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ShelterAuthResponseDto> register(
            @Valid @RequestBody ShelterAuthRegisterRequestDto request) {
        ShelterAuthResponseDto responseDto = shelterService.registerAlbergue(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ShelterAuthResponseDto> login(
            @Valid @RequestBody ShelterAuthLoginRequestDto request) {
        ShelterAuthResponseDto responseDto = shelterService.loginAlbergue(request);
        return ResponseEntity.ok(responseDto);
    }


}