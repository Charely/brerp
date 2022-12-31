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
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.base.entity.Scmuomconversion;
import org.br.erp.base.service.IScmuomconversionService;

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
 * @Description: 计量单位换算率
 * @Author: jeecg-boot
 * @Date:   2022-11-18
 * @Version: V1.0
 */
@Api(tags="计量单位换算率")
@RestController
@RequestMapping("/base/scmuomconversion")
@Slf4j
public class ScmuomconversionController extends JeecgController<Scmuomconversion, IScmuomconversionService> {
	@Autowired
	private IScmuomconversionService scmuomconversionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmuomconversion
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "计量单位换算率-分页列表查询")
	@ApiOperation(value="计量单位换算率-分页列表查询", notes="计量单位换算率-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmuomconversion>> queryPageList(Scmuomconversion scmuomconversion,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmuomconversion> queryWrapper = QueryGenerator.initQueryWrapper(scmuomconversion, req.getParameterMap());
		Page<Scmuomconversion> page = new Page<Scmuomconversion>(pageNo, pageSize);
		IPage<Scmuomconversion> pageList = scmuomconversionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmuomconversion
	 * @return
	 */
	@AutoLog(value = "计量单位换算率-添加")
	@ApiOperation(value="计量单位换算率-添加", notes="计量单位换算率-添加")
	//@RequiresPermissions("org.br.erp:scmuomconversion:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmuomconversion scmuomconversion) {
		scmuomconversionService.save(scmuomconversion);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmuomconversion
	 * @return
	 */
	@AutoLog(value = "计量单位换算率-编辑")
	@ApiOperation(value="计量单位换算率-编辑", notes="计量单位换算率-编辑")
	//@RequiresPermissions("org.br.erp:scmuomconversion:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmuomconversion scmuomconversion) {
		scmuomconversionService.updateById(scmuomconversion);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量单位换算率-通过id删除")
	@ApiOperation(value="计量单位换算率-通过id删除", notes="计量单位换算率-通过id删除")
	//@RequiresPermissions("org.br.erp:scmuomconversion:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmuomconversionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "计量单位换算率-批量删除")
	@ApiOperation(value="计量单位换算率-批量删除", notes="计量单位换算率-批量删除")
	//@RequiresPermissions("org.br.erp:scmuomconversion:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmuomconversionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "计量单位换算率-通过id查询")
	@ApiOperation(value="计量单位换算率-通过id查询", notes="计量单位换算率-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmuomconversion> queryById(@RequestParam(name="id",required=true) String id) {
		Scmuomconversion scmuomconversion = scmuomconversionService.getById(id);
		if(scmuomconversion==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmuomconversion);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmuomconversion
    */
    //@RequiresPermissions("org.br.erp:scmuomconversion:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmuomconversion scmuomconversion) {
        return super.exportXls(request, scmuomconversion, Scmuomconversion.class, "计量单位换算率");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmuomconversion:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmuomconversion.class);
    }

}
