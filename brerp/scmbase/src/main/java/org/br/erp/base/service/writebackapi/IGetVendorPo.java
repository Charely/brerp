package org.br.erp.base.service.writebackapi;

import org.br.erp.entity.po.ScmVendorEntity;

public interface IGetVendorPo {
    public ScmVendorEntity getVendorList(String billitemid);
}
