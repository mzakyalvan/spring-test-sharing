package com.tiket.poc.testing.webmvc;

import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zakyalvan
 */
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriberRegistrar subscriberRegistrar;

    @PostMapping
    @ResponseBody DeferredResult<SubscriptionResult> subscribe(@Validated @RequestBody SubscriptionRequest request, BindingResult bindings) {
        LOGGER.info("Handle subscription for : {}", request);

        DeferredResult<SubscriptionResult> deferredResult = new DeferredResult<>();

        Single.just(bindings)
                .filter(bindingResult -> !bindingResult.hasErrors())
                .switchIfEmpty(Single.error(new DataBindingException(bindings)))
                .flatMap(bindingResult -> subscriberRegistrar.register(request))
                .subscribe(deferredResult::setResult, deferredResult::setErrorResult);

        return deferredResult;
    }
}
