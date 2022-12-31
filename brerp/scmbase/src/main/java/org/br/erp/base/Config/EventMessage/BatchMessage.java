package org.br.erp.base.Config.EventMessage;

import lombok.Data;

@Data
public class BatchMessage {
    String companyid;
    String materialid;
    String wareshouseid;
}
