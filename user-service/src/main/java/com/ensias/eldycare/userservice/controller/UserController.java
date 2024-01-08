package com.ensias.eldycare.userservice.controller;

import com.ensias.eldycare.userservice.model.UserModel;
import com.ensias.eldycare.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserModel user) {
        LOG.info("User model : " + user);
        UserModel newUser = userService.addUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PutMapping("/add/urgent-contact/{urgentContactEmail}")
    public ResponseEntity<?> addUrgentContact(@PathVariable String urgentContactEmail,
            @RequestHeader("User-Email") String userEmail) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        try {
            userService.addUrgentContact(userEmail, urgentContactEmail);
            rootNode.put("message", "Urgent contact added successfully");
            return ResponseEntity.ok(rootNode);
        } catch (Exception e) {
            rootNode.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(rootNode);
        }
    }

    @PutMapping("/add/elder-contact/{elderContactEmail}")
    public ResponseEntity<?> addElderContact(@PathVariable String elderContactEmail,
            @RequestHeader("User-Email") String userEmail) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        try {
            userService.addElderContact(userEmail, elderContactEmail);
            rootNode.put("message", "Elder contact added successfully");
            return ResponseEntity.ok(rootNode);
        } catch (Exception e) {
            rootNode.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(rootNode);
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

    @GetMapping("/get/elder-contacts")
    public ResponseEntity<?> getElderContacts(@RequestHeader("User-Email") String userEmail) {
        return ResponseEntity.ok(userService.getElderContacts(userEmail));
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
