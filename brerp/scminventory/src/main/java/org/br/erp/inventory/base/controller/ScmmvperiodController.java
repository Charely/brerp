package org.br.erp.inventory.base.controller;

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
import org.br.erp.inventory.base.entity.Scmmvperiod;
import org.br.erp.inventory.base.service.IScmmvperiodService;

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
 * @Description: 库存期间表
 * @Author: jeecg-boot
 * @Date:   2022-11-14
 * @Version: V1.0
 */
@Api(tags="库存期间表")
@RestController
@RequestMapping("/inventory.base/scmmvperiod")
@Slf4j
public class ScmmvperiodController extends JeecgController<Scmmvperiod, IScmmvperiodService> {
	@Autowired
	private IScmmvperiodService scmmvperiodService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmmvperiod
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存期间表-分页列表查询")
	@ApiOperation(value="库存期间表-分页列表查询", notes="库存期间表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmmvperiod>> queryPageList(Scmmvperiod scmmvperiod,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmmvperiod> queryWrapper = QueryGenerator.initQueryWrapper(scmmvperiod, req.getParameterMap());
		Page<Scmmvperiod> page = new Page<Scmmvperiod>(pageNo, pageSize);
		IPage<Scmmvperiod> pageList = scmmvperiodService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmmvperiod
	 * @return
	 */
	@AutoLog(value = "库存期间表-添加")
	@ApiOperation(value="库存期间表-添加", notes="库存期间表-添加")
	//@RequiresPermissions("org.br.erp:scmmvperiod:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmmvperiod scmmvperiod) {
		scmmvperiodService.save(scmmvperiod);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmmvperiod
	 * @return
	 */
	@AutoLog(value = "库存期间表-编辑")
	@ApiOperation(value="库存期间表-编辑", notes="库存期间表-编辑")
	//@RequiresPermissions("org.br.erp:scmmvperiod:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmmvperiod scmmvperiod) {
		scmmvperiodService.updateById(scmmvperiod);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存期间表-通过id删除")
	@ApiOperation(value="库存期间表-通过id删除", notes="库存期间表-通过id删除")
	//@RequiresPermissions("org.br.erp:scmmvperiod:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmmvperiodService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存期间表-批量删除")
	@ApiOperation(value="库存期间表-批量删除", notes="库存期间表-批量删除")
	//@RequiresPermissions("org.br.erp:scmmvperiod:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmmvperiodService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存期间表-通过id查询")
	@ApiOperation(value="库存期间表-通过id查询", notes="库存期间表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmmvperiod> queryById(@RequestParam(name="id",required=true) String id) {
		Scmmvperiod scmmvperiod = scmmvperiodService.getById(id);
		if(scmmvperiod==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmmvperiod);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmmvperiod
    */
    //@RequiresPermissions("org.br.erp:scmmvperiod:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmmvperiod scmmvperiod) {
        return super.exportXls(request, scmmvperiod, Scmmvperiod.class, "库存期间表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmmvperiod:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmmvperiod.class);
    }

}
