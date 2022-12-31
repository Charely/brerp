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
import org.br.erp.base.entity.Scminventorykinds;
import org.br.erp.base.service.IScminventorykindsService;

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
 * @Description: 库存类型
 * @Author: jeecg-boot
 * @Date:   2022-10-27
 * @Version: V1.0
 */
@Api(tags="库存类型")
@RestController
@RequestMapping("/base/scminventorykinds")
@Slf4j
public class ScminventorykindsController extends JeecgController<Scminventorykinds, IScminventorykindsService> {
	@Autowired
	private IScminventorykindsService scminventorykindsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scminventorykinds
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存类型-分页列表查询")
	@ApiOperation(value="库存类型-分页列表查询", notes="库存类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scminventorykinds>> queryPageList(Scminventorykinds scminventorykinds,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scminventorykinds> queryWrapper = QueryGenerator.initQueryWrapper(scminventorykinds, req.getParameterMap());
		Page<Scminventorykinds> page = new Page<Scminventorykinds>(pageNo, pageSize);
		IPage<Scminventorykinds> pageList = scminventorykindsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scminventorykinds
	 * @return
	 */
	@AutoLog(value = "库存类型-添加")
	@ApiOperation(value="库存类型-添加", notes="库存类型-添加")
	//@RequiresPermissions("org.br.erp:scminventorykinds:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scminventorykinds scminventorykinds) {
		scminventorykindsService.save(scminventorykinds);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scminventorykinds
	 * @return
	 */
	@AutoLog(value = "库存类型-编辑")
	@ApiOperation(value="库存类型-编辑", notes="库存类型-编辑")
	//@RequiresPermissions("org.br.erp:scminventorykinds:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scminventorykinds scminventorykinds) {
		scminventorykindsService.updateById(scminventorykinds);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存类型-通过id删除")
	@ApiOperation(value="库存类型-通过id删除", notes="库存类型-通过id删除")
	//@RequiresPermissions("org.br.erp:scminventorykinds:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scminventorykindsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存类型-批量删除")
	@ApiOperation(value="库存类型-批量删除", notes="库存类型-批量删除")
	//@RequiresPermissions("org.br.erp:scminventorykinds:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scminventorykindsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存类型-通过id查询")
	@ApiOperation(value="库存类型-通过id查询", notes="库存类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scminventorykinds> queryById(@RequestParam(name="id",required=true) String id) {
		Scminventorykinds scminventorykinds = scminventorykindsService.getById(id);
		if(scminventorykinds==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scminventorykinds);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scminventorykinds
    */
    //@RequiresPermissions("org.br.erp:scminventorykinds:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scminventorykinds scminventorykinds) {
        return super.exportXls(request, scminventorykinds, Scminventorykinds.class, "库存类型");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scminventorykinds:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scminventorykinds.class);
    }

}
