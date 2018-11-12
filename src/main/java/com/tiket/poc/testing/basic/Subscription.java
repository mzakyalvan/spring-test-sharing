package com.tiket.poc.testing.basic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author zakyalvan
 */
@Getter
@JsonDeserialize(builder = Subscription.SubscriptionBuilder.class)
@SuppressWarnings("serial")
public class Subscription implements Serializable {
    @Email
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String fullName;

    @Builder
    protected Subscription(String emailAddress, String fullName) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class SubscriptionBuilder {

    }
}
