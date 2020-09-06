package com.dani.couponsystemv2.controllers;

import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.dto.LoginDto;
import com.dani.couponsystemv2.dto.ResponseDto;
import com.dani.couponsystemv2.facades.CompanyFacade;
import com.dani.couponsystemv2.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/company")
public class CompanyController extends ClientController {

    private final CompanyFacade facade;

    public CompanyController(AuthenticationManager authenticationManager, JwtService jwtService, CompanyFacade facade) {
        super(authenticationManager, jwtService);
        this.facade = facade;
    }

    @Override
    public ResponseEntity<ResponseDto> login(LoginDto loginDto) {
        try {
            authentication(loginDto);


        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure(e.getMessage()));
        }

        ;


        return ResponseEntity.ok(
                ResponseDto.of(
                        facade.login(loginDto.getEmail(), loginDto.getPassword()),
                        jwtService.encodeCompany(
                                new UserEntity(loginDto.getEmail(), loginDto.getPassword())
                        )));
    }

    @Override
    public ResponseEntity<ResponseDto> authenticate(LoginDto loginDto) {
        return null;
    }
}
