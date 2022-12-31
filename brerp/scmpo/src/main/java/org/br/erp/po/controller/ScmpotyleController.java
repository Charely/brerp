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
import org.br.erp.po.entity.Scmpotyle;
import org.br.erp.po.service.IScmpotyleService;

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
 * @Description: 采购订单类型
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Api(tags="采购订单类型")
@RestController
@RequestMapping("/po/scmpotyle")
@Slf4j
public class ScmpotyleController extends JeecgController<Scmpotyle, IScmpotyleService> {
	@Autowired
	private IScmpotyleService scmpotyleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmpotyle
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购订单类型-分页列表查询")
	@ApiOperation(value="采购订单类型-分页列表查询", notes="采购订单类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmpotyle>> queryPageList(Scmpotyle scmpotyle,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmpotyle> queryWrapper = QueryGenerator.initQueryWrapper(scmpotyle, req.getParameterMap());
		Page<Scmpotyle> page = new Page<Scmpotyle>(pageNo, pageSize);
		IPage<Scmpotyle> pageList = scmpotyleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmpotyle
	 * @return
	 */
	@AutoLog(value = "采购订单类型-添加")
	@ApiOperation(value="采购订单类型-添加", notes="采购订单类型-添加")
	//@RequiresPermissions("org.br.erp:scmpotyle:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmpotyle scmpotyle) {
		scmpotyleService.save(scmpotyle);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmpotyle
	 * @return
	 */
	@AutoLog(value = "采购订单类型-编辑")
	@ApiOperation(value="采购订单类型-编辑", notes="采购订单类型-编辑")
	//@RequiresPermissions("org.br.erp:scmpotyle:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmpotyle scmpotyle) {
		scmpotyleService.updateById(scmpotyle);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购订单类型-通过id删除")
	@ApiOperation(value="采购订单类型-通过id删除", notes="采购订单类型-通过id删除")
	//@RequiresPermissions("org.br.erp:scmpotyle:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmpotyleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购订单类型-批量删除")
	@ApiOperation(value="采购订单类型-批量删除", notes="采购订单类型-批量删除")
	//@RequiresPermissions("org.br.erp:scmpotyle:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmpotyleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购订单类型-通过id查询")
	@ApiOperation(value="采购订单类型-通过id查询", notes="采购订单类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmpotyle> queryById(@RequestParam(name="id",required=true) String id) {
		Scmpotyle scmpotyle = scmpotyleService.getById(id);
		if(scmpotyle==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmpotyle);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmpotyle
    */
    //@RequiresPermissions("org.br.erp:scmpotyle:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmpotyle scmpotyle) {
        return super.exportXls(request, scmpotyle, Scmpotyle.class, "采购订单类型");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmpotyle:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmpotyle.class);
    }

}
