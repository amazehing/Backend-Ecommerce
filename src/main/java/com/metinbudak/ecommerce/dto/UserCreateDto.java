package com.metinbudak.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserCreateDto {

    @NotBlank
    private String username;

    @NotBlank
    @Length(min = 6)
    private String password;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Email @NotBlank
    private String email;

}
