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
import org.br.erp.base.entity.Scmformtypes;
import org.br.erp.base.service.IScmformtypesService;

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
 * @Description: 控件列描述
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
@Api(tags="控件列描述")
@RestController
@RequestMapping("/base/scmformtypes")
@Slf4j
public class ScmformtypesController extends JeecgController<Scmformtypes, IScmformtypesService> {
	@Autowired
	private IScmformtypesService scmformtypesService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmformtypes
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "控件列描述-分页列表查询")
	@ApiOperation(value="控件列描述-分页列表查询", notes="控件列描述-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmformtypes>> queryPageList(Scmformtypes scmformtypes,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmformtypes> queryWrapper = QueryGenerator.initQueryWrapper(scmformtypes, req.getParameterMap());
		Page<Scmformtypes> page = new Page<Scmformtypes>(pageNo, pageSize);
		IPage<Scmformtypes> pageList = scmformtypesService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmformtypes
	 * @return
	 */
	@AutoLog(value = "控件列描述-添加")
	@ApiOperation(value="控件列描述-添加", notes="控件列描述-添加")
	//@RequiresPermissions("org.br.erp:scmformtypes:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmformtypes scmformtypes) {
		scmformtypesService.save(scmformtypes);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmformtypes
	 * @return
	 */
	@AutoLog(value = "控件列描述-编辑")
	@ApiOperation(value="控件列描述-编辑", notes="控件列描述-编辑")
	//@RequiresPermissions("org.br.erp:scmformtypes:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmformtypes scmformtypes) {
		scmformtypesService.updateById(scmformtypes);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "控件列描述-通过id删除")
	@ApiOperation(value="控件列描述-通过id删除", notes="控件列描述-通过id删除")
	//@RequiresPermissions("org.br.erp:scmformtypes:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmformtypesService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "控件列描述-批量删除")
	@ApiOperation(value="控件列描述-批量删除", notes="控件列描述-批量删除")
	//@RequiresPermissions("org.br.erp:scmformtypes:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmformtypesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "控件列描述-通过id查询")
	@ApiOperation(value="控件列描述-通过id查询", notes="控件列描述-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmformtypes> queryById(@RequestParam(name="id",required=true) String id) {
		Scmformtypes scmformtypes = scmformtypesService.getById(id);
		if(scmformtypes==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmformtypes);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmformtypes
    */
    //@RequiresPermissions("org.br.erp:scmformtypes:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmformtypes scmformtypes) {
        return super.exportXls(request, scmformtypes, Scmformtypes.class, "控件列描述");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmformtypes:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmformtypes.class);
    }

}
