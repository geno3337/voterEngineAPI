package com.example.voter_engine.Response;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
public class jwtResponse {

    private String token;

    private String email;

    private List<String> authorities;

    public jwtResponse(String jwt, String username, List<String> role) {
        this.token = jwt;
        this.email = username;
        this.authorities = role;
    }

}
