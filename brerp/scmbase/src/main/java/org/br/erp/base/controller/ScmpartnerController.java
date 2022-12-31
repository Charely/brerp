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
import javax.websocket.server.PathParam;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.base.entity.Scmpartner;
import org.br.erp.base.service.IScmpartnerService;

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
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Api(tags="往来单位")
@RestController
@RequestMapping("/base/scmpartner")
@Slf4j
public class ScmpartnerController extends JeecgController<Scmpartner, IScmpartnerService> {
	@Autowired
	private IScmpartnerService scmpartnerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmpartner
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "往来单位-分页列表查询")
	@ApiOperation(value="往来单位-分页列表查询", notes="往来单位-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmpartner>> queryPageList(Scmpartner scmpartner,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmpartner> queryWrapper = QueryGenerator.initQueryWrapper(scmpartner, req.getParameterMap());
		Page<Scmpartner> page = new Page<Scmpartner>(pageNo, pageSize);
		IPage<Scmpartner> pageList = scmpartnerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 @ApiOperation(value="往来单位-分页列表查询", notes="往来单位-分页列表查询")
	 @GetMapping(value = "/vendorlist/{type}")
	 public Result<IPage<Scmpartner>> queryVendorList(@PathVariable String type, Scmpartner scmpartner,
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													HttpServletRequest req) {
		 QueryWrapper<Scmpartner> queryWrapper = QueryGenerator.initQueryWrapper(scmpartner, req.getParameterMap());
		 Page<Scmpartner> page = new Page<Scmpartner>(pageNo, pageSize);
		 if(type.equalsIgnoreCase("vendor")){
			 queryWrapper.like("partnertype","0");
		 }else if(type.equalsIgnoreCase("customer")){
			 queryWrapper.like("partnertype","1");
		 }
		 IPage<Scmpartner> pageList = scmpartnerService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }


	
	/**
	 *   添加
	 *
	 * @param scmpartner
	 * @return
	 */
	@AutoLog(value = "往来单位-添加")
	@ApiOperation(value="往来单位-添加", notes="往来单位-添加")
	//@RequiresPermissions("org.br.erp:scmpartner:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmpartner scmpartner) {
		scmpartnerService.save(scmpartner);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmpartner
	 * @return
	 */
	@AutoLog(value = "往来单位-编辑")
	@ApiOperation(value="往来单位-编辑", notes="往来单位-编辑")
	//@RequiresPermissions("org.br.erp:scmpartner:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmpartner scmpartner) {
		scmpartnerService.updateById(scmpartner);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "往来单位-通过id删除")
	@ApiOperation(value="往来单位-通过id删除", notes="往来单位-通过id删除")
	//@RequiresPermissions("org.br.erp:scmpartner:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmpartnerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "往来单位-批量删除")
	@ApiOperation(value="往来单位-批量删除", notes="往来单位-批量删除")
	//@RequiresPermissions("org.br.erp:scmpartner:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmpartnerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "往来单位-通过id查询")
	@ApiOperation(value="往来单位-通过id查询", notes="往来单位-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmpartner> queryById(@RequestParam(name="id",required=true) String id) {
		Scmpartner scmpartner = scmpartnerService.getById(id);
		if(scmpartner==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmpartner);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmpartner
    */
    //@RequiresPermissions("org.br.erp:scmpartner:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmpartner scmpartner) {
        return super.exportXls(request, scmpartner, Scmpartner.class, "往来单位");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmpartner:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmpartner.class);
    }


	@GetMapping("/getpartnerbyid")
	public Result<Scmpartner> getPartnerById(@RequestParam("id") String id){
		Scmpartner byId = scmpartnerService.getById(id);
		return Result.ok(byId);
	}

	@GetMapping("/getpartnerlist")
	public Result<List<Scmpartner>> getVendorList(@RequestParam("partnertype") String partnertype){
		List<Scmpartner> partnerList = scmpartnerService.getPartnerList(partnertype);
		return Result.OK(partnerList);
	}

}
