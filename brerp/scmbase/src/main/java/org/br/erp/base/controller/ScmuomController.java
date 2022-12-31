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
import org.br.erp.base.entity.Scmuom;
import org.br.erp.base.service.IScmuomService;

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
 * @Description: 计量单位定义
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Api(tags="计量单位定义")
@RestController
@RequestMapping("/base/scmuom")
@Slf4j
public class ScmuomController extends JeecgController<Scmuom, IScmuomService> {
	@Autowired
	private IScmuomService scmuomService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmuom
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "计量单位定义-分页列表查询")
	@ApiOperation(value="计量单位定义-分页列表查询", notes="计量单位定义-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmuom>> queryPageList(Scmuom scmuom,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmuom> queryWrapper = QueryGenerator.initQueryWrapper(scmuom, req.getParameterMap());
		Page<Scmuom> page = new Page<Scmuom>(pageNo, pageSize);
		IPage<Scmuom> pageList = scmuomService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmuom
	 * @return
	 */
	@AutoLog(value = "计量单位定义-添加")
	@ApiOperation(value="计量单位定义-添加", notes="计量单位定义-添加")
	//@RequiresPermissions("org.br.erp:scmuom:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmuom scmuom) {
		scmuomService.save(scmuom);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmuom
	 * @return
	 */
	@AutoLog(value = "计量单位定义-编辑")
	@ApiOperation(value="计量单位定义-编辑", notes="计量单位定义-编辑")
	//@RequiresPermissions("org.br.erp:scmuom:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmuom scmuom) {
		scmuomService.updateById(scmuom);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "计量单位定义-通过id删除")
	@ApiOperation(value="计量单位定义-通过id删除", notes="计量单位定义-通过id删除")
	//@RequiresPermissions("org.br.erp:scmuom:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmuomService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "计量单位定义-批量删除")
	@ApiOperation(value="计量单位定义-批量删除", notes="计量单位定义-批量删除")
	//@RequiresPermissions("org.br.erp:scmuom:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmuomService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "计量单位定义-通过id查询")
	@ApiOperation(value="计量单位定义-通过id查询", notes="计量单位定义-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmuom> queryById(@RequestParam(name="id",required=true) String id) {
		Scmuom scmuom = scmuomService.getById(id);
		if(scmuom==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmuom);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmuom
    */
    //@RequiresPermissions("org.br.erp:scmuom:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmuom scmuom) {
        return super.exportXls(request, scmuom, Scmuom.class, "计量单位定义");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmuom:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmuom.class);
    }

}
