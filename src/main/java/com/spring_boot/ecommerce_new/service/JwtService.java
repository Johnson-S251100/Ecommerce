package com.spring_boot.ecommerce_new.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey;

    private JwtService(){
        secretKey =generateSecretKey();
    }

    private String generateSecretKey() {

        try{
            KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey1 = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey1.getEncoded());
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error generating secret key "+e);
        }
    }
    public SecretKey getKey(){
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String name){
        Map<String,Object> claims= new HashMap<>();
        return Jwts.builder()
                .claims(claims)
                .subject(name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getKey())
                .compact();
    }



    //extract the username from JWT token
    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }
    public Date extractExpiration (String token){
        return  extractClaims(token,Claims::getExpiration);
    }
    public boolean isTokenExpired(String token){
        return  extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName=extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String extractToken(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
