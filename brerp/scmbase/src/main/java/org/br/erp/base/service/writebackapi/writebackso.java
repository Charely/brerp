package org.br.erp.base.service.writebackapi;

public interface writebackso {

     void updatesoitemoutqty(String soitemid,String outqty);


     void updatesoiteminvoiceqty(String soitemid,String invoiceqty,Boolean isdelete);
}
