package com.ensias.eldycare.authenticationservice.controller;

import com.ensias.eldycare.authenticationservice.model.AuthModel;
import com.ensias.eldycare.authenticationservice.model.controller_params.LoginParams;
import com.ensias.eldycare.authenticationservice.model.controller_params.RegisterParams;
import com.ensias.eldycare.authenticationservice.service.AuthService;
import com.ensias.eldycare.authenticationservice.utils.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("hello !!");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterParams registerParams) {
        AuthModel authModel = new AuthModel();
        authModel.setEmail(registerParams.getEmail());
        authModel.setPhone(registerParams.getPhone());
        authModel.setPassword(registerParams.getPassword());
        authModel.setUsername(registerParams.getUsername());
        authModel.setUserType(registerParams.getUserType());
        LOG.info("Authentication model : " + authModel);
        try {
            authModel = authService.register(authModel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("message", "Successfully registered !");

        ObjectNode authModelNode = objectMapper.createObjectNode();
        authModelNode.put("id", authModel.getId());
        authModelNode.put("username", authModel.getUsername());
        authModelNode.put("email", authModel.getEmail());
        authModelNode.put("phone", authModel.getPhone());
        authModelNode.put("userType", authModel.getUserType().toString());

        rootNode.set("user", authModelNode);

        return ResponseEntity.ok(rootNode);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginParams loginParams) {
        String JWT = "";
        ObjectNode rootNode = objectMapper.createObjectNode();
        try{
            JWT = authService.login(loginParams);
        } catch (RuntimeException e) {
            rootNode.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        rootNode.put("message", "Successfully logged in !");
        rootNode.put("jwt", JWT);
        rootNode.put("userType", authService.getUserType(loginParams.getEmail()).toString());
        return ResponseEntity.ok(rootNode);
    }

    @GetMapping("/validate-jwt")
    public ResponseEntity<?> validateJWT(@RequestHeader(HttpHeaders.AUTHORIZATION) String JwtHeader) {
        String JWT = JwtUtils.extractJwt(JwtHeader);
        boolean isValid = authService.validateJWT(JWT);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("isValid", isValid);

        if (!isValid)
            return ResponseEntity.internalServerError().body(rootNode);
        return ResponseEntity.ok(rootNode);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        try {
            String tokenPrefix = "Bearer ";
            if (!jwt.startsWith(tokenPrefix)) {
                LOG.warn("Invalid token syntax: " + jwt);
                throw new RuntimeException("Invalid token");
            }
            jwt = jwt.substring(tokenPrefix.length());
            authService.logout(jwt);
            rootNode.put("message", "Successfully logged out !");
        } catch (RuntimeException e) {
            LOG.warn(e.getMessage());
            return ResponseEntity.internalServerError().body("Invalid Token");
        }
        return ResponseEntity.ok().body(rootNode);
    }
}
