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
import org.br.erp.base.entity.Currency;
import org.br.erp.base.service.ICurrencyService;

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
 * @Description: 币种
 * @Author: jeecg-boot
 * @Date:   2022-11-18
 * @Version: V1.0
 */
@Api(tags="币种")
@RestController
@RequestMapping("/base/currency")
@Slf4j
public class CurrencyController extends JeecgController<Currency, ICurrencyService> {
	@Autowired
	private ICurrencyService currencyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param currency
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "币种-分页列表查询")
	@ApiOperation(value="币种-分页列表查询", notes="币种-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Currency>> queryPageList(Currency currency,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Currency> queryWrapper = QueryGenerator.initQueryWrapper(currency, req.getParameterMap());
		Page<Currency> page = new Page<Currency>(pageNo, pageSize);
		IPage<Currency> pageList = currencyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param currency
	 * @return
	 */
	@AutoLog(value = "币种-添加")
	@ApiOperation(value="币种-添加", notes="币种-添加")
	//@RequiresPermissions("org.br.erp:currency:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Currency currency) {
		currencyService.save(currency);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param currency
	 * @return
	 */
	@AutoLog(value = "币种-编辑")
	@ApiOperation(value="币种-编辑", notes="币种-编辑")
	//@RequiresPermissions("org.br.erp:currency:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Currency currency) {
		currencyService.updateById(currency);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "币种-通过id删除")
	@ApiOperation(value="币种-通过id删除", notes="币种-通过id删除")
	//@RequiresPermissions("org.br.erp:currency:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		currencyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "币种-批量删除")
	@ApiOperation(value="币种-批量删除", notes="币种-批量删除")
	//@RequiresPermissions("org.br.erp:currency:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.currencyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "币种-通过id查询")
	@ApiOperation(value="币种-通过id查询", notes="币种-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Currency> queryById(@RequestParam(name="id",required=true) String id) {
		Currency currency = currencyService.getById(id);
		if(currency==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(currency);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param currency
    */
    //@RequiresPermissions("org.br.erp:currency:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Currency currency) {
        return super.exportXls(request, currency, Currency.class, "币种");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("currency:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Currency.class);
    }

}
