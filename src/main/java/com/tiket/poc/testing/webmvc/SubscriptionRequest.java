package com.tiket.poc.testing.webmvc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * @author zakyalvan
 */
@Data
@ToString
@JsonDeserialize(builder = SubscriptionRequest.SubscriptionRequestBuilder.class)
@SuppressWarnings("serial")
public class SubscriptionRequest implements Serializable {
    @Email
    @NotBlank
    private String emailAddress;

    @NotBlank
    private String fullName;

    private List<String> promoInterests;

    @Builder
    protected SubscriptionRequest(String emailAddress, String fullName, List<String> promoInterests) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.promoInterests = promoInterests;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class SubscriptionRequestBuilder {

    }
}
