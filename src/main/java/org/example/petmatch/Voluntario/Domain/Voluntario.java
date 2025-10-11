package org.example.petmatch.Voluntario.Domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.petmatch.User.Domain.User;

@Getter
@Setter
@Entity
@Table(name = "voluntarios")
public class Voluntario extends User {

}
