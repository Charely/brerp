package org.br.erp.base.Config.EventMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptWriteBackWwMessage {
    private String outsourceitemid;
    private String receiptreqqty;
    private boolean isdelete;
}
