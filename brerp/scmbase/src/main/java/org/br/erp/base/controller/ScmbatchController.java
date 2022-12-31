package org.br.erp.base.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.br.erp.base.utils.ERPUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.base.entity.Scmbatch;
import org.br.erp.base.service.IScmbatchService;

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
 * @Description: 批号档案
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Api(tags="批号档案")
@RestController
@RequestMapping("/base/scmbatch")
@Slf4j
public class ScmbatchController extends JeecgController<Scmbatch, IScmbatchService> {
	@Autowired
	private IScmbatchService scmbatchService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmbatch
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "批号档案-分页列表查询")
	@ApiOperation(value="批号档案-分页列表查询", notes="批号档案-分页列表查询")
	@GetMapping(value = {"/list"})
	public Result<IPage<Scmbatch>> queryPageList(Scmbatch scmbatch,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,
												 @PathVariable(required = false)String companyid,
												 @PathVariable(required = false)String materialid) {
		QueryWrapper<Scmbatch> queryWrapper = QueryGenerator.initQueryWrapper(scmbatch, req.getParameterMap());
		Page<Scmbatch> page = new Page<Scmbatch>(pageNo, pageSize);
		if(companyid!=null && !companyid.equalsIgnoreCase("")){
			queryWrapper.eq("companyid",companyid);
		}
		if(materialid!=null && !materialid.trim().equalsIgnoreCase("")){
			queryWrapper.eq("materialid",materialid);
		}
		IPage<Scmbatch> pageList = scmbatchService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @GetMapping(value = {"/batchlist"})
	 public Result<IPage<Scmbatch>> queryPageLists(
												  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												  HttpServletRequest req) {

		 QueryWrapper<Scmbatch> queryWrapper = new QueryWrapper<>();
		 Page<Scmbatch> page = new Page<Scmbatch>(pageNo, pageSize);
		 Map<String, String[]> parameterMap = req.getParameterMap();

		 if(ERPUtils.ifHtppReqParamContainKey(parameterMap,"companyid")){
			 queryWrapper.eq("companyid",ERPUtils.getHttpReqParam(parameterMap,"companyid"));
		 }

		 if(ERPUtils.ifHtppReqParamContainKey(parameterMap,"warehouseid")){
			 queryWrapper.eq("warehouseid",ERPUtils.getHttpReqParam(parameterMap,"warehouseid"));
		 }
		 if(ERPUtils.ifHtppReqParamContainKey(parameterMap,"materialid")){
			 queryWrapper.eq("materialid",ERPUtils.getHttpReqParam(parameterMap,"materialid"));
		 }
		 IPage<Scmbatch> pageList = scmbatchService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	@GetMapping("/getscmbatchbycompanyidandmaterialid")
	public Result<Scmbatch> getscmbatchByCompanyandmaterial(@RequestParam(value = "companyid")String companyid,
															@RequestParam(value = "warehouseid")String warehouseid,
															@RequestParam(value = "materialid")String materialid){
		Scmbatch scmbatch = scmbatchService.getscmbatchByCompanyandmaterial(companyid, warehouseid,materialid);
		return Result.ok(scmbatch);
	}
	
	/**
	 *   添加
	 *
	 * @param scmbatch
	 * @return
	 */
	@AutoLog(value = "批号档案-添加")
	@ApiOperation(value="批号档案-添加", notes="批号档案-添加")
	//@RequiresPermissions("org.br.erp:scmbatch:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmbatch scmbatch) {
		scmbatchService.save(scmbatch);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmbatch
	 * @return
	 */
	@AutoLog(value = "批号档案-编辑")
	@ApiOperation(value="批号档案-编辑", notes="批号档案-编辑")
	//@RequiresPermissions("org.br.erp:scmbatch:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmbatch scmbatch) {
		scmbatchService.updateById(scmbatch);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "批号档案-通过id删除")
	@ApiOperation(value="批号档案-通过id删除", notes="批号档案-通过id删除")
	//@RequiresPermissions("org.br.erp:scmbatch:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmbatchService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "批号档案-批量删除")
	@ApiOperation(value="批号档案-批量删除", notes="批号档案-批量删除")
	//@RequiresPermissions("org.br.erp:scmbatch:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmbatchService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "批号档案-通过id查询")
	@ApiOperation(value="批号档案-通过id查询", notes="批号档案-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmbatch> queryById(@RequestParam(name="id",required=true) String id) {
		Scmbatch scmbatch = scmbatchService.getById(id);
		if(scmbatch==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmbatch);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmbatch
    */
    //@RequiresPermissions("org.br.erp:scmbatch:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmbatch scmbatch) {
        return super.exportXls(request, scmbatch, Scmbatch.class, "批号档案");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmbatch:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmbatch.class);
    }

}
