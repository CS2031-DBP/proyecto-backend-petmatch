package Albergue.Controller;

import Albergue.DTO.AlberguePresentationDTO;
import Albergue.DTO.AlbergueRegisterDTO;
import Albergue.Domain.Albergue;
import Albergue.Domain.AlbergueService;
import Common.NewIdDTO;
import Common.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albergues")
@RequiredArgsConstructor
public class AlbergueController {

    private final AlbergueService albergueService;

    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<AlberguePresentationDTO>> getAllAlbergues() {
        List<Albergue> Albergues = albergueService.findAll();
        List<AlberguePresentationDTO> alberguedtos = Albergues.stream()
                .map(albergue -> modelMapper.map(albergue, AlberguePresentationDTO.class))
                .toList();
        return ResponseEntity.ok(alberguedtos);
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<NewIdDTO> createAlbergue(@Valid @RequestBody AlbergueRegisterDTO albergue) throws Exception {
        Albergue albergueEntity = albergueService.newAlbergue(albergue);
        return ResponseEntity.created(null).body(new NewIdDTO(albergueEntity.getId()));
    }

}
