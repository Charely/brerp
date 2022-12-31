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
import org.br.erp.base.entity.Scmorgwarelink;
import org.br.erp.base.service.IScmorgwarelinkService;

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
 * @Description: 仓库物料安全库存定义
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Api(tags="仓库物料安全库存定义")
@RestController
@RequestMapping("/base/scmorgwarelink")
@Slf4j
public class ScmorgwarelinkController extends JeecgController<Scmorgwarelink, IScmorgwarelinkService> {
	@Autowired
	private IScmorgwarelinkService scmorgwarelinkService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmorgwarelink
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "仓库物料安全库存定义-分页列表查询")
	@ApiOperation(value="仓库物料安全库存定义-分页列表查询", notes="仓库物料安全库存定义-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmorgwarelink>> queryPageList(Scmorgwarelink scmorgwarelink,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmorgwarelink> queryWrapper = QueryGenerator.initQueryWrapper(scmorgwarelink, req.getParameterMap());
		Page<Scmorgwarelink> page = new Page<Scmorgwarelink>(pageNo, pageSize);
		IPage<Scmorgwarelink> pageList = scmorgwarelinkService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmorgwarelink
	 * @return
	 */
	@AutoLog(value = "仓库物料安全库存定义-添加")
	@ApiOperation(value="仓库物料安全库存定义-添加", notes="仓库物料安全库存定义-添加")
	//@RequiresPermissions("org.br.erp:scmorgwarelink:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmorgwarelink scmorgwarelink) {
		scmorgwarelinkService.save(scmorgwarelink);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmorgwarelink
	 * @return
	 */
	@AutoLog(value = "仓库物料安全库存定义-编辑")
	@ApiOperation(value="仓库物料安全库存定义-编辑", notes="仓库物料安全库存定义-编辑")
	//@RequiresPermissions("org.br.erp:scmorgwarelink:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmorgwarelink scmorgwarelink) {
		scmorgwarelinkService.updateById(scmorgwarelink);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "仓库物料安全库存定义-通过id删除")
	@ApiOperation(value="仓库物料安全库存定义-通过id删除", notes="仓库物料安全库存定义-通过id删除")
	//@RequiresPermissions("org.br.erp:scmorgwarelink:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmorgwarelinkService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "仓库物料安全库存定义-批量删除")
	@ApiOperation(value="仓库物料安全库存定义-批量删除", notes="仓库物料安全库存定义-批量删除")
	//@RequiresPermissions("org.br.erp:scmorgwarelink:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmorgwarelinkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "仓库物料安全库存定义-通过id查询")
	@ApiOperation(value="仓库物料安全库存定义-通过id查询", notes="仓库物料安全库存定义-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmorgwarelink> queryById(@RequestParam(name="id",required=true) String id) {
		Scmorgwarelink scmorgwarelink = scmorgwarelinkService.getById(id);
		if(scmorgwarelink==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmorgwarelink);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmorgwarelink
    */
    //@RequiresPermissions("org.br.erp:scmorgwarelink:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmorgwarelink scmorgwarelink) {
        return super.exportXls(request, scmorgwarelink, Scmorgwarelink.class, "仓库物料安全库存定义");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmorgwarelink:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmorgwarelink.class);
    }

}
