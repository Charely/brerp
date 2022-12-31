package org.br.erp.inventory.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.br.erp.base.entity.Scmbatch;
import org.br.erp.base.service.IIMperiodService;
import org.br.erp.base.service.IMvperiodService;
import org.br.erp.base.service.IScmbatchService;
import org.br.erp.base.service.writebackapi.IBalanceIntf;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.inventory.base.entity.Scmimbalance;
import org.br.erp.inventory.base.entity.Scminventorytype;
import org.br.erp.inventory.base.mapper.ScmimbalanceMapper;
import org.br.erp.inventory.base.service.IScmimbalanceService;
import org.br.erp.inventory.base.service.IScminventorytypeService;
import org.br.erp.inventory.base.vo.ScmimbalanceVo;
import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.entity.Scminventoy;
import org.br.erp.inventory.entity.Scmtransferbill;
import org.br.erp.inventory.entity.Scmtransferbillitem;
import org.br.erp.inventory.service.IScminventoyService;
import org.br.erp.inventory.service.IScminventoyitemService;
import org.br.erp.inventory.service.IScmtransferbillService;
import org.br.erp.inventory.service.IScmtransferbillitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 库存账本表
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
@Service
public class ScmimbalanceServiceImpl extends ServiceImpl<ScmimbalanceMapper, Scmimbalance>
        implements IScmimbalanceService, IBalanceIntf {

    @Autowired
    IScminventoyService scminventoyService;

    @Autowired
    IScminventorytypeService scminventorytypeService;

    @Autowired
    IScminventoyitemService scminventoyitemService;

    @Autowired
    ScmimbalanceMapper scmimbalanceMapper;

    @Autowired
    IScmtransferbillService scmtransferbillService;

    @Autowired
    IScmtransferbillitemService scmtransferbillitemService;

    @Autowired
    IScmbatchService scmbatchService;

    @Autowired
    IIMperiodService imperiodService;

    /**
     * 出入库记账操作
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public String addToBalance(List<String> ids) {
        for (String id : ids) {
            //todo 获取每个单据的id信息
            Scminventoy scminventoy = scminventoyService.getById(id);

            String inventorytype = scminventoy.getInventorytype();
            //TODO 判断是入库还是出库
            Scminventorytype scminventorytype = scminventorytypeService.getInventoryTypeByTypecode(inventorytype);
            String instock = scminventorytype.getInstock();
            String outstock = scminventorytype.getOutstock();

            if(scminventoy.getStatus() == null || scminventoy.getStatus().equalsIgnoreCase("0")){
                if(instock!=null && instock.equalsIgnoreCase("true")){
                    throw new RuntimeException(scminventoy.getBillcode()+"制单状态，不允许入库，请提交审批");
                }else if(outstock!=null && outstock.equalsIgnoreCase("true")){
                    throw new RuntimeException(scminventoy.getBillcode()+"制单状态，不允许出库，请提交审批");
                }
            }
            if(instock!=null && instock.equalsIgnoreCase("true")){
                //入库操作
                if(scminventoy.getInventoryflag().equalsIgnoreCase("1")){
                    throw new RuntimeException("已经入库，不能进行出入库操作");
                }
                String companyid=scminventoy.getCompanyid();
                String warehouseid = scminventoy.getWarehouseid();
                String vendorid = scminventoy.getVendorid();
                String billdate = scminventoy.getBilldate().substring(0,7).replace("-","");

                Boolean isred=scminventoy.getIsred();
                QueryWrapper<Scminventoryitem> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("parentid",scminventoy.getId());
                List<Scminventoryitem> list = scminventoyitemService.list(queryWrapper);
                if(list!=null && list.size()>0){
                    for (Scminventoryitem scminventoyitem : list) {
                        //获取当期的账本数据
                        toInStockZb(companyid, warehouseid, vendorid, billdate, isred, scminventoyitem,"0");
                    }
                }
            }
            else if(outstock !=null && outstock.equalsIgnoreCase("true")){
                if(scminventoy.getInventoryflag().equalsIgnoreCase("1")){
                    throw new RuntimeException("已经出库，不能再次进行出库操作");
                }
                //出库
                String companyid = scminventoy.getCompanyid();
                String warehouseid = scminventoy.getWarehouseid();
                String vendorid = scminventoy.getVendorid();
                String billdate = scminventoy.getBilldate().substring(0,7).replace("-","");



                QueryWrapper<Scminventoryitem> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("parentid",scminventoy.getId());
                Boolean isred=scminventoy.getIsred();
                List<Scminventoryitem> list = scminventoyitemService.list(queryWrapper);
                if(list!=null && list.size()>0){
                    for (Scminventoryitem scminventoyitem : list) {
                        //获取当期的账本数据
                        toOutStockZb(companyid, warehouseid, vendorid, billdate, isred, scminventoyitem,"0");
                    }
                }
            }

            scminventoy.setInventoryflag("1");
            scminventoyService.updateById(scminventoy);
        }
        return null;
    }

    /**
     * 出库记账操作
     * @param companyid
     * @param warehouseid
     * @param vendorid
     * @param billdate
     * @param isred
     * @param scminventoyitem
     */
    private void toOutStockZb(String companyid, String warehouseid, String vendorid, String billdate, Boolean isred, Scminventoryitem scminventoyitem,String flag) {
        Scmimbalance currentBalance = getCurrentBalance(companyid, warehouseid, vendorid, billdate,
                scminventoyitem.getMaterialid(),
                scminventoyitem.getStocktypeid(),
                scminventoyitem.getInventorykindid(),
                scminventoyitem.getBatchid(),
                scminventoyitem.getStocklocationid());
        //出库的话，需要判断是否账本上有对应的数据，如果没有不允许负库存出库
        if(currentBalance.getAmount().compareTo(new BigDecimal(0)) == 0){
            throw new RuntimeException("本月可用库存不足，不能出库");
        }
        BigDecimal amount = currentBalance.getOutamount();
        BigDecimal newAmount=new BigDecimal(0);
        if(isred.equals(false)) {
            if(flag.equalsIgnoreCase("0")) {
                newAmount = amount.add(new BigDecimal(scminventoyitem.getQty()));
            }else if(flag.equalsIgnoreCase("1")){
                newAmount=amount.subtract(new BigDecimal(scminventoyitem.getQty()));
            }
        }else if(isred.equals(true)){
            if(flag.equalsIgnoreCase("0")) {
                newAmount = amount.add(new BigDecimal("-" + scminventoyitem.getQty()));
            }else if(flag.equalsIgnoreCase("1")){
                newAmount=amount.add(new BigDecimal(scminventoyitem.getQty()));
            }
        }
        currentBalance.setOutamount(newAmount);
        currentBalance.setAmount(currentBalance.getBeginmonthqty().add(currentBalance.getInamount()).subtract(currentBalance.getOutamount()));
        if(currentBalance.getAmount().compareTo(new BigDecimal(0)) == -1){
            throw new RuntimeException("本月库存数量不足，不能出库");
        }
        //  currentBalance.setEndmonthqty(currentBalance.getBeginmonthqty().add(currentBalance.getInamount()).subtract(currentBalance.getOutamount()));
        //currentBalance.setEndyearqty(currentBalance.getBeginyearqty().add(currentBalance.getInamount()).subtract(currentBalance.getOutamount()));

        scmimbalanceMapper.updateById(currentBalance);

        //更新批次档案中的批次数量
        if(currentBalance.getBatchid()!=null){
            Scmbatch scmbatch = scmbatchService.getById(currentBalance.getBatchid());
            if(scmbatch!=null){
                scmbatch.setBatchqty(currentBalance.getAmount().toString());
                scmbatchService.updateById(scmbatch);
            }
        }
    }

    /**
     * 入库记账操作
     * @param companyid
     * @param warehouseid
     * @param vendorid
     * @param billdate
     * @param isred
     * @param scminventoyitem
     */
    private void toInStockZb(String companyid, String warehouseid, String vendorid, String billdate, Boolean isred, Scminventoryitem scminventoyitem,String flag) {
        Scmimbalance currentBalance = getCurrentBalance(companyid, warehouseid, vendorid, billdate,
                scminventoyitem.getMaterialid(),
                scminventoyitem.getStocktypeid(),
                scminventoyitem.getInventorykindid(),
                scminventoyitem.getBatchid(),
                scminventoyitem.getStocklocationid());
        BigDecimal amount = currentBalance.getInamount();
        BigDecimal newAmount=new BigDecimal(0);
        if(isred.equals(true)){
            //红单
            if(flag.equalsIgnoreCase("0")) {
                newAmount = amount.add(new BigDecimal("-" + scminventoyitem.getQty()));
            }else if(flag.equalsIgnoreCase("1")){
                newAmount=amount.add(new BigDecimal(scminventoyitem.getQty()));
            }
        }else {
            if(flag.equalsIgnoreCase("0")) {
                newAmount = amount.add(new BigDecimal(scminventoyitem.getQty()));
            }else if(flag.equalsIgnoreCase("1")){
                newAmount=amount.subtract(new BigDecimal(scminventoyitem.getQty()));
            }
        }

        currentBalance.setInamount(newAmount);
        currentBalance.setAmount(currentBalance.getBeginmonthqty().add(currentBalance.getInamount())
               .subtract(currentBalance.getOutamount()));

        if(currentBalance.getAmount().compareTo(new BigDecimal(0)) == -1){
            throw new RuntimeException("当前库存不足！");
        }
        scmimbalanceMapper.updateById(currentBalance);
    }

    /**
     * 取消出入库操作
     * @param ids
     * @return
     */
    @Override
    public String uoaddToBalance(List<String> ids) {
        for (String id : ids) {
            //todo 获取每个单据的id信息
            Scminventoy scminventoy = scminventoyService.getById(id);

            String inventorytype = scminventoy.getInventorytype();
            //TODO 判断是入库还是出库
            Scminventorytype scminventorytype = scminventorytypeService.getInventoryTypeByTypecode(inventorytype);
            String instock = scminventorytype.getInstock();
            String outstock = scminventorytype.getOutstock();

            if(scminventoy.getStatus() == null || scminventoy.getStatus().equalsIgnoreCase("0")){
                if(instock!=null && instock.equalsIgnoreCase("true")){
                    throw new RuntimeException(scminventoy.getBillcode()+"制单状态，不允许取消入库，请提交审批");
                }else if(outstock!=null && outstock.equalsIgnoreCase("true")){
                    throw new RuntimeException(scminventoy.getBillcode()+"制单状态，不允许取消出库，请提交审批");
                }
            }
            if(instock!=null && instock.equalsIgnoreCase("true")){
                //入库操作
                if(scminventoy.getInventoryflag().equalsIgnoreCase("0")){
                    throw new RuntimeException("未入库，不能进行取消入库操作");
                }
                String companyid=scminventoy.getCompanyid();
                String warehouseid = scminventoy.getWarehouseid();
                String vendorid = scminventoy.getVendorid();
                String billdate = scminventoy.getBilldate().substring(0,7).replace("-","");


                Boolean isred=scminventoy.getIsred();
                QueryWrapper<Scminventoryitem> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("parentid",scminventoy.getId());
                List<Scminventoryitem> list = scminventoyitemService.list(queryWrapper);
                if(list!=null && list.size()>0){
                    for (Scminventoryitem scminventoyitem : list) {
                        //获取当期的账本数据
                        toInStockZb(companyid, warehouseid, vendorid, billdate, isred, scminventoyitem,"1");
                    }
                }
            }
            else if(outstock !=null && outstock.equalsIgnoreCase("true")){
                if(scminventoy.getInventoryflag().equalsIgnoreCase("0")){
                    throw new RuntimeException("未出库，不能进行取消出库操作");
                }
                //出库
                String companyid = scminventoy.getCompanyid();
                String warehouseid = scminventoy.getWarehouseid();
                String vendorid = scminventoy.getVendorid();
                String billdate = scminventoy.getBilldate().substring(0,7).replace("-","");

                QueryWrapper<Scminventoryitem> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("parentid",scminventoy.getId());
                Boolean isred=scminventoy.getIsred();
                List<Scminventoryitem> list = scminventoyitemService.list(queryWrapper);
                if(list!=null && list.size()>0){
                    for (Scminventoryitem scminventoyitem : list) {
                        //获取当期的账本数据
                        toOutStockZb(companyid, warehouseid, vendorid, billdate, isred, scminventoyitem,"1");
                    }
                }
            }

            scminventoy.setInventoryflag("0");
            scminventoyService.updateById(scminventoy);
        }
        return null;
    }

    @Override
    public List<ScmimbalanceVo> getBalanceAmount(IPage<ScmimbalanceVo> page, Map queryMap) {
        QueryWrapper<ScmimbalanceVo> queryWrapper=new QueryWrapper<>();
        if (ERPUtils.ifHtppReqParamContainKey(queryMap,"materialid")) {
            String materialid = ERPUtils.getHttpReqParam(queryMap, "materialid");
            queryWrapper.eq("scmimbalance.materialid",materialid);
        }
        if(ERPUtils.ifHtppReqParamContainKey(queryMap,"vendorid")){
            String vendorid = ERPUtils.getHttpReqParam(queryMap, "vendorid");
            queryWrapper.eq("vendorid",vendorid);
        }
        if(ERPUtils.ifHtppReqParamContainKey(queryMap,"companyid")) {
            String companyid = ERPUtils.getHttpReqParam(queryMap, "companyid");
            queryWrapper.eq("scmimbalance.companyid", companyid);
        }
        List<ScmimbalanceVo> balanceAmount = scmimbalanceMapper.getBalanceAmount(page, queryWrapper);
        return balanceAmount;
    }

    /*移库单移库操作*/
    @Override
    @Transactional
    public String transferBalance(List<String> ids) {

        List<Scmtransferbill> scmtransferbills = scmtransferbillService.listByIds(ids);
        for (Scmtransferbill scmtransferbill : scmtransferbills) {
            //移出仓库
            String fromwarehouseid = scmtransferbill.getFromwarehouseid();
            String towarehouseid = scmtransferbill.getTowarehouseid();
            if(fromwarehouseid.equalsIgnoreCase(towarehouseid)){
                throw new RuntimeException("同一仓库不允许进行移库");
            }

            if(scmtransferbill.getTransferflag().equalsIgnoreCase("1")){
                throw  new RuntimeException("已移库，不能再次进行移库");
            }
            if(scmtransferbill.getStatus().equalsIgnoreCase("0")){
                throw new RuntimeException("移库单为制单，不允许进行移库");
            }

            Scmtransferbill scmtransferbill1 = scmtransferbillService.getById(scmtransferbill.getId());
            scmtransferbill1.setTransferflag("1");

            String companyid = scmtransferbill1.getCompanyid();

            scmtransferbillService.updateById(scmtransferbill1);

            List<Scmtransferbillitem> scmtransferbillitems = scmtransferbillitemService.selectByMainId(scmtransferbill.getId());
            for (Scmtransferbillitem scmtransferbillitem : scmtransferbillitems) {
                //todo 判断移出仓库的可用库存是否满足当前数量
                Scmimbalance currentBalance = getCurrentBalance(companyid,fromwarehouseid, "",
                        scmtransferbill.getBilldate().substring(0,7).replace("-",""),
                        scmtransferbillitem.getMaterialid(),
                        scmtransferbillitem.getStocktypeid(),
                        scmtransferbillitem.getInventorykindid(),
                        scmtransferbillitem.getBatchid(),
                        scmtransferbillitem.getFromstocklocationid());
                if(currentBalance.getAmount().compareTo(new BigDecimal(0)) == 0){
                    throw new RuntimeException("库存数量为0");
                }

                if(currentBalance.getAmount().compareTo(new BigDecimal(scmtransferbillitem.getQty())) == -1){
                    throw  new RuntimeException("当前库存不足，不能移库");
                }


                transferbalancecommon(scmtransferbill, towarehouseid, companyid, scmtransferbillitem, currentBalance,"0");
            }
        }
        return "";
    }

    @Override
    public String untransferBalance(List<String> ids) {
        List<Scmtransferbill> scmtransferbills = scmtransferbillService.listByIds(ids);
        for (Scmtransferbill scmtransferbill : scmtransferbills) {
            //移出仓库
            String fromwarehouseid = scmtransferbill.getFromwarehouseid();
            String towarehouseid = scmtransferbill.getTowarehouseid();
            if(fromwarehouseid.equalsIgnoreCase(towarehouseid)){
                throw new RuntimeException("同一仓库不允许进行移库");
            }

            if(scmtransferbill.getTransferflag().equalsIgnoreCase("0")){
                throw  new RuntimeException("已取消移库，不能再次进行移库");
            }
            if(scmtransferbill.getStatus().equalsIgnoreCase("0")){
                throw new RuntimeException("移库单为制单，不允许进行移库");
            }

            Scmtransferbill scmtransferbill1 = scmtransferbillService.getById(scmtransferbill.getId());
            scmtransferbill1.setTransferflag("0");

            String companyid = scmtransferbill1.getCompanyid();

            scmtransferbillService.updateById(scmtransferbill1);

            List<Scmtransferbillitem> scmtransferbillitems = scmtransferbillitemService.selectByMainId(scmtransferbill.getId());
            for (Scmtransferbillitem scmtransferbillitem : scmtransferbillitems) {
                //todo 判断移出仓库的可用库存是否满足当前数量
                Scmimbalance currentBalance = getCurrentBalance(companyid,fromwarehouseid, "",
                        scmtransferbill.getBilldate().substring(0,7).replace("-",""),
                        scmtransferbillitem.getMaterialid(),
                        scmtransferbillitem.getStocktypeid(),
                        scmtransferbillitem.getInventorykindid(),
                        scmtransferbillitem.getBatchid(),
                        scmtransferbillitem.getFromstocklocationid());
                if(currentBalance.getAmount().compareTo(new BigDecimal(0)) == 0){
                    throw new RuntimeException("库存数量为0");
                }

                if(currentBalance.getAmount().compareTo(new BigDecimal(scmtransferbillitem.getQty())) == -1){
                    throw  new RuntimeException("当前库存不足，不能移库");
                }


                transferbalancecommon(scmtransferbill, towarehouseid, companyid, scmtransferbillitem, currentBalance,"1");
            }
        }
        return "";
    }

    /**
     * 更新移库或者取消移库账本数据
     * @param scmtransferbill
     * @param towarehouseid
     * @param companyid
     * @param scmtransferbillitem
     * @param currentBalance
     * @param flag
     */
    private void transferbalancecommon(Scmtransferbill scmtransferbill, String towarehouseid,
                                       String companyid, Scmtransferbillitem scmtransferbillitem, Scmimbalance currentBalance,String flag) {
        BigDecimal outamount = currentBalance.getOutamount();
        BigDecimal newOutAmount=BigDecimal.ZERO;
        if(flag.equalsIgnoreCase("0")) {
            newOutAmount = outamount.add(new BigDecimal(scmtransferbillitem.getQty()));
        }else if(flag.equalsIgnoreCase("1")){
            newOutAmount=outamount.subtract(new BigDecimal(scmtransferbillitem.getQty()));
        }
        currentBalance.setOutamount(newOutAmount);
        currentBalance.setAmount(currentBalance.getBeginmonthqty().add(currentBalance.getInamount()).subtract(currentBalance.getOutamount()));
        if(currentBalance.getAmount().compareTo(new BigDecimal(0))==-1){
            throw new RuntimeException("当前库存不足，不能移库");
        }
        scmimbalanceMapper.updateById(currentBalance);


        //todo 移入新仓库
        Scmimbalance tocurrentBalance = getCurrentBalance(companyid, towarehouseid, "", scmtransferbill.getBilldate().substring(0,7).replace("-",""),
                scmtransferbillitem.getMaterialid(), scmtransferbillitem.getStocktypeid(),
                scmtransferbillitem.getInventorykindid(), scmtransferbillitem.getBatchid(),
                scmtransferbillitem.getTostocklocationid());
        BigDecimal inamount = tocurrentBalance.getInamount();
        BigDecimal newInAmount=BigDecimal.ZERO;
        if(flag.equalsIgnoreCase("0")) {
            newInAmount = inamount.add(new BigDecimal(scmtransferbillitem.getQty()));
        }else if(flag.equalsIgnoreCase("1")){
            newInAmount=inamount.subtract(new BigDecimal(scmtransferbillitem.getQty()));
        }
        tocurrentBalance.setInamount(newInAmount);
        tocurrentBalance.setAmount(tocurrentBalance.getBeginmonthqty().add(tocurrentBalance.getInamount()).subtract(tocurrentBalance.getOutamount()));
        if(tocurrentBalance.getAmount().compareTo(BigDecimal.ZERO) == -1){
            throw new RuntimeException("当前数量不足，不允许移库");
        }
        scmimbalanceMapper.updateById(tocurrentBalance);
    }


    @Override
    public Scmbatch getScmbatchInfoByCompanyAndWarehouse(String companyid, String warehouseid, String materialid) {
        //todo 查询库存账本
        QueryWrapper<Scmimbalance> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("companyid",companyid);
        queryWrapper.eq("warehouseid",warehouseid);
        queryWrapper.eq("materialid",materialid);
        queryWrapper.orderByAsc("balancedate");

        List<Scmimbalance> scmimbalances = scmimbalanceMapper.selectList(queryWrapper);
        if(scmimbalances!=null && scmimbalances.size()>0){
            Scmimbalance scmimbalance=scmimbalances.get(0);
            if(scmimbalance.getBatchid()!=null && !scmimbalance.getBatchid().equalsIgnoreCase("")){
                Scmbatch scmbatch = scmbatchService.getById(scmimbalance.getBatchid());
                return scmbatch;
            }
        }
        return null;
    }

    @Override
    public List<Scmbatch> getBatchList(Integer pageNo, Integer pageSize,String companyid, String warehouseid, String materialid) {
        List<Scmbatch> res=new ArrayList<>();

        //todo 查询库存账本
        QueryWrapper<Scmimbalance> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("companyid",companyid);
        queryWrapper.eq("warehouseid",warehouseid);
        queryWrapper.eq("materialid",materialid);
       // queryWrapper.ne("amount","0");
        queryWrapper.orderByAsc("balancedate");

        List<Scmimbalance> scmimbalances = scmimbalanceMapper   .selectList(queryWrapper);
        if(scmimbalances!=null && scmimbalances.size()>0){
            for (Scmimbalance scmimbalance : scmimbalances) {
                if(scmimbalance.getBatchid()!=null && !scmimbalance.getBatchid().equalsIgnoreCase("")){
                    Scmbatch scmbatch = scmbatchService.getById(scmimbalance.getBatchid());
                    scmbatch.setBatchqty(scmimbalance.getAmount().toString());
                    res.add(scmbatch);
                }
            }
        }

        return res;
    }

    @Override
    public List<Scmbatch> getBatchListByMap(Integer pageNo, Integer pageSize, Map paramMap) {
        List<Scmbatch> res=new ArrayList<>();

        //todo 查询库存账本
        QueryWrapper<Scmimbalance> queryWrapper=new QueryWrapper<>();
        if(ERPUtils.ifHtppReqParamContainKey(paramMap,"companyid")) {
            queryWrapper.eq("companyid", ERPUtils.getHttpReqParam(paramMap,"companyid"));
        }else{
            throw new RuntimeException("请选择公司");
        }
        if(ERPUtils.ifHtppReqParamContainKey(paramMap,"warehouseid")){
            queryWrapper.eq("warehouseid",ERPUtils.getHttpReqParam(paramMap,"warehouseid"));
        }else{
            throw new RuntimeException("请选择仓库");
        }
        if(ERPUtils.ifHtppReqParamContainKey(paramMap,"materialid")) {
            queryWrapper.eq("materialid", ERPUtils.getHttpReqParam(paramMap,"materialid"));
        }else{
            throw new RuntimeException("请选择物料");
        }
        if(ERPUtils.ifHtppReqParamContainKey(paramMap,"stocklocationid")) {
            queryWrapper.eq("stocklocationid", ERPUtils.getHttpReqParam(paramMap, "stocklocationid"));
        }
        // queryWrapper.ne("amount","0");
        queryWrapper.orderByAsc("balancedate");

        List<Scmimbalance> scmimbalances = scmimbalanceMapper.selectList(queryWrapper);
        if(scmimbalances!=null && scmimbalances.size()>0){
            for (Scmimbalance scmimbalance : scmimbalances) {
                if(scmimbalance.getBatchid()!=null && !scmimbalance.getBatchid().equalsIgnoreCase("")){
                    Scmbatch scmbatch = scmbatchService.getById(scmimbalance.getBatchid());
                    scmbatch.setBatchqty(scmimbalance.getAmount().toString());
                    res.add(scmbatch);
                }
            }
        }

        return res;
    }

    /**
     * 盘点入库或出库处理
     * @param ids
     * @return
     */
    @Override
    public String stockCheck(List<String> ids) {
        return null;
    }

    /**
     * 获取账本表数据
     * @param warehouseid
     * @param vendorid
     * @param billdate
     * @return
     */
    private Scmimbalance getCurrentBalance(String companyid,String warehouseid, String vendorid, String billdate,
                                           String materialid,String stocktypeid,String inventorykindid,
                                           String batchid,
                                           String stocklocationid) {
       //先查询是否存在下一个月的期间，如果存在，不允许进行出入库业务
        if(!ifExistsNextCurrentBalance(companyid,warehouseid,vendorid,billdate,
                materialid,stocktypeid,inventorykindid,batchid,stocklocationid)){
            throw new RuntimeException("存在下个月的库存账本数据，不要在当前月份操作出入库业务");
        }

        //判断当前期间状态
        if(!imperiodService.checkPeriodState(companyid,billdate)){
            throw new RuntimeException("请查看当前库存期间状态！");
        }

        List<Scmimbalance> scmimbalances = getScmimbalances(companyid,warehouseid, vendorid, billdate, materialid,
                 stocktypeid, inventorykindid,batchid,stocklocationid);
        if(scmimbalances==null || scmimbalances.size() ==0){
            //todo 不存在当前期间，直接插入到当前期间表里
            Scmimbalance scmimbalance=new Scmimbalance();
            scmimbalance.setBalancedate(billdate);
//            if(vendorid!=null && !vendorid.equalsIgnoreCase("")) {
//                scmimbalance.setVendorid(vendorid);
//            }else{
//                scmimbalance.setVendorid("");
//            }
            if(warehouseid!=null && !warehouseid.equalsIgnoreCase("")){
                scmimbalance.setWarehouseid(warehouseid);
            }else{
                scmimbalance.setWarehouseid("");
            }
            if(batchid!=null && !batchid.equalsIgnoreCase("")){
                scmimbalance.setBatchid(batchid);
            }else{
                scmimbalance.setBatchid("");
            }
            scmimbalance.setMaterialid(materialid);
            if(stocktypeid!=null){
                scmimbalance.setStocktypeid(stocktypeid);
            }

            if(inventorykindid!=null){
                scmimbalance.setInventorykindid(inventorykindid);
            }
            if(stocklocationid == null){
                scmimbalance.setStocklocationid("");
            }else{
                scmimbalance.setStocklocationid(stocklocationid);
            }
            scmimbalance.setCompanyid(companyid);
            scmimbalance.setBeginyearqty(new BigDecimal(0));
            scmimbalance.setBeginmonthqty(new BigDecimal(0));
            scmimbalance.setBeginmonthqty(new BigDecimal(0));
            scmimbalance.setEndyearqty(new BigDecimal(0));
           // scmimbalance.setEndmonthqty(new BigDecimal(0));
            scmimbalance.setAmount(new BigDecimal(0));
            scmimbalance.setInamount(new BigDecimal(0));
            scmimbalance.setOutamount(new BigDecimal(0));
            //自动创建
            scmimbalanceMapper.insert(scmimbalance);
            return scmimbalance;
        }
        return scmimbalances.get(0);
    }

    /**
     * 查询是否存在账本数据
     * @param warehouseid
     * @param vendorid
     * @param billdate
     * @return
     */
    private List<Scmimbalance> getScmimbalances(String companyid,String warehouseid, String vendorid, String billdate,String materialid, String stocktypeid,String inventorykindid,
                                                String batchid,String stocklocationid) {
        QueryWrapper<Scmimbalance> scmimbalanceQueryWrapper=new QueryWrapper<>();
        scmimbalanceQueryWrapper.eq("balancedate", billdate);
        scmimbalanceQueryWrapper.eq("materialid",materialid);
        scmimbalanceQueryWrapper.eq("companyid",companyid);
//        if(vendorid !=null && !vendorid.equalsIgnoreCase("")) {
//            scmimbalanceQueryWrapper.eq("vendorid", vendorid);
//        }else{
//            //scmimbalanceQueryWrapper.eq("vendorid","").or().eq("vendorid",null);
//            scmimbalanceQueryWrapper.and(item->item.eq("vendorid","").or(qw->qw.eq("vendorid",null)));
//        }
        /*库存状态判断*/
        if(stocktypeid!=null){
            scmimbalanceQueryWrapper.eq("stocktypeid",stocktypeid);
        }else{
            scmimbalanceQueryWrapper.eq("stocktypeid","");
        }

        /*库存类型判断*/
        if(inventorykindid !=null){
            scmimbalanceQueryWrapper.eq("inventorykindid",inventorykindid);
        }else{
            scmimbalanceQueryWrapper.eq("inventorykindid","");
        }


        /*仓库判断*/
        if(warehouseid !=null && !warehouseid.equalsIgnoreCase("")) {
            scmimbalanceQueryWrapper.eq("warehouseid", warehouseid);
        }else{
            scmimbalanceQueryWrapper.and(item->item.eq("warehouseid","").or(qw->qw.eq("warehouseid",null)));
            //scmimbalanceQueryWrapper.eq("warehouseid","").or().eq("warehouseid",null);
        }

        /*批次判断*/
        if(batchid !=null && !batchid.equalsIgnoreCase("")){
            scmimbalanceQueryWrapper.eq("batchid",batchid);
        }else{
            scmimbalanceQueryWrapper.eq("batchid","").or().eq("batchid",null);
        }

        if(stocklocationid != null && !stocklocationid.equalsIgnoreCase("")){
            scmimbalanceQueryWrapper.eq("stocklocationid",stocklocationid);
        }else{
            scmimbalanceQueryWrapper.and(item->item.eq("stocklocationid","").or(a->a.isNull("stocklocationid")));
        }
        List<Scmimbalance> scmimbalances = scmimbalanceMapper.selectList(scmimbalanceQueryWrapper);
        return scmimbalances;
    }


    /**
     * 判断是否存在下个月或者下下个月的账本数据，如果存在，则当前账本数据不允许进行业务操作
     * @param warehouseid
     * @param vendorid
     * @param billdate
     * @return
     */
    private Boolean ifExistsNextCurrentBalance(String companyid,String warehouseid, String vendorid,
                                               String billdate, String materialid,String stocktypeid,
                                               String inventorykindid,String batchid,String stocklocationid){
        //先查询是否存在下一个月的期间，如果存在，不允许进行出入库业务
        SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
        String currentbilldate=billdate.substring(0,4)+"-"+billdate.substring(4,6)+"-"+"01";
        Calendar calendar=Calendar.getInstance();
        Timestamp timestamp = ERPUtils.getTimestamp(currentbilldate, "");
        calendar.setTime(timestamp);
        calendar.add(Calendar.MONTH,1);
        Date time = calendar.getTime();
        String format = df.format(time);
        String substring = format.substring(0, 6);
        List<Scmimbalance> scmimbalances = getScmimbalances(companyid,warehouseid, vendorid, substring,materialid,
                stocktypeid,
                inventorykindid,
                batchid,stocklocationid);
        if(scmimbalances==null || scmimbalances.size()==0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Map getBalanceByCompanyAndWarehouse(String companyid, String warehouseid, String batchid, String materialid) {
        Map res=new HashMap();
        QueryWrapper<Scmimbalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("companyid", companyid);
        queryWrapper.eq("materialid",materialid);
        queryWrapper.eq("warehouseid",warehouseid);
        queryWrapper.eq("batchid",batchid);
        String replace = ERPUtils.getNowDate().substring(0, 7).replace("-", "");
        queryWrapper.eq("balancedate",replace);
        List<Scmimbalance> scmimbalances = scmimbalanceMapper.selectList(queryWrapper);
        if(scmimbalances!=null && scmimbalances.size()>0){
            res.put("amout",scmimbalances.get(0).getAmount());
        }else{
            res.put("amout",0);
        }
        return res;
    }



}
