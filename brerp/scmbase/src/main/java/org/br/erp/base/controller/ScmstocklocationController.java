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
import org.br.erp.base.entity.Scmstocklocation;
import org.br.erp.base.service.IScmstocklocationService;

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
 * @Description: 货位管理
 * @Author: jeecg-boot
 * @Date:   2022-08-31
 * @Version: V1.0
 */
@Api(tags="货位管理")
@RestController
@RequestMapping("/base/scmstocklocation")
@Slf4j
public class ScmstocklocationController extends JeecgController<Scmstocklocation, IScmstocklocationService> {
	@Autowired
	private IScmstocklocationService scmstocklocationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmstocklocation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "货位管理-分页列表查询")
	@ApiOperation(value="货位管理-分页列表查询", notes="货位管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmstocklocation>> queryPageList(Scmstocklocation scmstocklocation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmstocklocation> queryWrapper = QueryGenerator.initQueryWrapper(scmstocklocation, req.getParameterMap());
		Page<Scmstocklocation> page = new Page<Scmstocklocation>(pageNo, pageSize);
		IPage<Scmstocklocation> pageList = scmstocklocationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmstocklocation
	 * @return
	 */
	@AutoLog(value = "货位管理-添加")
	@ApiOperation(value="货位管理-添加", notes="货位管理-添加")
	//@RequiresPermissions("org.br.erp:scmstocklocation:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmstocklocation scmstocklocation) {
		scmstocklocationService.save(scmstocklocation);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmstocklocation
	 * @return
	 */
	@AutoLog(value = "货位管理-编辑")
	@ApiOperation(value="货位管理-编辑", notes="货位管理-编辑")
	//@RequiresPermissions("org.br.erp:scmstocklocation:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmstocklocation scmstocklocation) {
		scmstocklocationService.updateById(scmstocklocation);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "货位管理-通过id删除")
	@ApiOperation(value="货位管理-通过id删除", notes="货位管理-通过id删除")
	//@RequiresPermissions("org.br.erp:scmstocklocation:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmstocklocationService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "货位管理-批量删除")
	@ApiOperation(value="货位管理-批量删除", notes="货位管理-批量删除")
	//@RequiresPermissions("org.br.erp:scmstocklocation:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmstocklocationService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "货位管理-通过id查询")
	@ApiOperation(value="货位管理-通过id查询", notes="货位管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmstocklocation> queryById(@RequestParam(name="id",required=true) String id) {
		Scmstocklocation scmstocklocation = scmstocklocationService.getById(id);
		if(scmstocklocation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmstocklocation);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmstocklocation
    */
    //@RequiresPermissions("org.br.erp:scmstocklocation:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmstocklocation scmstocklocation) {
        return super.exportXls(request, scmstocklocation, Scmstocklocation.class, "货位管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmstocklocation:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmstocklocation.class);
    }

}
