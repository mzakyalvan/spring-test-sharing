package com.tiket.poc.testing.rxjava;

import com.tiket.poc.testing.basic.Subscription;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

/**
 * @author zakyalvan
 */
@SpringBootTest(classes = RxTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class ReactiveSubscriptionHandlerTests {

    @Autowired
    private ReactiveSubscriptionHandler subscriptionHandler;

    @MockBean
    private SubscriberRepository subscriberRepository;

    @Test
    public void testSubscription() throws Exception {
        ZonedDateTime subscribedTime = ZonedDateTime.now(ZoneId.of("UTC"));

        RegisteredSubscriber registeredSubscriber = RegisteredSubscriber.builder()
                .emailAddress("zaky.alvan@tiket.com")
                .fullName("Muhammad Zaky Alvan")
                .subscribedTime(subscribedTime)
                .build();

        when(subscriberRepository.register(any(Subscription.class)))
                .thenReturn(Single.just(registeredSubscriber));

        TestScheduler testScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> testScheduler);

        TestObserver<RegisteredSubscriber> testObserver = subscriptionHandler.handle(Subscription.builder()
                    .emailAddress("zaky.alvan@tiket.com")
                    .fullName("Muhammad Zaky Alvan")
                    .build()
                )
                .test();

        testScheduler.triggerActions();

        testObserver.assertSubscribed()
                .await()
                .assertValue(subscriber -> subscriber.getEmailAddress().equals("zaky.alvan@tiket.com")
                        && subscriber.getFullName().equals("Muhammad Zaky Alvan")
                        && subscriber.getSubscribedTime().equals(subscribedTime)
                )
                .assertComplete();

        verify(subscriberRepository, times(1)).register(any(Subscription.class));
    }

    @Before
    public void tearDown() {
        verifyNoMoreInteractions(subscriberRepository);
    }
}
