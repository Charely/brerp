package org.br.erp.inventory.base.controller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.br.erp.base.entity.Scmbatch;
import org.br.erp.base.utils.ERPUtils;
import org.br.erp.inventory.base.vo.ScmimbalanceVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.inventory.base.entity.Scmimbalance;
import org.br.erp.inventory.base.service.IScmimbalanceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 库存账本表
 * @Author: jeecg-boot
 * @Date:   2022-10-24
 * @Version: V1.0
 */
@Api(tags="库存账本表")
@RestController
@RequestMapping("/inventory.base/scmimbalance")
@Slf4j
public class ScmimbalanceController extends JeecgController<Scmimbalance, IScmimbalanceService> {
	@Autowired
	private IScmimbalanceService scmimbalanceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmimbalance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存账本表-分页列表查询")
	@ApiOperation(value="库存账本表-分页列表查询", notes="库存账本表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmimbalance>> queryPageList(Scmimbalance scmimbalance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmimbalance> queryWrapper = QueryGenerator.initQueryWrapper(scmimbalance, req.getParameterMap());
		Page<Scmimbalance> page = new Page<Scmimbalance>(pageNo, pageSize);
		IPage<Scmimbalance> pageList = scmimbalanceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmimbalance
	 * @return
	 */
	@AutoLog(value = "库存账本表-添加")
	@ApiOperation(value="库存账本表-添加", notes="库存账本表-添加")
	//@RequiresPermissions("org.br.erp:scmimbalance:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmimbalance scmimbalance) {
		scmimbalanceService.save(scmimbalance);
		return Result.OK("添加成功！");
	}

	 /**
	  * 向账本中添加数据
	  * @param ids
	  * @return
	  */
	 @RequestMapping(value = "addtobalance")
	public Result<String> addToBalance(@RequestBody String ids){
		if(ids!=null){
			Map idsMap = (Map)JSON.parse(ids);
			String ids1 = (String)idsMap.get("ids");
			String[] split = ids1.split(",");
			scmimbalanceService.addToBalance(Arrays.asList(split));
			return Result.OK("入库成功");
		}else {
			return Result.error("入库失败");
		}

	}

	 /**
	  * 取消出入库操作
	  * @param ids
	  * @return
	  */
	 @RequestMapping(value = "unaddtobalance")
	 public Result<String> unaddToBalance(@RequestBody String ids){
		 if(ids!=null){
			 Map idsMap = (Map)JSON.parse(ids);
			 String ids1 = (String)idsMap.get("ids");
			 String[] split = ids1.split(",");
			 scmimbalanceService.uoaddToBalance(Arrays.asList(split));
			 return Result.OK("取消成功");
		 }else {
			 return Result.error("取消失败");
		 }

	 }



	 /**
	 *  编辑
	 *
	 * @param scmimbalance
	 * @return
	 */
	@AutoLog(value = "库存账本表-编辑")
	@ApiOperation(value="库存账本表-编辑", notes="库存账本表-编辑")
	//@RequiresPermissions("org.br.erp:scmimbalance:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmimbalance scmimbalance) {
		scmimbalanceService.updateById(scmimbalance);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存账本表-通过id删除")
	@ApiOperation(value="库存账本表-通过id删除", notes="库存账本表-通过id删除")
	//@RequiresPermissions("org.br.erp:scmimbalance:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmimbalanceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存账本表-批量删除")
	@ApiOperation(value="库存账本表-批量删除", notes="库存账本表-批量删除")
	//@RequiresPermissions("org.br.erp:scmimbalance:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmimbalanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存账本表-通过id查询")
	@ApiOperation(value="库存账本表-通过id查询", notes="库存账本表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmimbalance> queryById(@RequestParam(name="id",required=true) String id) {
		Scmimbalance scmimbalance = scmimbalanceService.getById(id);
		if(scmimbalance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmimbalance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmimbalance
    */
    //@RequiresPermissions("org.br.erp:scmimbalance:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmimbalance scmimbalance) {
        return super.exportXls(request, scmimbalance, Scmimbalance.class, "库存账本表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmimbalance:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmimbalance.class);
    }

	@GetMapping("/getbalanceamount")
	public Result<IPage<ScmimbalanceVo>> getImBalanceAmount( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															 HttpServletRequest req)
	{
		Map<String, String[]> parameterMap = req.getParameterMap();
		IPage<ScmimbalanceVo> page=new Page<>(pageNo,pageSize);
		List<ScmimbalanceVo> balanceAmount = scmimbalanceService.getBalanceAmount(page, parameterMap);
		page.setRecords(balanceAmount);
		return Result.ok(page);
	}


	 @RequestMapping(value = "/transferbalance")
	 public Result<String> transferBalance(@RequestBody String ids) {
		 if(ids!=null) {
			 Map idsMap = (Map) JSON.parse(ids);
			 String ids1 = (String) idsMap.get("ids");
			 String[] split = ids1.split(",");
			 String s = scmimbalanceService.transferBalance(Arrays.asList(split));
		 }
		 return Result.ok("转移成功");
	 }

	 @RequestMapping(value = "/untransferbalance")
	 public Result<String> untransferBalance(@RequestBody String ids) {
		 if(ids!=null) {
			 Map idsMap = (Map) JSON.parse(ids);
			 String ids1 = (String) idsMap.get("ids");
			 String[] split = ids1.split(",");
			 String s = scmimbalanceService.untransferBalance(Arrays.asList(split));
		 }
		 return Result.ok("取消转移成功");
	 }
	 @GetMapping("/getscmbatchbycompanyidandmaterialid")
	 public Result<Scmbatch> getscmbatchByCompanyandmaterial(@RequestParam(value = "companyid")String companyid,
															 @RequestParam(value = "warehouseid")String warehouseid,
															 @RequestParam(value = "materialid")String materialid){
		 Scmbatch scmbatch = scmimbalanceService.getScmbatchInfoByCompanyAndWarehouse(companyid, warehouseid,materialid);
		 return Result.ok(scmbatch);
	 }


	 @GetMapping(value = {"/batchlist"})
	 public Result<List<Scmbatch>> queryBatchList(
			 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			 HttpServletRequest req){

		 Map<String, String[]> parameterMap = req.getParameterMap();
//		 String companyid="";
//		 String warehouseid="";
//		 String materialid="";
//		 if(ERPUtils.ifHtppReqParamContainKey(parameterMap,"companyid")){
//			 companyid = ERPUtils.getHttpReqParam(parameterMap, "companyid");
//		 }else{
//			 throw new RuntimeException("请先选择公司");
//		 }
//		 if(ERPUtils.ifHtppReqParamContainKey(parameterMap,"warehouseid")){
//			 warehouseid=ERPUtils.getHttpReqParam(parameterMap,"warehouseid");
//		 }else{
//			 throw new RuntimeException("请先选择仓库");
//		 }
//		 if(ERPUtils.ifHtppReqParamContainKey(parameterMap,"materialid")){
//			 materialid=ERPUtils.getHttpReqParam(parameterMap,"materialid");
//		 }else{
//			 throw new RuntimeException("请先选择物料");
//		 }
		 //List<Scmbatch> batchList = scmimbalanceService.getBatchList(pageNo,pageSize,companyid, warehouseid, materialid);
		 List<Scmbatch> batchListByMap = scmimbalanceService.getBatchListByMap(pageNo, pageSize, parameterMap);
		 return Result.ok(batchListByMap);
	 }


}
