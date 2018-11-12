package com.tiket.poc.testing.retrofit;

import com.tiket.tix.common.spring.retrofit.annotation.RetrofitServiceScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zakyalvan
 */
@ComponentScan
@RetrofitServiceScan
@SpringBootConfiguration
// Excluded, because mongodb components take long time to start.
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        EmbeddedMongoAutoConfiguration.class
})
public class RetrofitTestConfiguration {
}
