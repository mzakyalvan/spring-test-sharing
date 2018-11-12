package com.tiket.poc.testing.retrofit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.vavr.control.Try;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

/**
 * For details on using OkHttp's mock web server, please refer https://github.com/square/okhttp/tree/master/mockwebserver.
 *
 * @author zakyalvan
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "logging.level.com.tiket=debug",
                "tiket.retrofit.default-url=${retrofit.default.url}",
                "tiket.retrofit.connection.debug-request=true"
        })
@RunWith(SpringRunner.class)
public class SubscriptionServiceTests {
    private static final RetrofitSubscriptionService.SubscribeParameters BAD_PARAMETERS = RetrofitSubscriptionService.SubscribeParameters.builder()
            .build();

    @ClassRule
    public static MockWebServer webServer = new MockWebServer();

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("retrofit.default.url", webServer.url("/").toString());
    }

    @Test
    public void whenSubscribingWithValidParameter_thenMustSuccess() throws Exception {
        /**
         * Setup expectations, see https://github.com/square/okhttp/tree/master/mockwebserver
         */
        webServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                return Try
                        .of(() -> {
                            RetrofitSubscriptionService.SubscribeParameters subscribeParameters = objectMapper
                                    .readValue(request.getBody().readByteArray(), RetrofitSubscriptionService.SubscribeParameters.class);

                            if (request.getPath().equals("/subscribe/")) {
                                return new MockResponse().setResponseCode(200).setBody(objectMapper.writeValueAsString(SubscriptionResult.builder()
                                        .emailAddress(subscribeParameters.getEmailAddress())
                                        .subscribeSuccess(true).notificationSent(true)
                                        .subscribeTime(LocalDateTime.now())
                                        .build()));
                            }
                            else {
                                return new MockResponse().setResponseCode(404)
                                        .setBody("Not Found");
                            }
                        })
                        .recover(error -> Match(error).of(
                                Case($(instanceOf(JsonMappingException.class)), e -> new MockResponse().setResponseCode(400).setBody("Bad Request")),
                                Case($(instanceOf(JsonProcessingException.class)), e -> new MockResponse().setResponseCode(500).setBody("Internal Server Error"))
                        ))
                        .get();
            }
        });

        TestObserver<SubscriptionResult> testObserver = subscriptionService
                .register("zaky.alvan@tiket.com", "Muhammad Zaky Alvan", "flight", "hotel")
                .subscribeOn(Schedulers.io())
                .test();

        testObserver.assertSubscribed()
                .await()
                .assertValue(result -> result instanceof SubscriptionResult && result.getEmailAddress().equals("zaky.alvan@tiket.com"))
                .assertComplete();
    }
}
