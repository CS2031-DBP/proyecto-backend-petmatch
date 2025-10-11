package Albergue.Domain;

import Albergue.DTO.AlberguePresentationDTO;
import Albergue.DTO.AlbergueRegisterDTO;
import Albergue.Infraestructure.AlbergueRepository;
import Common.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AlbergueService {

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
        if (StringUtils.hasText(albergueDTO.getName())) {
            albergue.setName(albergueDTO.getName());
        } else if (StringUtils.hasText(albergueDTO.getEmail())) {
            albergue.setEmail(albergueDTO.getEmail());
        }
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
}
