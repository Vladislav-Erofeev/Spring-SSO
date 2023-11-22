package com.example.oauthauthorizationserver.domain.entities;

import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue
    private long id;

    private String clientId;
    private String secret;

    @Type(StringArrayType.class)
    @Column(name = "scopes",
    columnDefinition = "text[]")
    private String[] scope;

    @Type(StringArrayType.class)
    @Column(name = "redirect",
    columnDefinition = "text[]")
    private String[] redirectUri;

    @Type(StringArrayType.class)
    @Column(name = "grantTypes",
    columnDefinition = "text[]")
    @Enumerated(value = EnumType.STRING)
    private String[] authGrantType;
}
