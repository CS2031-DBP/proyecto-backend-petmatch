package org.example.petmatch.Volunteer_Program.Infraestructure;

import org.example.petmatch.Volunteer_Program.Domain.VolunteerProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VolunteerProgramRepository extends JpaRepository<VolunteerProgram, Long> {
    Optional<VolunteerProgram> findById(Long id);
}
