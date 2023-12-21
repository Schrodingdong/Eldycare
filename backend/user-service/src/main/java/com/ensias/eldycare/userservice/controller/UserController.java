package com.ensias.eldycare.userservice.controller;

import com.ensias.eldycare.userservice.model.UserModel;
import com.ensias.eldycare.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserModel user) {
        UserModel newUser = userService.addUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/add/urgent-contact/{urgentContactEmail}")
    public ResponseEntity<?> addUrgentContact(@PathVariable String urgentContactEmail,
            @RequestHeader("User-Email") String userEmail) {
        try {
            userService.addUrgentContact(userEmail, urgentContactEmail);
            return ResponseEntity.ok("Urgent contact added successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // TODO Recursion problem to solve
    // @GetMapping("/get/all")
    // public ResponseEntity<?> getAllUsers() {
    // List<UserModel> users = userService.getAllUsers();
    // return ResponseEntity.ok(users);
    // }

    @GetMapping("/get/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        UserModel user = userService.getUser(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get/urgent-contacts")
    public ResponseEntity<?> getUrgentContacts(@RequestHeader("User-Email") String userEmail) {
        return ResponseEntity.ok(userService.getUrgentContacts(userEmail));
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted successfully");
    }

    @DeleteMapping("/delete/urgent-contact/{urgentContactEmail}")
    public ResponseEntity<?> deleteUrgentContact(@PathVariable String urgentContactEmail,
            @RequestHeader("User-Email") String userEmail) {
        userService.deleteUrgentContact(userEmail, urgentContactEmail);
        return ResponseEntity.ok("Urgent contact deleted successfully");
    }

    @PatchMapping("/update/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody UserModel userInfo) {
        UserModel user = userService.updateUser(email, userInfo);
        return ResponseEntity.ok(user);
    }

}
