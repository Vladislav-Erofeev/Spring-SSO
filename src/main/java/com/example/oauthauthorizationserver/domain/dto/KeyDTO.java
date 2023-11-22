package com.example.oauthauthorizationserver.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyDTO {
    private String clientId;
    private String clientSecret;
}
