package com.dani.couponsystemv2.Services;

import com.dani.couponsystemv2.model.AdminDetails;
import com.dani.couponsystemv2.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminDetailsService implements UserDetailsService {

    @Value("${admin-email}")
    public String username;

    @Value("${admin-password}")
    public String password;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return Optional.ofNullable(email).filter(subject -> subject.equals(username))
                .map(subject -> new AdminDetails(
                        new UserEntity(username,password)
                ))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Wrong username or password"
                ));
    }
}
