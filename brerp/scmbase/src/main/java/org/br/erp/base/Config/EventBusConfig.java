package org.br.erp.base.Config;


import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class EventBusConfig {

    @Bean
    public AsyncEventBus asyncEventBus(){
        return new AsyncEventBus(Executors.newFixedThreadPool(5),new EventExceptionHandler());
    }

    @Bean(value = {"myeventbus"})
    public EventBus eventBus(){

        return new com.google.common.eventbus.EventBus(new EventExceptionHandler());
    }

}
