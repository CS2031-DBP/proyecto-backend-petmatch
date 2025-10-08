package Albergue.Infraestructure;

import Albergue.Domain.Albergue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbergueRepository extends JpaRepository<Albergue, Long> {
}
