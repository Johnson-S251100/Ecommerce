package com.spring_boot.ecommerce_new.service;

import com.spring_boot.ecommerce_new.exceptions.ApiException;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.Address;
import com.spring_boot.ecommerce_new.model.Role;
import com.spring_boot.ecommerce_new.model.User;
import com.spring_boot.ecommerce_new.payload.AddressDTO;
import com.spring_boot.ecommerce_new.payload.CommonMapper;
import com.spring_boot.ecommerce_new.repository.AddressRepo;
import com.spring_boot.ecommerce_new.repository.RoleRepo;
import com.spring_boot.ecommerce_new.repository.UserRepo;
import org.apache.tomcat.util.http.parser.Cookie;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class AddressService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private RoleRepo roleRepo;

    public AddressDTO createAddress(String emailId, Address address) {

        User user=userRepo.findByEmail(emailId);
        if (user == null) {

            throw new ResourceNotFoundException("User","emailId",emailId);
        }
        Address newAddress=new Address();
        newAddress.setBuildingName(address.getBuildingName());
        newAddress.setStreet(address.getStreet());
        newAddress.setCity(address.getCity());
        newAddress.setState(address.getState());
        newAddress.setPincode(address.getPincode());
        newAddress.setCountry(address.getCountry());
        newAddress.setUser(user);

        addressRepo.save(newAddress);
        return CommonMapper.INSTANCE.toAdressDTO(newAddress);

    }

    public List<AddressDTO> getAddress() {
        List<Address> addresses=addressRepo.findAll();
        if(addresses.isEmpty()){
            throw new ApiException("User not  Enter Address yet");
        }
        List<AddressDTO> addressDTOS=addresses.stream().map(CommonMapper.INSTANCE::toAdressDTO).toList();
        return addressDTOS;
    }

    public List<AddressDTO> getAddressOfUser(String emailId) {
        User user=userRepo.findByEmail(emailId);
        if(user==null){
            throw new ResourceNotFoundException("User","email",emailId);
        }

        List<Address> addresses=addressRepo.findByUserUserId(user.getUserId());
        return addresses.stream().map(CommonMapper.INSTANCE::toAdressDTO).toList();

    }

    public AddressDTO getAddresses(String emailId, UUID addressId) {
        User user=userRepo.findByEmail(emailId);
        if(user==null){
            throw new ResourceNotFoundException("user","email",emailId);

        }
        Address address=addressRepo.findByAddressIdAndUserUserId(addressId,user.getUserId());
        return CommonMapper.INSTANCE.toAdressDTO(address);
    }

    public AddressDTO updateAddress(String emailId, UUID addressId, Address address) {

        User user=userRepo.findByEmail(emailId);
        if(user==null){
            throw new ResourceNotFoundException("user","email",emailId);
        }

        Address savedAddress=addressRepo.findByAddressIdAndUserUserId(addressId,user.getUserId());

        savedAddress.setBuildingName(address.getBuildingName());
        savedAddress.setStreet(address.getStreet());
        savedAddress.setCity(address.getCity());
        savedAddress.setStreet(address.getState());
        savedAddress.setPincode(address.getPincode());
        savedAddress.setCountry(address.getCountry());

        addressRepo.save(savedAddress);

        return CommonMapper.INSTANCE.toAdressDTO(savedAddress);
    }

    public String deleteAddress(UUID addressId, String emailId) {

        User user=userRepo.findByEmail(emailId);

        if(user==null){
            throw new ResourceNotFoundException("user","email",emailId);
        }
        Address address=addressRepo.findByAddressIdAndUserUserId(addressId,user.getUserId());
        addressRepo.delete(address);
        return "Address with "+address.getAddressId()+" deleted Successfully";
    }
}
