package com.dani.couponsystemv2.controllers;

import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.dto.LoginDto;
import com.dani.couponsystemv2.dto.ResponseDto;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.facades.AdminFacade;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Customer;
import com.dani.couponsystemv2.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController extends ClientController {

    @Value("${admin-email}")
    public String email;

    @Value("${admin-password}")
    public String password;

    private final AdminFacade facade;

    public AdminController(AuthenticationManager authenticationManager, JwtService jwtService, AdminFacade facade) {
        super(authenticationManager, jwtService);
        this.facade = facade;
    }

    @PostMapping("/authenticate")
    @Override
    public ResponseEntity<ResponseDto> authenticate(@RequestBody LoginDto loginDto) {
        try {
            authentication(loginDto);

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure(e.getMessage()));
        }

        return ResponseEntity.ok(
                ResponseDto.of(
                        facade.login(loginDto.getEmail(), loginDto.getPassword()),
                        jwtService.encodeAdmin(
                                new UserEntity(email, password)
                        )));
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        return facade.login(loginDto.getEmail(), loginDto.getPassword()) ?
                ResponseEntity.ok(ResponseDto.success(true)) :
                ResponseEntity.ok(ResponseDto.failure("In order to continue, you must log in"));
    }

    @PostMapping("/company/add")
    public ResponseEntity addCompany(@RequestBody Company company) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.addCompany(company)));
//        } catch (LoggedOutException e) {
//            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(ResponseDto.failure(e));
        }
    }

    @PutMapping("/company/update")
    public ResponseEntity updateCompany(@RequestBody Company company) {
        try {
            return ResponseEntity.ok(
                    ResponseDto.success(
                            facade.updateCompany(
                                    company.getEmail(),
                                    company.getPassword(),
                                    company.getId())
                    ));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(ResponseDto.failure(e));
        }
    }

    @DeleteMapping("/company/delete/{id}")
    public ResponseEntity deleteCompany(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.deleteCompany(id)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/company/get/{id}")
    public ResponseEntity getOneCompany(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.getOneCompany(id)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/company/all")
    public ResponseEntity getAllCompanies() {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.getAllCompanies()));
        } catch (LoggedOutException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }


    @PostMapping("/customer/add")
    public ResponseEntity addCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.addCustomer(customer)));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(ResponseDto.failure(e));
        }
    }

    @PutMapping("/customer/update")
    public ResponseEntity updateCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(
                    ResponseDto.success(
                            facade.updateCustomer(
                                    customer
                            )));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(ResponseDto.failure(e));
        }
    }

    @DeleteMapping("/customer/delete/{id}")
    public ResponseEntity deleteCustomer(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.deleteCustomer(id)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/customer/get/{id}")
    public ResponseEntity getOneCustomer(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.getOneCustomer(id)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/customer/all")
    public ResponseEntity getAllCustomers() {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.getAllCustomers()));
        } catch (LoggedOutException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }
}
