//package net.engineeringdigest.journalApp.controller;
//
//import net.engineeringdigest.journalApp.entity.JournalEntry;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/_______journal")
//public class JournalEntryController {
//    Map<String, JournalEntry> journalEntries = new HashMap<>();
//
//    @GetMapping
//    public List<JournalEntry> getJournalEntries() {
//        return new ArrayList<>(journalEntries.values());
//    }
//
//
//    @PostMapping
//    public boolean postJournalEntries(@RequestBody JournalEntry journalEntry) {
//        try {
//            journalEntries.put(journalEntry.getId(), journalEntry);
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//            return false;
//        }
//        return true;
//
//    }
//    @GetMapping("/getById/{id}")
//    public JournalEntry getJournalEntryById(@PathVariable String id){
//        try {
//            return journalEntries.get(id);
//        }catch (Exception e){
//            return null;
//        }
//    }
//    @PutMapping
//    public boolean putJournalEntries(@RequestParam String id,@RequestBody JournalEntry journalEntry) {
//        try {
//            journalEntries.replace(id,journalEntry);
//        }
//        catch(Exception e){
//            System.out.println(e);
//            return false;
//        }
//        return true;
//    }
//    @DeleteMapping
//    public boolean deleteJournalEntries(@RequestParam String id) {
//        try {
//            journalEntries.remove(id);
//        }
//        catch(Exception e){
//            System.out.println(e);
//            return false;
//        }
//        return true;
//    }
//
//}
