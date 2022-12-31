package org.br.erp.base.service.writebackapi;

public interface writebackpo {
    void updatepoinstockqty(String poitemid,String instockqty);
    void updatematreceiptinstockqty(String matreceiptreqitemid,String instockqty);
    void updatepoitemreturnqty(String poitemid,String returnqty);
    void updatepoiteminvoiceqty(String poitemid, String invoiceqty, Boolean isdelete);
}
