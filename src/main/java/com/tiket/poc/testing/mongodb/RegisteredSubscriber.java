package com.tiket.poc.testing.mongodb;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zakyalvan
 */
@Data
@Document(collection = "registered-subscriber")
public class RegisteredSubscriber implements Serializable {
    @Id
    private String id;

    @Indexed(unique = true)
    private String emailAddress;

    private String fullName;

    private LocalDateTime subscribedTime;

    @Builder
    public RegisteredSubscriber(String id, String emailAddress, String fullName, LocalDateTime subscribedTime) {
        this.id = id;
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.subscribedTime = subscribedTime;
    }
}
