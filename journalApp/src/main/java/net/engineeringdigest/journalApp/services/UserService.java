package net.engineeringdigest.journalApp.services;


import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Component
@Slf4j
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JournalEntryService journalEntryService;

    @Autowired
    PasswordEncoder passwordEncoder;
    public ResponseEntity<UserEntity> postJournalEntriesForUser(UserEntity userEntity) {//this should be moved to JournalEntryService
        try {

            return new ResponseEntity<>(userRepository.save(userEntity),HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<UserEntity> createUser(UserEntity user) throws Exception {

        try {
            UserEntity findUser = findUserByUsername(user.getUsername()).getBody();
            if(findUser == null){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRoles(Arrays.asList("USER"));
                user.setCreatedDate(LocalDateTime.now());
                userRepository.save(user);
                return new ResponseEntity<>(user, HttpStatus.CREATED);

            }
            else{
                log.error("User already exists");
                return new ResponseEntity<>(null,HttpStatus.CONFLICT);
            }


        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public ResponseEntity<List<UserEntity>> getAllUsers() {
        try {
            List<UserEntity> users = userRepository.findAll();
            if (users.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<UserEntity> getUserById(String id) {
        try {
            UserEntity user = userRepository.findById(id).get();
            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<UserEntity> updateUser(UserEntity previousUser, UserEntity newUser) {
        try {
            if (newUser.getUsername() != null && !newUser.getUsername().isEmpty()) {
                previousUser.setUsername(newUser.getUsername());
            }
            if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
                previousUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            }

            return new ResponseEntity<>(userRepository.save(previousUser), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<Boolean> deleteUser(String username) {
        try {
            UserEntity user = userRepository.findByUsername(username);
            userRepository.deleteByUsername(user.getUsername());
            //Below code is to delete all journal entries of the user
            List<JournalEntry> journalEntries = user.getJournalEntries();
            for (JournalEntry journalEntry : journalEntries) {
                journalEntryService.deleteJournalEntries(journalEntry.getId());
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<UserEntity> findUserByUsername(String username) {
        try{
            UserEntity user = userRepository.findByUsername(username);
            if(user == null){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(user,HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }
    }
}
