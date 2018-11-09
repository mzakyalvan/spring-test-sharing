package com.tiket.poc.testing.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>Performing test using mock servlet infrastructure (without running servlet container, e.g. Tomcat, Undertow etc).</p>
 *
 * <p>For more details on testing async controller, please refer following links</p>
 * <ul>
 * <li>https://github.com/spring-projects/spring-framework/blob/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples/standalone/AsyncTests.java</li>
 * <li>https://github.com/spring-projects/spring-mvc-showcase/blob/master/src/test/java/org/springframework/samples/mvc/async/DeferredResultControllerTests.java</li>
 * </ul>
 *
 * @author zakyalvan
 */
@WebMvcTest
@RunWith(SpringRunner.class)
public class SubscriptionControllerMockMvcTests {
    @Autowired
    private MockMvc mockMvc;

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

        MvcResult mvcResult = mockMvc.perform(post("/subscriptions").content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.emailAddress").value("zaky.alvan@tiket.com"))
                .andExpect(jsonPath("$.subscribedTime", notNullValue()))
                .andExpect(jsonPath("$.notificationSent", is(true)));
    }

    @Test
    public void whenSubscribeUsingInvalidEmail_thenMustRejectRequest() throws Exception {
        SubscriptionRequest request = SubscriptionRequest.builder()
                .emailAddress("zaky.alvan[at]tiket.com")
                .fullName("Muhammad Zaky Alvan")
                .promoInterests(Arrays.asList("flight", "hotel", "car"))
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/subscriptions").content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(DataBindingException.class)))
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenSubscribeUsingSubscribedEmail_thenMustThrowException() throws Exception {
        SubscriptionRequest request = SubscriptionRequest.builder()
                .emailAddress("registered.email@example.com")
                .fullName("Registered Subscriber")
                .promoInterests(Arrays.asList("flight", "hotel", "car"))
                .build();

        mockMvc.perform(post("/subscriptions").content(objectMapper.writeValueAsBytes(request))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(IllegalArgumentException.class)))
                .andReturn();
    }
}
