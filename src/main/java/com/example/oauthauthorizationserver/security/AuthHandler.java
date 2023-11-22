package com.example.oauthauthorizationserver.security;

import com.example.oauthauthorizationserver.domain.entities.PersonDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenIntrospection;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenIntrospectionAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthHandler implements AuthenticationSuccessHandler {
    private final static String principalAttributeKey = "java.security.Principal";
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private OAuth2AuthorizationService auth2AuthorizationService;
    private final RegisteredClientRepository myClientRepo;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2TokenIntrospectionAuthenticationToken introspectionAuthenticationToken  =
                (OAuth2TokenIntrospectionAuthenticationToken) authentication;
        TokenInfo.TokenInfoBuilder tokenInfoBuilder = new TokenInfo.TokenInfoBuilder();
        tokenInfoBuilder.active(introspectionAuthenticationToken.getTokenClaims().isActive());

        if (introspectionAuthenticationToken.getTokenClaims().isActive()) {
            OAuth2TokenIntrospection introspection = introspectionAuthenticationToken.getTokenClaims();
            tokenInfoBuilder.active(true)
                    .sub(introspection.getSubject())
                    .aud(introspection.getAudience())
                    .clientId(introspection.getClientId())
                    .tokenType(introspection.getTokenType());

            List<String> authorities = new LinkedList<>();

            // adding client scopes
            RegisteredClient registeredClient = myClientRepo.findByClientId(introspection.getClientId());
            authorities.addAll(registeredClient.getScopes());

            // getting access token
            OAuth2Authorization auth2Authorization = auth2AuthorizationService.findByToken(introspectionAuthenticationToken.getToken(),
                    OAuth2TokenType.ACCESS_TOKEN);
            if (auth2Authorization != null) {
                Authentication authentication1 = auth2Authorization.getAttribute(principalAttributeKey);
                // writing principal details
                if (authentication1 != null) {
                    Map<String, Object> principal = new HashMap<>();
                    PersonDetails userDetails = (PersonDetails) authentication1.getPrincipal();

                    // adding principal authorities
                    authentication1.getAuthorities().stream().forEach(s -> {
                        authorities.add(s.getAuthority());
                    });
                    principal.put("username", userDetails.getUsername());
                    principal.put("authorities", userDetails.getAuthorities());
                    tokenInfoBuilder.id(userDetails.getPerson().getId());

                    tokenInfoBuilder.principal(principal);
                }
            }
            tokenInfoBuilder.authorities(authorities);
        }
        ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response);
        mappingJackson2HttpMessageConverter.write(tokenInfoBuilder.build(), null, servletServerHttpResponse);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }
}
