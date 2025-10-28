package org.example.petmatch.Shelter.Domain;

import org.example.petmatch.Shelter.DTO.Auth.ShelterAuthLoginRequestDto;
import org.example.petmatch.Shelter.DTO.Auth.ShelterAuthResponseDto;
import org.example.petmatch.Shelter.DTO.Auth.ShelterAuthRegisterRequestDto;
import org.example.petmatch.Shelter.DTO.ShelterPresentationDTO;
import org.example.petmatch.Shelter.Exceptions.ShelterAlreadyExistsException;
import org.example.petmatch.Shelter.Exceptions.ShelterNotFoundException;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Exception.ValidationException;
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
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private static final double EARTH_RADIUS_KM = 6371.0088;
    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public List<ShelterPresentationDTO> findAll(){
        return shelterRepository.findAll().stream().map(albergue -> modelMapper.map(albergue, ShelterPresentationDTO.class))
                .toList();
    }


    public void deleteAlberguebyName(String name) throws ValidationException {
        if (!StringUtils.hasText(name)) {
            throw new ValidationException("Name is required");
        }
        var found = shelterRepository.findByName(name);
        if (found == null) {
            throw new ValidationException("No existe un albergue con ese nombre");
        } else {
            shelterRepository.deleteByName(name);
        }
    }
    @Transactional
    public Shelter updateAvailableSeats(Integer nMascotas, String albergue_name) throws Exception{
        Shelter shelter = shelterRepository.findByName(albergue_name).orElseThrow(() -> new ShelterNotFoundException("No existe un albergue con ese nombre"));

        if (nMascotas < 0) {
            throw new ValidationException("No se puede dejar menos de 0 mascotas en el albergue");
        }else if (nMascotas > shelter.getAvailableSpaces()){
            throw new ValidationException("El albergue no puede albergar ese numero de mascotas");
        }
        shelter.setAvailableSpaces(shelter.getAvailableSpaces() - nMascotas);
        return shelterRepository.save(shelter);
    }

    public Shelter updateAddress(String newAddress, String albergue_name) throws Exception {
        Shelter shelter = shelterRepository.findByName(albergue_name).orElseThrow(() -> new ShelterNotFoundException("No existe un albergue con ese nombre"));

        shelter.setAddress(newAddress);
        return shelterRepository.save(shelter);
    }

    @Transactional
    public Shelter updatePhone(Long telefono, String albergue_name) throws Exception{
        Shelter shelter = shelterRepository.findByName(albergue_name).orElseThrow(() -> new ShelterNotFoundException("No existe un albergue con ese nombre"));

        if (!Pattern.matches("^(\\+?51)?9\\d{8}$", telefono.toString())){
            throw new ValidationException("El telefono debe tener el formato: +519XXXXXXXX or 9XXXXXXXX");
        }
        shelter.setPhone(telefono);
        return shelterRepository.save(shelter);

    }


    @Transactional
    public ShelterAuthResponseDto registerAlbergue(ShelterAuthRegisterRequestDto request) {

        if (shelterRepository.existsByEmail(request.getEmail())) {
            throw new ShelterAlreadyExistsException("El albergue con email " + request.getEmail() + " ya existe");
        }

        Shelter shelter = modelMapper.map(request, Shelter.class);
        shelter.setPassword(passwordEncoder.encode(request.getPassword()));
        shelter.setAvailableSpaces(request.getCapacity());

        if (request.getCapacity() < 20) {
            shelter.setRating(Rating.BAJA);
        } else if (request.getCapacity() < 40) {
            shelter.setRating(Rating.MEDIA);
        } else {
            shelter.setRating(Rating.ALTA);
        }

        shelterRepository.save(shelter);

        String token = jwtService.generateToken(shelter.getEmail(), "ALBERGUE");

        ShelterAuthResponseDto responseDto = modelMapper.map(shelter, ShelterAuthResponseDto.class);
        responseDto.setRating(shelter.getRating().name());
        responseDto.setAccessToken(token);

        return responseDto;
    }

    @Transactional(readOnly = true)
    public ShelterAuthResponseDto loginAlbergue(ShelterAuthLoginRequestDto request) {
        Shelter shelter = shelterRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), shelter.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(shelter.getEmail(), "ALBERGUE");

        ShelterAuthResponseDto response = modelMapper.map(shelter, ShelterAuthResponseDto.class);
        response.setRating(shelter.getRating() != null ? shelter.getRating().name() : null);
        response.setAccessToken(token);

        return response;
    }

    public List<ShelterPresentationDTO> findSheltersNear(Double latitude, Double longitude) throws Exception {
        if (latitude == null || longitude == null) {
            throw new ValidationException("La latitud y la longitud son obligatorias");
        }
        List<ShelterPresentationDTO> nearShelters = new java.util.ArrayList<>();
        final double RADIUS_KM = 6.0;

        for (Shelter shelter : shelterRepository.findAll()){
            if (shelter.getLatitude() != null && shelter.getLongitude() != null) {
                Double lat2 = shelter.getLatitude();
                Double lon2 = shelter.getLongitude();
                if (lat2 == null || lon2 == null) continue;

                double dKm = haversineKm(latitude, longitude, lat2, lon2);
                if (dKm <= RADIUS_KM) {
                    ShelterPresentationDTO dto = modelMapper.map(shelter, ShelterPresentationDTO.class);
                    nearShelters.add(dto);
                }
            }
        }
        return nearShelters;
    }
}
