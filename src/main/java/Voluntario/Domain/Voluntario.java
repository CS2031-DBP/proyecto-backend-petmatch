package Voluntario.Domain;

import User.Domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "voluntarios")
public class Voluntario extends User {
}
