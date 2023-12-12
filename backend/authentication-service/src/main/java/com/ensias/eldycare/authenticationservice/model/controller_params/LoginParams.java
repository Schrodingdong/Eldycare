package com.ensias.eldycare.authenticationservice.model.controller_params;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginParams {
    @NotBlank(message = "The email must not be null")
    @Email(message = "The email must be a valid email")
    private String email;
    @NotBlank(message = "The email must not be null")
    private String password;
}