package com.example.oauthauthorizationserver.security;

import com.example.oauthauthorizationserver.domain.entities.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegisteredClientRepositoryImpl implements RegisteredClientRepository {
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        Client client = new Client();
        client.setClientId(registeredClient.getClientId());
        client.setSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
        String[] scopes = new String[]{};
        scopes = registeredClient.getScopes().toArray(scopes);
        client.setScope(scopes);

        String[] redirectUris = new String[]{};
        redirectUris = registeredClient.getRedirectUris().toArray(redirectUris);
        client.setRedirectUri(redirectUris);

        List<String> authGrantTypes = new LinkedList<>();
        for (AuthorizationGrantType x : registeredClient.getAuthorizationGrantTypes())
            authGrantTypes.add(x.getValue());
        String[] auth = new String[]{};
        auth = authGrantTypes.toArray(auth);
        client.setAuthGrantType(auth);
        clientRepository.save(client);
    }

    @Override
    public RegisteredClient findById(String id) {
        Optional<Client> optionalClient = clientRepository.findById(Long.valueOf(id));
        if (optionalClient.isEmpty())
            return null;
        Client client = optionalClient.get();

        RegisteredClient.Builder builder = RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(client.getClientId())
                .clientSecret(client.getSecret());
        for (String x : client.getScope())
            builder.scope(x);
        for (String x : client.getRedirectUri())
            builder.redirectUri(x);
        for (String x : client.getAuthGrantType())
            builder.authorizationGrantType(new AuthorizationGrantType(x));
        return builder.build();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Optional<Client> optionalClient = clientRepository.findByClientId(clientId);
        if (optionalClient.isEmpty())
            return null;
        Client client = optionalClient.get();

        RegisteredClient.Builder builder = RegisteredClient.withId(String.valueOf(client.getId()))
                .clientId(clientId)
                .clientSecret(client.getSecret());
        for (String x : client.getScope())
            builder.scope(x);
        for (String x : client.getRedirectUri())
            builder.redirectUri(x);
        for (String x : client.getAuthGrantType())
            builder.authorizationGrantType(new AuthorizationGrantType(x));
        return builder.build();
    }
}
