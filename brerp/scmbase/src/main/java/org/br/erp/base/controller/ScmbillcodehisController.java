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
import org.br.erp.base.entity.Scmbillcodehis;
import org.br.erp.base.service.IScmbillcodehisService;

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
 * @Description: 单据编号历史记录
 * @Author: jeecg-boot
 * @Date:   2022-10-14
 * @Version: V1.0
 */
@Api(tags="单据编号历史记录")
@RestController
@RequestMapping("/base/scmbillcodehis")
@Slf4j
public class ScmbillcodehisController extends JeecgController<Scmbillcodehis, IScmbillcodehisService> {
	@Autowired
	private IScmbillcodehisService scmbillcodehisService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmbillcodehis
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "单据编号历史记录-分页列表查询")
	@ApiOperation(value="单据编号历史记录-分页列表查询", notes="单据编号历史记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmbillcodehis>> queryPageList(Scmbillcodehis scmbillcodehis,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmbillcodehis> queryWrapper = QueryGenerator.initQueryWrapper(scmbillcodehis, req.getParameterMap());
		Page<Scmbillcodehis> page = new Page<Scmbillcodehis>(pageNo, pageSize);
		IPage<Scmbillcodehis> pageList = scmbillcodehisService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmbillcodehis
	 * @return
	 */
	@AutoLog(value = "单据编号历史记录-添加")
	@ApiOperation(value="单据编号历史记录-添加", notes="单据编号历史记录-添加")
	//@RequiresPermissions("org.br.erp:scmbillcodehis:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmbillcodehis scmbillcodehis) {
		scmbillcodehisService.save(scmbillcodehis);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmbillcodehis
	 * @return
	 */
	@AutoLog(value = "单据编号历史记录-编辑")
	@ApiOperation(value="单据编号历史记录-编辑", notes="单据编号历史记录-编辑")
	//@RequiresPermissions("org.br.erp:scmbillcodehis:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmbillcodehis scmbillcodehis) {
		scmbillcodehisService.updateById(scmbillcodehis);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "单据编号历史记录-通过id删除")
	@ApiOperation(value="单据编号历史记录-通过id删除", notes="单据编号历史记录-通过id删除")
	//@RequiresPermissions("org.br.erp:scmbillcodehis:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmbillcodehisService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "单据编号历史记录-批量删除")
	@ApiOperation(value="单据编号历史记录-批量删除", notes="单据编号历史记录-批量删除")
	//@RequiresPermissions("org.br.erp:scmbillcodehis:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmbillcodehisService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "单据编号历史记录-通过id查询")
	@ApiOperation(value="单据编号历史记录-通过id查询", notes="单据编号历史记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmbillcodehis> queryById(@RequestParam(name="id",required=true) String id) {
		Scmbillcodehis scmbillcodehis = scmbillcodehisService.getById(id);
		if(scmbillcodehis==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmbillcodehis);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmbillcodehis
    */
    //@RequiresPermissions("org.br.erp:scmbillcodehis:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmbillcodehis scmbillcodehis) {
        return super.exportXls(request, scmbillcodehis, Scmbillcodehis.class, "单据编号历史记录");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmbillcodehis:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmbillcodehis.class);
    }

}
