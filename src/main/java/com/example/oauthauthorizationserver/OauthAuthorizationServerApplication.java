package com.example.oauthauthorizationserver;

import com.example.oauthauthorizationserver.security.RegisteredClientRepositoryImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootApplication
public class OauthAuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthAuthorizationServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RegisteredClientRepositoryImpl clientRepository) {
        return arg -> {
            List<RegisteredClient> registeredClientList = List.of(
                    RegisteredClient
                            .withId("client")
                            .clientId("client")
                            .clientSecret("secret")
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
                            .clientSecret("secret")
                            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                            .build(),
                    RegisteredClient.withId("api-key")
                            .clientId("api")
                            .clientSecret("api")
                            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                            .scope("api_key")
                            .build());

                    for (RegisteredClient x : registeredClientList)
                        clientRepository.save(x);
        };
    }
}
