package com.dani.couponsystemv2.controllers;

import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.dto.LoginDto;
import com.dani.couponsystemv2.dto.ResponseDto;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.facades.CompanyFacade;
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
@RequestMapping("/company")
public class CompanyController extends ClientController {

    private final CompanyFacade facade;

    public CompanyController(AuthenticationManager authenticationManager, JwtService jwtService, CompanyFacade facade) {
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
                            jwtService.encodeCompany(
                                    facade.getCompanyDetails()
                            )));
        } catch (LoggedOutException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure(e.getMessage()));
        }

    }

    @PostMapping("/coupon/add/{category}")
    public ResponseEntity addCoupon(@RequestBody Coupon coupon, @PathVariable("category") CategoryType category) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.addCoupon(coupon, category)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(ResponseDto.failure(e));
        }
    }

    @PutMapping("/coupon/update")
    public ResponseEntity updateCoupon(@RequestBody Coupon coupon) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.updateCoupon(coupon)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(ResponseDto.failure(e));
        }
    }

    @DeleteMapping("/coupon/delete/{id}")
    public ResponseEntity deleteCoupon(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.deleteCoupon(id)));
        } catch (LoggedOutException | DoesntExistException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/coupon/all")
    public ResponseEntity getCoupons(@RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                     @RequestParam(value = "category", required = false) CategoryType type) {
        try {
            if (maxPrice != null) {
                return ResponseEntity.ok(ResponseDto.success(facade.getCompanyCoupons(maxPrice)));
            } else if (type != null) {
                return ResponseEntity.ok(ResponseDto.success(facade.getCompanyCoupons(type)));
            } else {
                return ResponseEntity.ok(ResponseDto.success(facade.getCompanyCoupons()));
            }
        } catch (LoggedOutException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }

    @GetMapping("/details")
    public ResponseEntity getCompanyDetails() {
        try {
            return ResponseEntity.ok(ResponseDto.success(facade.getCompanyDetails()));
        } catch (LoggedOutException e) {
            return ResponseEntity.ok(ResponseDto.failure(e.getMessage()));
        }
    }
}
