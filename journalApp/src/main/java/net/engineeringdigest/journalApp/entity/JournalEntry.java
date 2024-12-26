package net.engineeringdigest.journalApp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import lombok.*;
@Document(collection = "journaldb")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JournalEntry {
    @Id
    private String id;
    private String title;
    private String content;
    private LocalDateTime date;
}
