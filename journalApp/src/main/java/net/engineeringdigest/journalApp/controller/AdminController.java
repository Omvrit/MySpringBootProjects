package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.services.AdminService;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    UserService userService;
    @Autowired
    AdminService adminService;

    @GetMapping("/getAllEntries")
    public ResponseEntity<?> getAllEntries(){
        return journalEntryService.getAllJournalEntries();
    }
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping("/createAdminUser")
    public ResponseEntity<?> createAdminUser(@RequestBody UserEntity userEntity){
        return adminService.CreateAdminUser(userEntity);
    }


}
