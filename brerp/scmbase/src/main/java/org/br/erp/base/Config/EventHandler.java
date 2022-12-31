package org.br.erp.base.Config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.br.erp.base.Config.EventMessage.BatchMessage;
import org.br.erp.base.Config.EventMessage.ReceiptWriteBackWwMessage;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Slf4j
@Service
public class EventHandler {

    @Autowired
    public  AsyncEventBus asyncEventBus;

    @Autowired
    @Qualifier(value = "myeventbus")
    private EventBus eventBus;

    @Autowired
    private EventListener eventListener;

    @PostConstruct
    public void init(){
        eventBus.register(eventListener);
    }

    @PreDestroy
    public void destory(){
        eventBus.unregister(eventListener);
    }

    public void eventPost(List<ScmOutSourceToOutStockModel> eventmessage){
        eventBus.post(eventmessage);
        log.info("event bus {}"+eventmessage);
    }


    public void getBatchListEvent(BatchMessage batchMessage){
        eventBus.post(batchMessage);
    }

    /**
     * 收获申请回写委外订单
     * @param receiptWriteBackWwMessage
     */
    public void receiptReqWriteBackOutSourceQty(ReceiptWriteBackWwMessage receiptWriteBackWwMessage){
        eventBus.post(receiptWriteBackWwMessage);
    }
}
