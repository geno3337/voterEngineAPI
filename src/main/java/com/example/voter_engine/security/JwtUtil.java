package com.example.voter_engine.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String Secret_key = "secret";

    public String createToken(Map<String,Object> clams, String UserName){
        return Jwts.builder().setClaims(clams).setSubject(UserName).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*100))
                .signWith(SignatureAlgorithm.HS256,Secret_key).compact();
    }
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,userDetails.getUsername());
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(Secret_key).parseClaimsJws(token).getBody();

    }

    public <T> T extractclaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token){
        return  extractclaims(token,Claims::getSubject);
    }


    public Date extractExpiration(String token){
        return extractclaims(token,Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final  String userName=extractUsername(token);
        return (userName.equals((userDetails.getUsername())) && isTokenExpired(token));
    }

}
