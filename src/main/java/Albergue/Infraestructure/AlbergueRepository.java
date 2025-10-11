package Albergue.Infraestructure;

import Albergue.Domain.Albergue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbergueRepository extends JpaRepository<Albergue, Long> {
    Optional<Albergue> findByName(@NonNull @NotEmpty @Pattern(regexp = "^[A-Z].+") String name);
}
