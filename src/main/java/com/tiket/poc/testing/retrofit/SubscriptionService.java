package com.tiket.poc.testing.retrofit;

import io.reactivex.Single;

/**
 * @author zakyalvan
 */
public interface SubscriptionService {
    Single<SubscriptionResult> register(String emailAddress, String fullName, String... interestTopics);
}
