package com.tiket.poc.testing.kafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zakyalvan
 */
@Getter
@JsonDeserialize(builder = SubscriptionData.SubscriptionDataBuilder.class)
@SuppressWarnings("serial")
public class SubscriptionData implements Serializable {
    @Email
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String fullName;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime subscribeTime;

    @Builder
    protected SubscriptionData(String emailAddress, String fullName, LocalDateTime subscribeTime) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.subscribeTime = subscribeTime;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class SubscriptionDataBuilder {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        public SubscriptionDataBuilder subscribeTime(LocalDateTime subscribeTime) {
            this.subscribeTime = subscribeTime;
            return this;
        }
    }
}
