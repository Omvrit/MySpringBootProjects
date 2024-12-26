package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AdminService  {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    public ResponseEntity<?>CreateAdminUser(UserEntity userEntity) {
        try{
            userEntity.setRoles(Arrays.asList("USER", "ADMIN"));
            String password = userEntity.getPassword();
            userEntity.setPassword(passwordEncoder.encode(password));
            return new ResponseEntity<>(userRepository.save(userEntity), HttpStatus.CREATED);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
