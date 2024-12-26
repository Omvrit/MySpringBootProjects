package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
//    @GetMapping
//    public ResponseEntity<List<UserEntity>> getUsers(){
//        return userService.getUsers();
//    }
//    @GetMapping("/getById/{id}")
//    public ResponseEntity<UserEntity> getUserById(@PathVariable String id){
//        return userService.getUserById(id);
//    }
    @PutMapping
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity newUser) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity previousUser = userService.findUserByUsername(authentication.getName()).getBody();
        return userService.updateUser(previousUser, newUser);
    }
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUserByUsername(authentication.getName()).getBody();
        if(user==null){
            return ResponseEntity.ok(false);
        }
        return userService.deleteUser(user.getUsername());
    }




}
