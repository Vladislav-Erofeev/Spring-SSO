package com.example.oauthauthorizationserver.security;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TokenInfo {

    private Boolean active;
    private String sub;
    private List<String> aud;
    private String clientId;
    private int id;
    private String tokenType;

    private Object principal;
    private List<String> authorities;
}
