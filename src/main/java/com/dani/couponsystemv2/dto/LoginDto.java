package com.dani.couponsystemv2.dto;

import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

import static org.apache.commons.codec.digest.DigestUtils.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginDto {
    private String email;
    private String password;

    public void encryptPassword(){
        password = md5Hex(password);
    }
}