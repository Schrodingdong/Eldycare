package com.ensias.eldycare.authenticationservice.service;

import com.ensias.eldycare.authenticationservice.model.AuthModel;
import com.ensias.eldycare.authenticationservice.model.controller_params.LoginParams;
import com.ensias.eldycare.authenticationservice.repository.AuthRepository;
import com.ensias.eldycare.authenticationservice.utils.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtUtils jwtUtils;
    private final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    public AuthModel register(AuthModel authModel) throws RuntimeException{
        // check Mails
        if (authRepository.existsByEmail(authModel.getEmail())){
            throw new RuntimeException("The email is already registered");
        }
        return authRepository.save(authModel);
    }
    public String login(LoginParams loginParams) throws RuntimeException{
        String email = loginParams.getEmail();
        String password = loginParams.getPassword();
        AuthModel authModel = authRepository.findByEmail(email);
        if(authModel == null){
            throw new RuntimeException("The email is not registered");
        } else {
            if (authModel.getPassword().equals(password)){
                return generateJwt(email);
            } else {
                throw new RuntimeException("The password is incorrect");
            }
        }
    }

    public void logout(String jwt){
        jwtUtils.blackListToken(jwt);
    }

    public String generateJwt(String userEmail){
        return jwtUtils.generateToken(userEmail);
    }
    public boolean validateJWT(String jwt){
        try{
            jwtUtils.validateToken(jwt);
            return true;
        } catch (RuntimeException e){
            LOG.error(e.getMessage());
            return false;
        }
    }
}
