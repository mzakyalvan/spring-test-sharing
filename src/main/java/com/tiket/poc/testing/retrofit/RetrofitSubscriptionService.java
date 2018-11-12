package com.tiket.poc.testing.retrofit;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.tiket.tix.common.spring.retrofit.annotation.RetrofitService;
import io.reactivex.Single;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author zakyalvan
 */
@Service
@Validated
class RetrofitSubscriptionService implements SubscriptionService {
    private final SubscriptionClient subscriptionClient;

    public RetrofitSubscriptionService(SubscriptionClient subscriptionClient) {
        this.subscriptionClient = subscriptionClient;
    }

    @Override
    public Single<SubscriptionResult> register(String emailAddress, String fullName, String... interestTopics) {
        return subscriptionClient.subscribe(SubscribeParameters.builder()
                .emailAddress(emailAddress).fullName(fullName).interestTopics(Arrays.asList(interestTopics))
                .build());
    }

    @RetrofitService
    interface SubscriptionClient {
        @POST("/subscribe/")
        Single<SubscriptionResult> subscribe(@Body SubscribeParameters subscribeParameters);
    }

    @Getter
    @JsonDeserialize(builder = SubscribeParameters.SubscribeParametersBuilder.class)
    @SuppressWarnings("serial")
    static class SubscribeParameters implements Serializable {
        private String emailAddress;
        private String fullName;
        private List<String> interestTopics;

        @Builder
        protected SubscribeParameters(String emailAddress, String fullName, List<String> interestTopics) {
            this.emailAddress = emailAddress;
            this.fullName = fullName;
            this.interestTopics = interestTopics;
        }

        @JsonPOJOBuilder(withPrefix = "")
        static class SubscribeParametersBuilder {

        }
    }
}
