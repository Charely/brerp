package org.br.erp.po.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.po.entity.Scmpoitemtype;
import org.br.erp.po.service.IScmpoitemtypeService;

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
 * @Description: 采购行类别定义
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Api(tags="采购行类别定义")
@RestController
@RequestMapping("/po/scmpoitemtype")
@Slf4j
public class ScmpoitemtypeController extends JeecgController<Scmpoitemtype, IScmpoitemtypeService> {
	@Autowired
	private IScmpoitemtypeService scmpoitemtypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmpoitemtype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购行类别定义-分页列表查询")
	@ApiOperation(value="采购行类别定义-分页列表查询", notes="采购行类别定义-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmpoitemtype>> queryPageList(Scmpoitemtype scmpoitemtype,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmpoitemtype> queryWrapper = QueryGenerator.initQueryWrapper(scmpoitemtype, req.getParameterMap());
		Page<Scmpoitemtype> page = new Page<Scmpoitemtype>(pageNo, pageSize);
		IPage<Scmpoitemtype> pageList = scmpoitemtypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmpoitemtype
	 * @return
	 */
	@AutoLog(value = "采购行类别定义-添加")
	@ApiOperation(value="采购行类别定义-添加", notes="采购行类别定义-添加")
	//@RequiresPermissions("org.br.erp:scmpoitemtype:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmpoitemtype scmpoitemtype) {
		scmpoitemtypeService.save(scmpoitemtype);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmpoitemtype
	 * @return
	 */
	@AutoLog(value = "采购行类别定义-编辑")
	@ApiOperation(value="采购行类别定义-编辑", notes="采购行类别定义-编辑")
	//@RequiresPermissions("org.br.erp:scmpoitemtype:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmpoitemtype scmpoitemtype) {
		scmpoitemtypeService.updateById(scmpoitemtype);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购行类别定义-通过id删除")
	@ApiOperation(value="采购行类别定义-通过id删除", notes="采购行类别定义-通过id删除")
	//@RequiresPermissions("org.br.erp:scmpoitemtype:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmpoitemtypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购行类别定义-批量删除")
	@ApiOperation(value="采购行类别定义-批量删除", notes="采购行类别定义-批量删除")
	//@RequiresPermissions("org.br.erp:scmpoitemtype:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmpoitemtypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购行类别定义-通过id查询")
	@ApiOperation(value="采购行类别定义-通过id查询", notes="采购行类别定义-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmpoitemtype> queryById(@RequestParam(name="id",required=true) String id) {
		Scmpoitemtype scmpoitemtype = scmpoitemtypeService.getById(id);
		if(scmpoitemtype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmpoitemtype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmpoitemtype
    */
    //@RequiresPermissions("org.br.erp:scmpoitemtype:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmpoitemtype scmpoitemtype) {
        return super.exportXls(request, scmpoitemtype, Scmpoitemtype.class, "采购行类别定义");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmpoitemtype:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmpoitemtype.class);
    }

}
