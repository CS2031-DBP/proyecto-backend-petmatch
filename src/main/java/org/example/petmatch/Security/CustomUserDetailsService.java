package org.example.petmatch.Security;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Shelter.Domain.Shelter;
import org.example.petmatch.Shelter.Infraestructure.ShelterRepository;
import org.example.petmatch.User.Domain.User;
import org.example.petmatch.User.Infraestructure.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );
        }


        Optional<Shelter> shelterOpt = shelterRepository.findByEmail(email);
        if (shelterOpt.isPresent()) {
            Shelter shelter = shelterOpt.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ALBERGUE")); // ← Verificar esto

            return new org.springframework.security.core.userdetails.User(
                    shelter.getEmail(),
                    shelter.getPassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("Usuario o albergue no encontrado con email: " + email);
    }
}
