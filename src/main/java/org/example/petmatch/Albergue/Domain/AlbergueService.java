package org.example.petmatch.Albergue.Domain;

import org.example.petmatch.Albergue.DTO.AlbergueRegisterDTO;
import org.example.petmatch.Exception.NotFoundException;
import org.example.petmatch.Albergue.Infraestructure.AlbergueRepository;
import lombok.RequiredArgsConstructor;
import org.example.petmatch.Common.ValidationException;
import org.example.petmatch.GoogleApi.GoogleMapsService;
import org.modelmapper.ModelMapper;
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

    public List<Albergue> findAll(){
        return albergueRepository.findAll();
    }


    public Albergue newAlbergue(AlbergueRegisterDTO albergueDTO) throws ValidationException {
        String pwd = albergueDTO.getPassword();
        if (!Pattern.matches(".*[A-Z].*", pwd) || !Pattern.matches(".*[0-9].*", pwd)) {
            throw new ValidationException("Password has no alphanumeric characters");
        }

        Albergue albergue = modelMapper.map(albergueDTO, Albergue.class);
        if (albergueDTO.getCapacity() < 20) {
            albergue.setRating(Rating.BAJA);
            albergue.setCapacity(albergueDTO.getCapacity());
            albergue.setAvailableSpaces(albergueDTO.getCapacity());
        }else if (albergueDTO.getCapacity() < 40) {
            albergue.setRating(Rating.MEDIA);
            albergue.setCapacity(albergueDTO.getCapacity());
            albergue.setAvailableSpaces(albergueDTO.getCapacity());
        }else {
            albergue.setRating(Rating.ALTA);
            albergue.setCapacity(albergueDTO.getCapacity());
            albergue.setAvailableSpaces(albergueDTO.getCapacity());
        }

        var found = albergueRepository.findByName(albergueDTO.getName());
        if (found.isPresent()) {
            throw new ValidationException("Este albergue ya existe");
        } else {
            albergueRepository.save(albergue);
        }
        return albergue;
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
        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(() -> new NotFoundException("No existe un albergue con ese nombre"));

        if (nMascotas < 0) {
            throw new ValidationException("No se puede dejar menos de 0 mascotas en el albergue");
        }else if (nMascotas > albergue.getAvailableSpaces()){
            throw new ValidationException("El albergue no puede albergar ese numero de mascotas");
        }
        albergue.setAvailableSpaces(albergue.getAvailableSpaces() - nMascotas);
        return albergueRepository.save(albergue);
    }

    public Albergue actualizardireccion(String newAddress, String albergue_name) throws Exception {
        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(() -> new NotFoundException("No existe un albergue con ese nombre"));
        if (!googleMapsService.isValidAddress(newAddress)) {
            throw new ValidationException("La dirección proporcionada no es válida o no existe en Google Maps.");
        }

        albergue.setAddress(newAddress);
        return albergueRepository.save(albergue);
    }

    @Transactional
    public Albergue actualizartelefono(Long telefono, String albergue_name) throws Exception{
        Albergue albergue = albergueRepository.findByName(albergue_name).orElseThrow(() -> new NotFoundException("No existe un albergue con ese nombre"));

        if (!Pattern.matches("^(\\+?51)?9\\d{8}$", telefono.toString())){
            throw new ValidationException("El telefono debe tener el formato: +519XXXXXXXX or 9XXXXXXXX");
        }
        albergue.setPhone(telefono);
        return albergueRepository.save(albergue);

    }
}
