package com.tiket.poc.testing.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * @author zakyalvan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SubscriptionControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Helper for json serialize/deserialize
     */
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriberRegistrar subscriberRegistrar;

    @Before
    public void setUp() {
        when(subscriberRegistrar.register(Mockito.any(SubscriptionRequest.class)))
                .then(invocation -> {
                    SubscriptionRequest request = (SubscriptionRequest) invocation.getArguments()[0];

                    if(request.getEmailAddress().equalsIgnoreCase("registered.email@example.com")) {
                        return Single.error(new IllegalArgumentException("Email address already registered"));
                    }

                    return Single.just(SubscriptionResult.builder()
                            .emailAddress(request.getEmailAddress())
                            .subscribedTime(LocalDateTime.now())
                            .notificationSent(true)
                            .build());
                });
    }

    @Test
    public void whenSubscribeUsingUnsubscribedEmail_thenMustReturnSuccessResult() throws Exception {
        SubscriptionRequest request = SubscriptionRequest.builder()
                .emailAddress("zaky.alvan@tiket.com")
                .fullName("Muhammad Zaky Alvan")
                .promoInterests(Arrays.asList("flight", "hotel", "car"))
                .build();

        ResponseEntity<SubscriptionResult> response = restTemplate.postForEntity("/subscriptions", request, SubscriptionResult.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getEmailAddress(), equalTo("zaky.alvan@tiket.com"));
        assertThat(response.getBody().getSubscribedTime(), notNullValue());
        assertThat(response.getBody().isNotificationSent(), is(true));
    }
}
