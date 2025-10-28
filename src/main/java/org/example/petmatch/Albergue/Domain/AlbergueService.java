package org.example.petmatch.Albergue.Domain;

import org.example.petmatch.Albergue.DTO.AlbergueRegisterDTO;
import org.example.petmatch.Albergue.DTO.Auth.AlbergueAuthLoginRequestDto;
import org.example.petmatch.Albergue.DTO.Auth.AlbergueAuthResponseDto;
import org.example.petmatch.Albergue.DTO.Auth.AlbergueAuthRegisterRequestDto;
import org.example.petmatch.Albergue.Exceptions.AlbergueAlreadyExistsException;
import org.example.petmatch.Albergue.Exceptions.AlbergueNotFoundException;
import org.example.petmatch.Albergue.Infraestructure.AlbergueRepository;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Albergue.Exceptions.ValidationException;
import org.example.petmatch.GoogleApi.GoogleMapsService;
import org.example.petmatch.Security.JwtService;
import org.example.petmatch.User.Exceptions.InvalidCredentialsException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AlbergueService {

    private final GoogleMapsService googleMapsService;
    private final AlbergueRepository albergueRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public List<Albergue> findAll(){
        return albergueRepository.findAll();
    }


    public void deleteAlberguebyName(String name) throws ValidationException {
        if (!StringUtils.hasText(name)) {
            throw new ValidationException("Name is required");
        }
        var found = albergueRepository.findByName(name);
        if (found == null) {
            throw new ValidationException("No existe un albergue con ese nombre");
        } else {
            albergueRepository.deleteByName(name);
        }
    }
    @Transactional
    public Albergue actualizarAvailableSeats(Integer nMascotas, String albergue_name) throws Exception{
        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(() -> new AlbergueNotFoundException("No existe un albergue con ese nombre"));

        if (nMascotas < 0) {
            throw new ValidationException("No se puede dejar menos de 0 mascotas en el albergue");
        }else if (nMascotas > albergue.getAvailableSpaces()){
            throw new ValidationException("El albergue no puede albergar ese numero de mascotas");
        }
        albergue.setAvailableSpaces(albergue.getAvailableSpaces() - nMascotas);
        return albergueRepository.save(albergue);
    }

    public Albergue actualizardireccion(String newAddress, String albergue_name) throws Exception {
        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(() -> new AlbergueNotFoundException("No existe un albergue con ese nombre"));
        if (!googleMapsService.isValidAddress(newAddress)) {
            throw new ValidationException("La dirección proporcionada no es válida o no existe en Google Maps.");
        }

        albergue.setAddress(newAddress);
        return albergueRepository.save(albergue);
    }

    @Transactional
    public Albergue actualizartelefono(Long telefono, String albergue_name) throws Exception{
        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(() -> new AlbergueNotFoundException("No existe un albergue con ese nombre"));

        if (!Pattern.matches("^(\\+?51)?9\\d{8}$", telefono.toString())){
            throw new ValidationException("El telefono debe tener el formato: +519XXXXXXXX or 9XXXXXXXX");
        }
        albergue.setPhone(telefono);
        return albergueRepository.save(albergue);

    }


    @Transactional
    public AlbergueAuthResponseDto registerAlbergue(AlbergueAuthRegisterRequestDto request) {

        if (albergueRepository.existsByEmail(request.getEmail())) {
            throw new AlbergueAlreadyExistsException("El albergue con email " + request.getEmail() + " ya existe");
        }

        Albergue albergue = modelMapper.map(request, Albergue.class);
        albergue.setPassword(passwordEncoder.encode(request.getPassword()));
        albergue.setAvailableSpaces(request.getCapacity());

        if (request.getCapacity() < 20) {
            albergue.setRating(Rating.BAJA);
        } else if (request.getCapacity() < 40) {
            albergue.setRating(Rating.MEDIA);
        } else {
            albergue.setRating(Rating.ALTA);
        }

        albergueRepository.save(albergue);

        String token = jwtService.generateToken(albergue.getEmail(), "ALBERGUE");

        AlbergueAuthResponseDto responseDto = modelMapper.map(albergue, AlbergueAuthResponseDto.class);
        responseDto.setRating(albergue.getRating().name());
        responseDto.setAccessToken(token);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public AlbergueAuthResponseDto loginAlbergue(AlbergueAuthLoginRequestDto request) {
        Albergue albergue = albergueRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), albergue.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(albergue.getEmail(), "ALBERGUE");

        AlbergueAuthResponseDto response = modelMapper.map(albergue, AlbergueAuthResponseDto.class);
        response.setRating(albergue.getRating() != null ? albergue.getRating().name() : null);
        response.setAccessToken(token);

        return response;
    }

}
