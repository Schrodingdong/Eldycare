package com.ensias.eldycare.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Node("User")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {
    @Id
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String phone;
    @NotNull
    private UserType userType;

    @JsonIgnore
    @Relationship(type = "HAS_URGENT_CONTACT", direction = Relationship.Direction.OUTGOING)
    private Set<UserModel> urgentContacts;

    @Override
    public String toString() {
        return "UserModel{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", userType=" + userType +
                '}';
    }
}
