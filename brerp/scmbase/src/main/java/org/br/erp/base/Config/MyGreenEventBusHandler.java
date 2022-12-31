package org.br.erp.base.Config;

import lombok.extern.slf4j.Slf4j;
import org.br.erp.base.Config.EventMessage.ReceiptWriteBackWwMessage;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.greenrobot.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Slf4j
@Service
public class MyGreenEventBusHandler {


    @Autowired
    private EventListener eventListener;


    private EventBus eventBus=new EventBus();


    @PostConstruct
    public void init() {
        eventBus.register(eventListener);
    }

    @PreDestroy
    public void destory() {
        eventBus.unregister(eventListener);
    }


    /**
     * 收获申请回写委外订单
     *
     * @param receiptWriteBackWwMessage
     */
    public void receiptReqWriteBackOutSourceQty(ReceiptWriteBackWwMessage receiptWriteBackWwMessage) {
        eventBus.post(receiptWriteBackWwMessage);
    }

    public void onMessageEvent(List<ScmOutSourceToOutStockModel> event){
        eventBus.post(event);
    }
}
