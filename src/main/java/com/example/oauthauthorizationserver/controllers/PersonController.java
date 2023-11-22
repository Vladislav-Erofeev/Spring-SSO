package com.example.oauthauthorizationserver.controllers;

import com.example.oauthauthorizationserver.domain.dto.KeyDTO;
import com.example.oauthauthorizationserver.domain.dto.RegistrationRequest;
import com.example.oauthauthorizationserver.domain.entities.Person;
import com.example.oauthauthorizationserver.security.RegisteredClientRepositoryImpl;
import com.example.oauthauthorizationserver.services.PersonDetailsService;
import com.example.oauthauthorizationserver.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PersonController {
    private final PersonDetailsService personDetailsService;
    private final RegisteredClientRepositoryImpl registeredClientRepository;
    private final PersonService personService;

    @PostMapping("/register")
    public Person register(@RequestBody RegistrationRequest registrationRequest) {
        return personDetailsService.save(registrationRequest);
    }


    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/profile")
    public Person getById(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) throws Exception {
        return personService.getById(oAuth2AuthenticatedPrincipal.getAttribute("id"));
    }

    @PreAuthorize("hasAnyAuthority('user')")
    @GetMapping("/generate_key")
    public KeyDTO generateApiKey() {
        KeyDTO keyDTO = new KeyDTO();
        keyDTO.setClientId(UUID.randomUUID().toString());
        keyDTO.setClientSecret(UUID.randomUUID().toString());
        registeredClientRepository.save(RegisteredClient.withId("1").clientId(keyDTO.getClientId())
                .clientSecret(keyDTO.getClientSecret())
                .scope("api_key")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).build());
        return keyDTO;
    }
}
