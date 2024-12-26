package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getJournalEntriesOfUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userService.findUserByUsername(username).getBody();
        if (user == null) {//If user is not found we return a 404 status code
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        //This all below is for else
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (journalEntries.isEmpty()) {//If user has no journal entries we return a 204 status code
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(journalEntries, HttpStatus.FOUND);
    }


    @PostMapping()
    @Transactional
    public ResponseEntity<Boolean> postJournalEntryOfUser(@RequestBody JournalEntry journalEntry) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            journalEntry.setDate(LocalDateTime.now());
            UserEntity user = userService.findUserByUsername(username).getBody();
            System.out.println(user);
            if (user == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }

            user.getJournalEntries().add(journalEntryService.postJournalEntries(journalEntry).getBody());//This saves general entry on JournalEntry table plus adds it to User account on cache not the actual database
            userService.postJournalEntriesForUser(user);//This saves general entry on User account
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> ids = userService.findUserByUsername(username).getBody().getJournalEntries().stream().map(JournalEntry::getId).toList();
        for(String e: ids){
            if(e.equals(id)){
                return journalEntryService.getJournalEntryById(id);
            }
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<JournalEntry> updateJournalEntriesOfUser(@RequestBody JournalEntry journalEntry,@PathVariable String id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UserEntity user = userService.findUserByUsername(username).getBody();
            if (user == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            JournalEntry journalEntryPrevious = journalEntryService.getJournalEntryById(id).getBody();//There is a bug here, if the journal entry is not found it will return a null pointer exception
            if (journalEntryPrevious == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            journalEntry.setId(id);
            JournalEntry journalEntryAfterUpdate = journalEntryService.updateJournalEntries(journalEntry).getBody();
            for (int i = 0; i < user.getJournalEntries().size(); i++) {
                if (user.getJournalEntries().get(i).getId().equals(id)) {
                    user.getJournalEntries().set(i, journalEntryAfterUpdate);
                    break;
                }
            }
            userService.postJournalEntriesForUser(user);
            return new ResponseEntity<>(journalEntryAfterUpdate, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            throw new Exception(e);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteJournalEntryOfUser(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            UserEntity user = userService.findUserByUsername(username).getBody();
            System.out.println(user);
            if (user == null) {
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
            }
            List<String> ids = user.getJournalEntries().stream().map(JournalEntry::getId).toList();
            for(String e: ids){
                if(e.equals(id)){
                    JournalEntry journalEntry = journalEntryService.getJournalEntryById(id).getBody();
                    if (journalEntry == null) {
                        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
                    }
                    for (int i = 0; i < user.getJournalEntries().size(); i++) {
                        if (user.getJournalEntries().get(i).getId().equals(id)) {
                            user.getJournalEntries().remove(i);
                            break;
                        }
                    }
                    userService.postJournalEntriesForUser(user);
                    return journalEntryService.deleteJournalEntries(id);
                }
            }
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
