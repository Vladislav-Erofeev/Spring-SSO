package com.example.oauthauthorizationserver.services;

import com.example.oauthauthorizationserver.domain.entities.Person;
import com.example.oauthauthorizationserver.security.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;

    public Person getById(int id) throws Exception {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isEmpty())
            throw new Exception("user not found");
        return optionalPerson.get();
    }
}
