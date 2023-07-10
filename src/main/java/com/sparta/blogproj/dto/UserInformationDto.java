package com.sparta.blogproj.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserInformationDto {
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()_=+-]{8,15}$")
    private String password;
    private boolean admin = false;
    private String adminToken = "";

}