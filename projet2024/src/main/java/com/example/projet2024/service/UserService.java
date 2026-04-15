package com.example.projet2024.service;

import com.example.projet2024.DTO.UserDTO;
import com.example.projet2024.DTO.UserUpdateRequest;
import com.example.projet2024.DTO.PasswordChangeRequest;
import com.example.projet2024.Enum.Role_Enum;
import com.example.projet2024.Security.Jwt.JwtUtils;
import com.example.projet2024.entite.User;
import com.example.projet2024.mapper.UserMapper;
import com.example.projet2024.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class UserService implements IUserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private UserMapper userMapper;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        try {
            System.out.println("========== USER SERVICE: getAllUsers() START ==========");
            List<User> users = userRepository.findAllActive();
            System.out.println("✅ Requête SQL exécutée");
            System.out.println("✅ Nombre d'utilisateurs trouvés: " + users.size());
            
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                System.out.println("  User " + (i+1) + ": " + u.getEmail() + " | Role: " + (u.getRole() != null ? u.getRole().toString() : "NULL"));
            }
            
            System.out.println("========== USER SERVICE: getAllUsers() END ==========");
            return users;
        } catch (Exception e) {
            System.out.println("❌ ERREUR dans getAllUsers(): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserProfileById(Long id, UserUpdateRequest updateRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    // Update basic profile fields
                    user.setFirstname(updateRequest.getFirstname());
                    user.setLastname(updateRequest.getLastname());
                    user.setPhoneNumber(updateRequest.getPhoneNumber());
                    user.setSex(updateRequest.getSex());
                    user.setDateOfBirth(updateRequest.getDateOfBirth());
                    user.setVerified(true);

                    // Handle password update if provided
                    if (updateRequest.getNewPassword() != null && !updateRequest.getNewPassword().isEmpty()) {
                        String encodedPassword = passwordEncoder.encode(updateRequest.getNewPassword());
                        user.setPassword(encodedPassword);
                    }

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    // ✅ Update only profile fields, NEVER modify password
                    if (updatedUser.getFirstname() != null) {
                        user.setFirstname(updatedUser.getFirstname());
                    }
                    if (updatedUser.getLastname() != null) {
                        user.setLastname(updatedUser.getLastname());
                    }
                    if (updatedUser.getEmail() != null) {
                        user.setEmail(updatedUser.getEmail());
                    }
                    if (updatedUser.getSex() != null) {
                        user.setSex(updatedUser.getSex());
                    }
                    if (updatedUser.getPhoneNumber() != null) {
                        user.setPhoneNumber(updatedUser.getPhoneNumber());
                    }
                    if (updatedUser.getDateOfBirth() != null) {
                        user.setDateOfBirth(updatedUser.getDateOfBirth());
                    }
                    
                    // ✅ Allow role update ONLY if current user is SUPER_ADMIN
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    boolean isSuperAdmin = authentication != null && authentication.getAuthorities().stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_SUPER_ADMIN"));
                    
                    if (isSuperAdmin && updatedUser.getRole() != null) {
                        user.setRole(updatedUser.getRole());
                        System.out.println("✅ Role updated by SUPER_ADMIN: " + updatedUser.getRole());
                    } else if (updatedUser.getRole() != null) {
                        System.out.println("⚠️  Role update denied: user is not SUPER_ADMIN");
                    }
                    
                    // ❌ NEVER update password from updateUser() endpoint
                    // Password is managed through dedicated /change-password endpoint only
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        try {
            System.out.println("🗑️  Hard deleting user with ID: " + id);
            
            // Step 1: Remove all FK references from intervention_preventive_assigned_users using native query
            System.out.println("Step 1: Cleaning foreign key references...");
            int deletedAssignments = entityManager.createNativeQuery(
                "DELETE FROM intervention_preventive_assigned_users WHERE user_id = ?1"
            ).setParameter(1, id).executeUpdate();
            System.out.println("✅ Deleted " + deletedAssignments + " intervention assignments");
            
            // Flush to ensure FK cleanup is committed
            entityManager.flush();
            
            // Step 2: Hard delete user from database
            System.out.println("Step 2: Deleting user from database...");
            User userToDelete = userRepository.findById(id).orElse(null);
            if (userToDelete != null) {
                userRepository.delete(userToDelete);
                System.out.println("✅ User hard deleted successfully");
            } else {
                System.out.println("⚠️  User not found");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error in deleteUser: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    public User getUserById(Long id) {
        // ✅ Get only active users (not deleted)
        return userRepository.findByIdActive(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User assignUserRole(Long id, Role_Enum newRole) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé.");
        }

        User user = optionalUser.get();
        user.setRole(newRole);
        return userRepository.save(user);
    }

    public boolean verifyUser(String token) {
        Optional<User> optionalUser = userRepository.findByVerificationToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerified(true);
            user.setVerificationToken(null); // Clear the verification token after verification
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<UserDTO> getAllUsersDTO() {
        // ✅ Get only active users (not deleted)
        List<User> usersList = userRepository.findAllActive();
        return userMapper.UserListToUserDTOList(usersList);
    }

    @Override
    public User findByEmail(String email) {
        // ✅ Get only active users (not deleted)
        return userRepository.findByEmailActive(email)
                .orElse(null);
    }

    @Override
    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    @Override
    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setVerified(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setVerified(false);
        userRepository.save(user);
    }

    @Override
    public User updateProfilePicture(Long id, String profilePicture) {
        User user = getUserById(id);
        user.setProfilePicture(profilePicture);
        return userRepository.save(user);
    }

    @Override
    public boolean changePassword(Long userId, PasswordChangeRequest request) {
        User user = getUserById(userId);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

}
