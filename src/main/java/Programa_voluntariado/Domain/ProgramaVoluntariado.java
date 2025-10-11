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

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private ZonedDateTime fechaInicio;

    @Column(nullable = false)
    private ZonedDateTime fechaFin;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Integer numeroVoluntariosNecesarios;

    private Integer numeroVoluntariosInscritos = 0;

    @Enumerated(EnumType.STRING)
    private ProgramaStatus status;


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
