package com.ensias.eldycare.authenticationservice.controller;


import com.ensias.eldycare.authenticationservice.model.AuthModel;
import com.ensias.eldycare.authenticationservice.model.controller_params.LoginParams;
import com.ensias.eldycare.authenticationservice.model.controller_params.RegisterParams;
import com.ensias.eldycare.authenticationservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final Logger LOG = LoggerFactory.getLogger(AuthController.class);


    @GetMapping
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("hello !!");
    }

    @PostMapping("/register")
    public  ResponseEntity<?> register(@Validated  @RequestBody RegisterParams registerParams){
        AuthModel authModel = new AuthModel();
        authModel.setEmail(registerParams.getEmail());
        authModel.setPassword(registerParams.getPassword());
        authModel.setUsername(registerParams.getUsername());
        LOG.info("Authentication model : " + authModel);
        try{
            authModel = authService.register(authModel);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok("Successfully registered !\n" + authModel);
    }

    @PostMapping("/login")
    public  ResponseEntity<?> login(@Validated @RequestBody LoginParams loginParams){
        String JWT = authService.login(loginParams);
        return ResponseEntity.ok(JWT);
    }

    @PostMapping("/validate-jwt")
    public  ResponseEntity<?> validateJWT(String JWT){
        boolean isValid = authService.validateJWT(JWT);
        if (!isValid) return ResponseEntity.badRequest().body("Invalid JWT");
        return ResponseEntity.ok("valid JWT");
    }

    @PostMapping("/logout")
    public  ResponseEntity<?> logout(){
        return ResponseEntity.ok("logout !!");
    }
}
