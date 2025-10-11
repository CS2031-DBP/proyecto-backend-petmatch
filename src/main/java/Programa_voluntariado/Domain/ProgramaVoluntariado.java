package Programa_voluntariado.Domain;


import Albergue.Domain.Albergue;
import Voluntario.Domain.Voluntario;
import VoluntarioPrograma.Inscripcion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "programas_de_voluntariado")
public class ProgramaVoluntariado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private ZonedDateTime fechaInicio;

    private ZonedDateTime fechaFin;

    private String ubicacion;

    private Integer numeroVoluntariosNecesarios;

    private Integer numeroVoluntariosInscritos = 0;


    @OneToMany(mappedBy = "programaVoluntariado", orphanRemoval = true)
    private List<Inscripcion> inscritos = new ArrayList<>();

    public List<Voluntario> getVoluntarios(){
        List<Voluntario> voluntarios = new ArrayList<>();
        for(Inscripcion inscripcion : inscritos){
            voluntarios.add(inscripcion.getVoluntario());
        }
        return voluntarios;
    }


    @ManyToOne
    @JoinColumn(name = "albergue_id")
    private Albergue albergue;
}
