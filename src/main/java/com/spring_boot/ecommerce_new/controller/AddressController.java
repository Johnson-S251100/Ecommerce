package com.spring_boot.ecommerce_new.controller;

import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.Address;
import com.spring_boot.ecommerce_new.payload.AddressDTO;
import com.spring_boot.ecommerce_new.repository.UserRepo;
import com.spring_boot.ecommerce_new.service.AddressService;
import com.spring_boot.ecommerce_new.service.JwtService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.GeneratedValue;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
@SecurityRequirement(name = "E-Commerce")
public class AddressController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AddressService addressService;

    @PostMapping("/public/users")
    public ResponseEntity<AddressDTO> createAddress(HttpServletRequest request, @Valid @RequestBody Address address){

        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        AddressDTO addressDTO=addressService.createAddress(emailId,address);
        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.CREATED);
    }
    @GetMapping("/admin/address")
    public ResponseEntity<List<AddressDTO>> getAddress(){
        List<AddressDTO> addressDTOS=addressService.getAddress();
        return new ResponseEntity<List<AddressDTO>>(addressDTOS,HttpStatus.OK);
    }

    @GetMapping("public/address")
    public ResponseEntity<List<AddressDTO>> getAddressByUser(HttpServletRequest request){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
//        if(emailId==null){
//            throw new ResourceNotFoundException("User","email",emailId);
//        }
        List<AddressDTO> addressDTO=addressService.getAddressOfUser(emailId);
        return new ResponseEntity<List<AddressDTO>>(addressDTO,HttpStatus.OK);

    }
    @GetMapping("public/address/{addressId}")
    public ResponseEntity<AddressDTO> getAddresses(HttpServletRequest request,@PathVariable UUID addressId)
    {
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);

        AddressDTO addressDTO=addressService.getAddresses(emailId,addressId);
        return new ResponseEntity<AddressDTO>(addressDTO,HttpStatus.OK);
    }

    @PutMapping("/public/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(HttpServletRequest request,@PathVariable UUID addressId,@RequestBody Address address){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);
        AddressDTO addressDTO=addressService.updateAddress(emailId,addressId,address);
        return new ResponseEntity<AddressDTO>(addressDTO,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/public/address/{addressId}")
    public ResponseEntity<String> deleteAddress(HttpServletRequest request,@PathVariable UUID addressId){
        String token=jwtService.extractToken(request);
        String emailId=jwtService.extractUserName(token);

        String response=addressService.deleteAddress(addressId,emailId);
        return new ResponseEntity<String>(response,HttpStatus.OK);
    }


}
