package com.dani.couponsystemv2.controllers;

import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.dto.LoginDto;
import com.dani.couponsystemv2.dto.ResponseDto;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.facades.CustomerFacade;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/customer")
public class CustomerController extends ClientController {

    private final CustomerFacade facade;

    public CustomerController(AuthenticationManager authenticationManager, JwtService jwtService, CustomerFacade facade) {
        super(authenticationManager, jwtService);
        this.facade = facade;
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
        return facade.login(loginDto.getEmail(), loginDto.getPassword()) ?
                ResponseEntity.ok(ResponseDto.success(true)) :
                ResponseEntity.ok(ResponseDto.failure("In order to continue, you must log in"));
    }

    @PostMapping("/authenticate")
    @Override
    public ResponseEntity<ResponseDto> authenticate(@RequestBody LoginDto loginDto) {
        try {
            authentication(encryptLogin(loginDto));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure(e.getMessage()));
        }

        try {
            return ResponseEntity.ok(
                    ResponseDto.of(
                            facade.login(loginDto.getEmail(), loginDto.getPassword()),
                            jwtService.encodeCustomer(
                                    facade.getCustomerDetails()
                            )));
        } catch (LoggedOutException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure(e.getMessage()));
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity purchaseCoupon(@RequestBody Coupon coupon) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.purchaseCoupon(coupon)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/coupons")
    public ResponseEntity getCustomerCoupons(@RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                             @RequestParam(value = "category", required = false) CategoryType type) {
        try {
            if (maxPrice != null){
                return ResponseEntity.ok(ResponseDto.success(facade.getCustomerCoupons(maxPrice)));
            }else if (type != null) {
                return ResponseEntity.ok(ResponseDto.success(facade.getCustomerCoupons(type)));
            }else{
                return ResponseEntity.ok(ResponseDto.success(facade.getCustomerCoupons()));
            }
        } catch (LoggedOutException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/details")
    public ResponseEntity getCustomerDetails() {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.getCustomerDetails()));
        } catch (LoggedOutException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }
}
