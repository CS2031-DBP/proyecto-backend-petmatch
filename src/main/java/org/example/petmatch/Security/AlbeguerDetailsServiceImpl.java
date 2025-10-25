package org.example.petmatch.Security;

import lombok.RequiredArgsConstructor;
import org.example.petmatch.Albergue.Domain.Albergue;
import org.example.petmatch.Albergue.Infraestructure.AlbergueRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbeguerDetailsServiceImpl implements UserDetailsService {
    private final AlbergueRepository albergueRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Albergue albergue = albergueRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Albergue no encontrado"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ALBERGUE"));

        return new org.springframework.security.core.userdetails.User(
                albergue.getEmail(),
                albergue.getPassword(),
                authorities
        );
    }

}
