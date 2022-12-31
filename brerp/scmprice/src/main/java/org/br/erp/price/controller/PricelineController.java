package org.br.erp.price.controller;

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
import org.br.erp.price.entity.Priceline;
import org.br.erp.price.service.IPricelineService;

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
 * @Description: 价格管理
 * @Author: jeecg-boot
 * @Date:   2022-08-28
 * @Version: V1.0
 */
@Api(tags="价格管理")
@RestController
@RequestMapping("/price/priceline")
@Slf4j
public class PricelineController extends JeecgController<Priceline, IPricelineService> {
	@Autowired
	private IPricelineService pricelineService;
	
	/**
	 * 分页列表查询
	 *
	 * @param priceline
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "价格管理-分页列表查询")
	@ApiOperation(value="价格管理-分页列表查询", notes="价格管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Priceline>> queryPageList(Priceline priceline,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Priceline> queryWrapper = QueryGenerator.initQueryWrapper(priceline, req.getParameterMap());
		Page<Priceline> page = new Page<Priceline>(pageNo, pageSize);
		IPage<Priceline> pageList = pricelineService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param priceline
	 * @return
	 */
	@AutoLog(value = "价格管理-添加")
	@ApiOperation(value="价格管理-添加", notes="价格管理-添加")
	//@RequiresPermissions("org.br.erp:priceline:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Priceline priceline) {
		pricelineService.save(priceline);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param priceline
	 * @return
	 */
	@AutoLog(value = "价格管理-编辑")
	@ApiOperation(value="价格管理-编辑", notes="价格管理-编辑")
	//@RequiresPermissions("org.br.erp:priceline:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Priceline priceline) {
		pricelineService.updateById(priceline);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "价格管理-通过id删除")
	@ApiOperation(value="价格管理-通过id删除", notes="价格管理-通过id删除")
	//@RequiresPermissions("org.br.erp:priceline:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		pricelineService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "价格管理-批量删除")
	@ApiOperation(value="价格管理-批量删除", notes="价格管理-批量删除")
	//@RequiresPermissions("org.br.erp:priceline:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.pricelineService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "价格管理-通过id查询")
	@ApiOperation(value="价格管理-通过id查询", notes="价格管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Priceline> queryById(@RequestParam(name="id",required=true) String id) {
		Priceline priceline = pricelineService.getById(id);
		if(priceline==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(priceline);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param priceline
    */
    //@RequiresPermissions("org.br.erp:priceline:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Priceline priceline) {
        return super.exportXls(request, priceline, Priceline.class, "价格管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("priceline:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Priceline.class);
    }

}
