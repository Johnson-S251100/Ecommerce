package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.payload.UserDto;
import com.spring_boot.ecommerce_new.payload.UserResponse;
import com.spring_boot.ecommerce_new.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static java.lang.Math.log10;

@RestController
@RequestMapping("/ecommerce")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register/ll/{pageNum}/{pageSize}")
    public ResponseEntity<UserResponse> getAllUsers(@PathVariable int pageNum, @PathVariable int pageSize){
        UserResponse userResponse= userService.getAllUsers(pageNum,pageSize);
        System.out.println(userResponse);
        return  new ResponseEntity<UserResponse>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/public/user")
    public ResponseEntity<UserDto> getUser(HttpServletRequest request){
        UserDto userDTO = userService.getUser(request);

        return new ResponseEntity<UserDto>(userDTO,HttpStatus.OK);
    }

    @PutMapping("/public/user")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, HttpServletRequest request){
        UserDto users=userService.updateUser(userDto,request);

        return new ResponseEntity<UserDto>(users,HttpStatus.OK);
    }

    @DeleteMapping("/public/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId){
        String deleteUser=userService.deleteUser(userId);
        return new ResponseEntity<String>(deleteUser,HttpStatus.OK);

    }


}
