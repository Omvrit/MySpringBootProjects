package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class JournalEntryService {
    @Autowired
    JournalEntryRepository journalEntryRepository;

    public ResponseEntity<?> getAllJournalEntries() {
                try {
                    List<JournalEntry> journalEntries = journalEntryRepository.findAll();
                    if (journalEntries.isEmpty()) {
                        return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
                    }
                    return new ResponseEntity<>(journalEntries, HttpStatus.OK);
                }
                catch (Exception e) {
                    System.out.println("Error: " + e);
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<JournalEntry> postJournalEntries(JournalEntry journalEntry) {
        try {

            return new ResponseEntity<>(journalEntryRepository.save(journalEntry),HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity<JournalEntry> getJournalEntryById(String id) {
        try {
            JournalEntry journalEntry = journalEntryRepository.findById(id).get();
            if (journalEntry == null) {
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity<JournalEntry>updateJournalEntries(JournalEntry journalEntry) {
        try {
            JournalEntry journalEntryPrevious = getJournalEntryById(journalEntry.getId()).getBody();

            if (journalEntry.getContent() != null && !journalEntry.getContent().isEmpty()) {
                journalEntryPrevious.setContent(journalEntry.getContent());
            }
            if (journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty()) {
                journalEntryPrevious.setTitle(journalEntry.getTitle());
            }
            journalEntryPrevious.setDate(LocalDateTime.now());
            return new ResponseEntity<>(journalEntryRepository.save(journalEntryPrevious), HttpStatus.CREATED);//returning th
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    public ResponseEntity<Boolean> deleteJournalEntries(String id) {
        try {
            journalEntryRepository.deleteById(id);
            return new ResponseEntity<>(true,HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
