package org.br.erp.base.Config;


import lombok.extern.slf4j.Slf4j;
import org.br.erp.base.Config.EventMessage.ReceiptWriteBackWwMessage;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.br.erp.base.service.writebackapi.ICreateOutStockFromOutSource;
import org.br.erp.base.service.writebackapi.IReceiptReqWriteBackOutSource;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberExceptionEvent;
import org.greenrobot.eventbus.ThreadMode;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EventListener {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ICreateOutStockFromOutSource createOutStockFromOutSource;

    @Autowired
    IReceiptReqWriteBackOutSource receiptReqWriteBackOutSource;

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(List<ScmOutSourceToOutStockModel> event){
        createOutStockFromOutSource.createOutStock(event);
    }

    @Subscribe(threadMode=ThreadMode.POSTING)
    public void onReceiptReqWriteBackOutSourceEvent(ReceiptWriteBackWwMessage receiptWriteBackWwMessage){
        receiptReqWriteBackOutSource.ReqWriteBackOutSource(receiptWriteBackWwMessage);
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onException(SubscriberExceptionEvent subscriberExceptionEvent){
        //throw new RuntimeException(subscriberExceptionEvent.throwable.getMessage());
        if(subscriberExceptionEvent.causingEvent.getClass().equals(ReceiptWriteBackWwMessage.class)){
            //回写委外订单错误
            ReceiptWriteBackWwMessage causingEvent = (ReceiptWriteBackWwMessage)subscriberExceptionEvent.causingEvent;
            String key = causingEvent.getOutsourceitemid()+","+ causingEvent.getReceiptreqqty();
            redisUtil.set(key,subscriberExceptionEvent.throwable.getMessage());
        }
    }
}
