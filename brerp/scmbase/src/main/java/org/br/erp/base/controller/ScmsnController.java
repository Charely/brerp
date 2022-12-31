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
import org.br.erp.base.entity.Scmsn;
import org.br.erp.base.service.IScmsnService;

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
 * @Description: 单件档案
 * @Author: jeecg-boot
 * @Date:   2022-09-07
 * @Version: V1.0
 */
@Api(tags="单件档案")
@RestController
@RequestMapping("/base/scmsn")
@Slf4j
public class ScmsnController extends JeecgController<Scmsn, IScmsnService> {
	@Autowired
	private IScmsnService scmsnService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmsn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "单件档案-分页列表查询")
	@ApiOperation(value="单件档案-分页列表查询", notes="单件档案-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmsn>> queryPageList(Scmsn scmsn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmsn> queryWrapper = QueryGenerator.initQueryWrapper(scmsn, req.getParameterMap());
		Page<Scmsn> page = new Page<Scmsn>(pageNo, pageSize);
		IPage<Scmsn> pageList = scmsnService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmsn
	 * @return
	 */
	@AutoLog(value = "单件档案-添加")
	@ApiOperation(value="单件档案-添加", notes="单件档案-添加")
	//@RequiresPermissions("org.br.erp:scmsn:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmsn scmsn) {
		scmsnService.save(scmsn);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmsn
	 * @return
	 */
	@AutoLog(value = "单件档案-编辑")
	@ApiOperation(value="单件档案-编辑", notes="单件档案-编辑")
	//@RequiresPermissions("org.br.erp:scmsn:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmsn scmsn) {
		scmsnService.updateById(scmsn);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "单件档案-通过id删除")
	@ApiOperation(value="单件档案-通过id删除", notes="单件档案-通过id删除")
	//@RequiresPermissions("org.br.erp:scmsn:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmsnService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "单件档案-批量删除")
	@ApiOperation(value="单件档案-批量删除", notes="单件档案-批量删除")
	//@RequiresPermissions("org.br.erp:scmsn:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmsnService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "单件档案-通过id查询")
	@ApiOperation(value="单件档案-通过id查询", notes="单件档案-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmsn> queryById(@RequestParam(name="id",required=true) String id) {
		Scmsn scmsn = scmsnService.getById(id);
		if(scmsn==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmsn);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmsn
    */
    //@RequiresPermissions("org.br.erp:scmsn:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmsn scmsn) {
        return super.exportXls(request, scmsn, Scmsn.class, "单件档案");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmsn:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmsn.class);
    }

}
