package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    UserService userService;
    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "OK";
    }
    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> creatUser(@NotNull @RequestBody UserEntity user) throws Exception {
        if(user.getPassword()==null || user.getUsername()==null){
            return ResponseEntity.badRequest().body(null);
        }
        return userService.createUser(user);
    }
}
