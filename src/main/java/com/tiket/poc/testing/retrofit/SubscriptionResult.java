package com.tiket.poc.testing.retrofit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zakyalvan
 */
@Getter
@JsonDeserialize(builder = SubscriptionResult.SubscriptionResultBuilder.class)
@SuppressWarnings("serial")
public class SubscriptionResult implements Serializable {
    private String emailAddress;

    private boolean subscribeSuccess;

    private boolean notificationSent;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime subscribeTime;

    @Builder
    protected SubscriptionResult(String emailAddress, boolean subscribeSuccess, boolean notificationSent, LocalDateTime subscribeTime) {
        this.emailAddress = emailAddress;
        this.subscribeSuccess = subscribeSuccess;
        this.notificationSent = notificationSent;
        this.subscribeTime = subscribeTime;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class SubscriptionResultBuilder {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        public SubscriptionResultBuilder subscribeTime(LocalDateTime subscribeTime) {
            this.subscribeTime = subscribeTime;
            return this;
        }
    }
}
