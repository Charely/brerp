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
import org.br.erp.base.entity.Scmstocktype;
import org.br.erp.base.service.IScmstocktypeService;

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
 * @Description: 库存状态
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Api(tags="库存状态")
@RestController
@RequestMapping("/base/scmstocktype")
@Slf4j
public class ScmstocktypeController extends JeecgController<Scmstocktype, IScmstocktypeService> {
	@Autowired
	private IScmstocktypeService scmstocktypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmstocktype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存状态-分页列表查询")
	@ApiOperation(value="库存状态-分页列表查询", notes="库存状态-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmstocktype>> queryPageList(Scmstocktype scmstocktype,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmstocktype> queryWrapper = QueryGenerator.initQueryWrapper(scmstocktype, req.getParameterMap());
		Page<Scmstocktype> page = new Page<Scmstocktype>(pageNo, pageSize);
		queryWrapper.orderByDesc("stockcode");
		IPage<Scmstocktype> pageList = scmstocktypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmstocktype
	 * @return
	 */
	@AutoLog(value = "库存状态-添加")
	@ApiOperation(value="库存状态-添加", notes="库存状态-添加")
	//@RequiresPermissions("org.br.erp:scmstocktype:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmstocktype scmstocktype) {
		//scmstocktypeService.saveMain(scmstocktype);
		scmstocktypeService.save(scmstocktype);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmstocktype
	 * @return
	 */
	@AutoLog(value = "库存状态-编辑")
	@ApiOperation(value="库存状态-编辑", notes="库存状态-编辑")
	//@RequiresPermissions("org.br.erp:scmstocktype:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmstocktype scmstocktype) {
		scmstocktypeService.updateById(scmstocktype);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存状态-通过id删除")
	@ApiOperation(value="库存状态-通过id删除", notes="库存状态-通过id删除")
	//@RequiresPermissions("org.br.erp:scmstocktype:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmstocktypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存状态-批量删除")
	@ApiOperation(value="库存状态-批量删除", notes="库存状态-批量删除")
	//@RequiresPermissions("org.br.erp:scmstocktype:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmstocktypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存状态-通过id查询")
	@ApiOperation(value="库存状态-通过id查询", notes="库存状态-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmstocktype> queryById(@RequestParam(name="id",required=true) String id) {
		Scmstocktype scmstocktype = scmstocktypeService.getById(id);
		if(scmstocktype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmstocktype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmstocktype
    */
    //@RequiresPermissions("org.br.erp:scmstocktype:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmstocktype scmstocktype) {
        return super.exportXls(request, scmstocktype, Scmstocktype.class, "库存状态");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmstocktype:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmstocktype.class);
    }

}
