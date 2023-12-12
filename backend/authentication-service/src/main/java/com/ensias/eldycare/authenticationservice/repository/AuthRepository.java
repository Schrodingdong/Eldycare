package com.ensias.eldycare.authenticationservice.repository;

import com.ensias.eldycare.authenticationservice.model.AuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthModel,Long> {
    public boolean existsByEmail(String email);
    public AuthModel findByEmail(String email);
}
