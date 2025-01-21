package com.spring_boot.ecommerce_new.service;


import com.spring_boot.ecommerce_new.configuration.UserPrincipal;
import com.spring_boot.ecommerce_new.exceptions.ResourceNotFoundException;
import com.spring_boot.ecommerce_new.model.User;
import com.spring_boot.ecommerce_new.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(username);
        if(user==null){
            throw new ResourceNotFoundException("User","email",username);
        }
        return  new UserPrincipal(user);
    }
}
