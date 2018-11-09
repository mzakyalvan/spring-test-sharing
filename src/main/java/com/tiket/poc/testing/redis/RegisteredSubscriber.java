package com.tiket.poc.testing.redis;

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
@JsonDeserialize(builder = RegisteredSubscriber.Builder.class)
@SuppressWarnings("serial")
public class RegisteredSubscriber implements Serializable {
    @Email
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String fullName;

    @lombok.Builder(builderClassName = "Builder")
    protected RegisteredSubscriber(String emailAddress, String fullName) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

    }
}