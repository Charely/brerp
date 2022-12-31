package org.br.erp.mv.controller;

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
import org.br.erp.mv.entity.Scmmvbalance;
import org.br.erp.mv.service.IScmmvbalanceService;

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
 * @Description: 存货账本
 * @Author: jeecg-boot
 * @Date:   2022-12-29
 * @Version: V1.0
 */
@Api(tags="存货账本")
@RestController
@RequestMapping("/mv/scmmvbalance")
@Slf4j
public class ScmmvbalanceController extends JeecgController<Scmmvbalance, IScmmvbalanceService> {
	@Autowired
	private IScmmvbalanceService scmmvbalanceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmmvbalance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "存货账本-分页列表查询")
	@ApiOperation(value="存货账本-分页列表查询", notes="存货账本-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmmvbalance>> queryPageList(Scmmvbalance scmmvbalance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmmvbalance> queryWrapper = QueryGenerator.initQueryWrapper(scmmvbalance, req.getParameterMap());
		Page<Scmmvbalance> page = new Page<Scmmvbalance>(pageNo, pageSize);
		IPage<Scmmvbalance> pageList = scmmvbalanceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmmvbalance
	 * @return
	 */
	@AutoLog(value = "存货账本-添加")
	@ApiOperation(value="存货账本-添加", notes="存货账本-添加")
	//@RequiresPermissions("org.br.erp:scmmvbalance:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmmvbalance scmmvbalance) {
		scmmvbalanceService.save(scmmvbalance);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param scmmvbalance
	 * @return
	 */
	@AutoLog(value = "存货账本-编辑")
	@ApiOperation(value="存货账本-编辑", notes="存货账本-编辑")
	//@RequiresPermissions("org.br.erp:scmmvbalance:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmmvbalance scmmvbalance) {
		scmmvbalanceService.updateById(scmmvbalance);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "存货账本-通过id删除")
	@ApiOperation(value="存货账本-通过id删除", notes="存货账本-通过id删除")
	//@RequiresPermissions("org.br.erp:scmmvbalance:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmmvbalanceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "存货账本-批量删除")
	@ApiOperation(value="存货账本-批量删除", notes="存货账本-批量删除")
	//@RequiresPermissions("org.br.erp:scmmvbalance:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmmvbalanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "存货账本-通过id查询")
	@ApiOperation(value="存货账本-通过id查询", notes="存货账本-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmmvbalance> queryById(@RequestParam(name="id",required=true) String id) {
		Scmmvbalance scmmvbalance = scmmvbalanceService.getById(id);
		if(scmmvbalance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmmvbalance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmmvbalance
    */
    //@RequiresPermissions("org.br.erp:scmmvbalance:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmmvbalance scmmvbalance) {
        return super.exportXls(request, scmmvbalance, Scmmvbalance.class, "存货账本");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmmvbalance:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmmvbalance.class);
    }

}
