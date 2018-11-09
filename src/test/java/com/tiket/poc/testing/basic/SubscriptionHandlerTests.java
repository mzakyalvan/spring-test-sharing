package com.tiket.poc.testing.basic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.Matchers.*;

/**
 * @author zakyalvan
 */
@SpringBootTest(classes = BasicTestConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RunWith(SpringRunner.class)
public class SubscriptionHandlerTests {
    @Rule
    public ExpectedException expectedErrors = ExpectedException.none();

    @Autowired
    private SubscriptionHandler subscriptionHandler;

    @Test
    public void whenSubscribeWithValidData_thenMustSuccess() {
        subscriptionHandler.handle(Subscription.builder()
                .emailAddress("zaky.alvan@tiket.com")
                .fullName("Muhammad Zaky Alvan")
                .build());
    }

    @Test
    public void whenSubscribeWithoutEmailAddress_thenMustThrowConstrainViolationError() {
        expectedErrors.expect(allOf(
                instanceOf(ConstraintViolationException.class),
                hasProperty("constraintViolations", iterableWithSize(2))
        ));

        subscriptionHandler.handle(Subscription.builder()
                .fullName("Muhammad Zaky Alvan")
                .build());
    }
}
