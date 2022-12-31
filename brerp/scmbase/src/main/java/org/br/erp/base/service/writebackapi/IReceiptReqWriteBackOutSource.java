package org.br.erp.base.service.writebackapi;

import org.br.erp.base.Config.EventMessage.ReceiptWriteBackWwMessage;

public interface IReceiptReqWriteBackOutSource {

    public void ReqWriteBackOutSource(ReceiptWriteBackWwMessage receiptWriteBackWwMessage);
}
