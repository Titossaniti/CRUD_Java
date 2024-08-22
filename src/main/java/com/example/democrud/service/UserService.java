package com.example.democrud.service;

import com.example.democrud.model.User;
import com.example.democrud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    // Méthodes CRUD
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setName(userDetails.getName());
        user.setAge(userDetails.getAge());
        user.setRole(userDetails.getRole()); // Met à jour le rôle également
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public User updateUserPartially(Long id, Map<String, Object> updates) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur avec l'id " + id + " n'a pas été trouvé"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                case "age":
                    user.setAge(Integer.parseInt((String) value));
                    break;
                // Ajoutez ici d'autres champs à mettre à jour si nécessaire
                default:
                    throw new IllegalArgumentException("Le champs " + key + " n'est pas modifiable.");
            }
        });

        return userRepo.save(user);
    }

    // Implémentation de UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

}
