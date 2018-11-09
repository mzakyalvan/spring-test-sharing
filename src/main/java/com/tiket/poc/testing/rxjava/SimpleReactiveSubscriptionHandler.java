package com.tiket.poc.testing.rxjava;

import com.tiket.poc.testing.basic.Subscription;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zakyalvan
 */
@Component
public class SimpleReactiveSubscriptionHandler implements ReactiveSubscriptionHandler {
    @Autowired
    private SubscriberRepository subscribers;

    @Override
    public Single<RegisteredSubscriber> handle(Subscription subscription) {
        return Single.just(subscription)
                .flatMap(s -> subscribers.register(s)
                        .retryWhen(errors -> errors
                                    .zipWith(Flowable.range(1, 3), ((throwable, count) -> count))
                                    .flatMap(retryCount -> Flowable.timer(retryCount * 100, TimeUnit.MILLISECONDS, Schedulers.computation()))
                        )
                        .subscribeOn(Schedulers.computation())
                );
    }
}
