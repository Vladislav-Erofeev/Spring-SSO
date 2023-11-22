package com.example.oauthauthorizationserver.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.array.StringArrayType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String login;
    @JsonIgnore
    private String password;

    @JsonIgnore
    @Type(StringArrayType.class)
    @Column(name = "authorities", columnDefinition = "text[]")
    private String[] authorities;
}
