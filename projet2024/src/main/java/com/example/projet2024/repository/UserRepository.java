package com.example.projet2024.repository;

import com.example.projet2024.entite.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByVerificationToken(String token);
    
    // ✅ Soft delete queries - exclude deleted users
    @Query("SELECT u FROM User u WHERE u.isDeleted = false")
    List<User> findAllActive();
    
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isDeleted = false")
    Optional<User> findByIdActive(@Param("id") Long id);
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isDeleted = false")
    Optional<User> findByEmailActive(@Param("email") String email);

}
