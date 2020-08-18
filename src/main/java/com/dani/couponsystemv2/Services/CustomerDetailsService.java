package com.dani.couponsystemv2.Services;

import com.dani.couponsystemv2.model.CustomerDetails;
import com.dani.couponsystemv2.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {

    private CustomerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .map(CustomerDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User by the email: " + email + " does not exists"
                        ));
    }
}
