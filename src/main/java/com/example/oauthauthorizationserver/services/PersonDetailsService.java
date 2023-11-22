package com.example.oauthauthorizationserver.services;

import com.example.oauthauthorizationserver.domain.dto.RegistrationRequest;
import com.example.oauthauthorizationserver.domain.entities.Person;
import com.example.oauthauthorizationserver.domain.entities.PersonDetails;
import com.example.oauthauthorizationserver.security.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByLogin(username);
        if (person.isEmpty())
            throw new UsernameNotFoundException(username);
        return new PersonDetails(person.get());
    }

    @Transactional
    public Person save(RegistrationRequest registrationRequest) {
        Person person = new Person();
        person.setLogin(registrationRequest.getLogin());
        person.setEmail(registrationRequest.getEmail());
        person.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        person.setAuthorities(new String[]{"user"});
        return personRepository.save(person);
    }
}
