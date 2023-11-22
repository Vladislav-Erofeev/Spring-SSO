package com.example.oauthauthorizationserver.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String login;
    private String email;
    private String password;

}
