package org.example.petmatch.Volunteer.Infraestructure;

import org.example.petmatch.Volunteer.Domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Optional<Volunteer> findById(Long id);
    Optional<Volunteer> findByEmail(String email);
}
