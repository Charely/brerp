package org.br.erp.base.service.writebackapi;

import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;

import java.util.List;

public interface ICreateOutStockFromOutSource {
    public boolean createOutStock(List<ScmOutSourceToOutStockModel> scmOutSourceToOutStockModels);
}
