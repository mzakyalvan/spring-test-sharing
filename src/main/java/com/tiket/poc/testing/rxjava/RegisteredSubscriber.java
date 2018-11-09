package com.tiket.poc.testing.rxjava;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author zakyalvan
 */
@Getter
@SuppressWarnings("serial")
public class RegisteredSubscriber implements Serializable {
    private String emailAddress;
    private String fullName;
    private ZonedDateTime subscribedTime;

    @Builder
    protected RegisteredSubscriber(String emailAddress, String fullName, ZonedDateTime subscribedTime) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.subscribedTime = subscribedTime;
    }
}
