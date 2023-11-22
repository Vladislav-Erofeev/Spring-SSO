package com.example.oauthauthorizationserver;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class MyClientRepo implements RegisteredClientRepository {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final List<RegisteredClient> registeredClientList = List.of(
            RegisteredClient
                    .withId("client")
                    .clientId("client")
                    .clientSecret(passwordEncoder.encode("secret"))
                    .scope("write")
                    .redirectUri("http://lcoalhost:9000/token")
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .tokenSettings(TokenSettings.builder()
                            .reuseRefreshTokens(false)
                            .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                            .idTokenSignatureAlgorithm(SignatureAlgorithm.ES256)
                            .accessTokenTimeToLive(Duration.of(30, ChronoUnit.MINUTES))
                            .build())
                    .build(),
            RegisteredClient.withId("resource")
                    .clientId("resource")
                    .clientSecret(passwordEncoder.encode("secret"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .build(),
            RegisteredClient.withId("api-key")
                    .clientId("api")
                    .clientSecret(passwordEncoder.encode("api"))
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .scope("api_key")
                    .build()
    );

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return registeredClientList.stream().filter(client -> client.getId().equals(id)).findFirst().get();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return registeredClientList.stream().filter(client -> client.getClientId().equals(clientId)).findFirst().get();
    }
}
