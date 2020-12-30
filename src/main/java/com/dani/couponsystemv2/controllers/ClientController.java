package com.dani.couponsystemv2.controllers;

import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.dto.LoginDto;
import com.dani.couponsystemv2.dto.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public abstract class ClientController {

    protected final AuthenticationManager authenticationManager;
    protected final JwtService jwtService;

    protected void authentication(LoginDto loginDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
    }

    public abstract ResponseEntity<ResponseDto> login(LoginDto loginDto);

    public abstract ResponseEntity<ResponseDto> authenticate(LoginDto loginDto);

    public LoginDto encryptLogin(LoginDto loginDto){
        LoginDto temp = new LoginDto(loginDto.getEmail(),loginDto.getPassword());
        temp.encryptPassword();
        return temp;
    }
}
