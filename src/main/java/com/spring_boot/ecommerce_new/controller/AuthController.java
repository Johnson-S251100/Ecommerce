package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.model.User;
import com.spring_boot.ecommerce_new.payload.LoginCredentials;
import com.spring_boot.ecommerce_new.payload.UserDto;
import com.spring_boot.ecommerce_new.service.JwtService;
import com.spring_boot.ecommerce_new.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Map<String,Object>> registerHandler(@Valid @RequestBody User user){
        UserDto userDto=userService.registerUser(user);
        String token =jwtService.generateToken(userDto.getEmail());
        return new ResponseEntity<Map<String, Object>>(Collections.singletonMap("jwt-token",token),HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public  ResponseEntity<Map<String,Object>> loginHandle(@Valid @RequestBody LoginCredentials loginCredentials){

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(),loginCredentials.getPassword());

        Authentication auth=authenticationManager.authenticate(authenticationToken);
        System.out.println(loginCredentials.getEmail()+loginCredentials.getPassword());
        String token=null;
        if(auth.isAuthenticated()){
            System.out.println("Authenticated success");
            token=jwtService.generateToken(loginCredentials.getEmail());

        }
        return new ResponseEntity<Map<String,Object>>(Collections.singletonMap("jwt-token",token),HttpStatus.OK);
    }





}
