package org.br.erp.base.Config;

import com.google.common.base.Throwables;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;

@Slf4j
public class EventExceptionHandler  implements SubscriberExceptionHandler, Thread.UncaughtExceptionHandler {

    @Override
    public void handleException(Throwable throwable, SubscriberExceptionContext subscriberExceptionContext) {
//        Throwables.throwIfInstanceOf(throwable,new RuntimeException().getClass());
        throw  new RuntimeException(throwable.getMessage());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        throw new RuntimeException(e.getMessage());
    }
}
