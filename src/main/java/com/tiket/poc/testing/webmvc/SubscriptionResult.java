package com.tiket.poc.testing.webmvc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zakyalvan
 */
@Getter
@ToString
@JsonDeserialize(builder = SubscriptionResult.SubscriptionResultBuilder.class)
@SuppressWarnings("serial")
public class SubscriptionResult implements Serializable {
    private String emailAddress;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime subscribedTime;
    private boolean notificationSent;

    @Builder
    protected SubscriptionResult(String emailAddress, LocalDateTime subscribedTime, boolean notificationSent) {
        this.emailAddress = emailAddress;
        this.subscribedTime = subscribedTime;
        this.notificationSent = notificationSent;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class SubscriptionResultBuilder {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        public SubscriptionResultBuilder subscribedTime(LocalDateTime subscribedTime) {
            this.subscribedTime = subscribedTime;
            return this;
        }
    }
}
