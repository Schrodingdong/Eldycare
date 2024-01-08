package com.ensias.eldycare.userservice.service;

import com.ensias.eldycare.userservice.model.UserModel;
import com.ensias.eldycare.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserModel addUser(UserModel user) {
        return userRepository.save(user);
    }

    public void addUrgentContact(String userEmail, String urgentContactEmail) throws RuntimeException {
        try {
            userRepository.addUrgentContact(userEmail, urgentContactEmail);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding urgent contact : \n" + e.getMessage());
        }
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUser(String email) {
        return userRepository.findById(email).orElseThrow();
    }

    public Set<UserModel> getUrgentContacts(String email) {
        UserModel user = userRepository.findById(email).orElseThrow();
        return user.getUrgentContacts();
    }
    public Set<UserModel> getElderContacts(String email) {
        return userRepository.getElderContacts(email);
    }

    public void deleteUser(String email) {
        userRepository.deleteById(email);
        // TODO : delete user from Auth-Service
    }

    public void deleteUrgentContact(String userEmail, String urgentContactEmail) {
        userRepository.deleteUrgentContact(userEmail, urgentContactEmail);
    }

    public UserModel updateUser(String email, UserModel userInfo) {
        UserModel user = userRepository.findById(email).orElseThrow();
        user.setUsername(
                userInfo.getUsername() == null ? user.getUsername() : userInfo.getUsername());
        user.setUserType(
                userInfo.getUserType() == null ? user.getUserType() : userInfo.getUserType());
        return userRepository.save(user);
    }

    public void addElderContact(String userEmail, String elderContactEmail) {
        userRepository.addElderContact(userEmail, elderContactEmail);
    }
}
