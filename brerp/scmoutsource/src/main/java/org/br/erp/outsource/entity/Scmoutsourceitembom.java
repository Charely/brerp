package org.br.erp.outsource.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@TableName("scmoutsourceitembom")
@Data
public class Scmoutsourceitembom {
    @Id
    private String id;
    private String parentid;
    private String materialid;
    private String materialcode;
    private String materialname;

    private String batchid;
    private String batchcode;

    private String warehouseid;

    private String qty;
    /*出库数量*/
    private String outqty;
    /*退货数量*/
    @ColumnDefault("0")
    private String returnqty;
    @ColumnDefault("0")
    private String outflag;
    @ColumnDefault("0")
    private String returnflag;
}
