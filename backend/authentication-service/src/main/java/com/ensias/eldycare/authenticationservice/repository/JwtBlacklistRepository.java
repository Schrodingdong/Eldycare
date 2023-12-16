package com.ensias.eldycare.authenticationservice.repository;

import com.ensias.eldycare.authenticationservice.model.JwtBlacklistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklistModel, Long> {
    boolean existsByJwt(String jwt);
}