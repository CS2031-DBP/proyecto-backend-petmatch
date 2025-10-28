package org.example.petmatch.Volunteer.Domain;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Volunteer_Program.DTO.VolunteerProgramResponseDto;
import org.example.petmatch.Volunteer.Infraestructure.VolunteerRepository;
import org.example.petmatch.Volunteer.dto.VolunteerResponseDto;
import org.example.petmatch.Volunteer.exception.VolunteerNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final ModelMapper modelMapper;

    public List<VolunteerResponseDto> getAllVoluntarios(){
        List<Volunteer> volunteers = volunteerRepository.findAll();
        List<VolunteerResponseDto> voluntariosDtos = volunteers.stream()
                .map(voluntario -> modelMapper.map(voluntario, VolunteerResponseDto.class))
                .toList();
        return voluntariosDtos;
    }

    public List<VolunteerProgramResponseDto> getProgramaByVoluntarioId(Long id){
        Volunteer volunteer = volunteerRepository.findById(id).orElseThrow(() -> new VolunteerNotFoundException("Voluntario no encontrado con id: " + id));
        return volunteer.getPrograms().stream().map(programa -> modelMapper.map(programa, VolunteerProgramResponseDto.class)).toList();
    }
}
