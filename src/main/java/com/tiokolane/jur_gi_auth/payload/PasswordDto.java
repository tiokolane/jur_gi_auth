package com.tiokolane.jur_gi_auth.payload;

import lombok.Data;

@Data
public class PasswordDto {

    private String oldPassword;

    private  String token;

    @ValidPassword
    private String newPassword;
}