package com.ensias.eldycare.authenticationservice.amqp.model;

import com.ensias.eldycare.authenticationservice.model.AuthModel;
import com.ensias.eldycare.authenticationservice.model.controller_params.UserType;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserModel implements Serializable {
    private String email;
    private String username;
    private String phone;
    private String userType;

    public UserModel(AuthModel authModel) {
        this.email = authModel.getEmail();
        this.phone = authModel.getPhone();
        this.username = authModel.getUsername();
        this.userType = authModel.getUserType().toString();
    }
}
