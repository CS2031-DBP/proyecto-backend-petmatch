package Voluntario.Domain;

import Usuario.Domain.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "voluntarios")
public class Voluntario extends Usuario {

}
