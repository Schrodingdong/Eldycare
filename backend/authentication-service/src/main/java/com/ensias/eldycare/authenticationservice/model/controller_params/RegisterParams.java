package com.ensias.eldycare.authenticationservice.model.controller_params;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterParams {
    @NotBlank(message = "The email must not be null")
    @Email(message = "The email must be a valid email")
    private String email;
    @NotBlank(message = "The password must not be null")
    private String password;
    @NotBlank(message = "The username must not be null")
    private String username;
    @NotNull(message = "The user type must not be null")
    private UserType userType;
}