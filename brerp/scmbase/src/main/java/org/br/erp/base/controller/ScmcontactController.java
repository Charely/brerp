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
import org.br.erp.base.entity.Scmcontact;
import org.br.erp.base.service.IScmcontactService;

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
 * @Description: 联系人
 * @Author: jeecg-boot
 * @Date:   2022-11-19
 * @Version: V1.0
 */
@Api(tags="联系人")
@RestController
@RequestMapping("/base/scmcontact")
@Slf4j
public class ScmcontactController extends JeecgController<Scmcontact, IScmcontactService> {
	@Autowired
	private IScmcontactService scmcontactService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmcontact
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "联系人-分页列表查询")
	@ApiOperation(value="联系人-分页列表查询", notes="联系人-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmcontact>> queryPageList(Scmcontact scmcontact,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmcontact> queryWrapper = QueryGenerator.initQueryWrapper(scmcontact, req.getParameterMap());
		Page<Scmcontact> page = new Page<Scmcontact>(pageNo, pageSize);
		IPage<Scmcontact> pageList = scmcontactService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmcontact
	 * @return
	 */
	@AutoLog(value = "联系人-添加")
	@ApiOperation(value="联系人-添加", notes="联系人-添加")
	//@RequiresPermissions("org.br.erp:scmcontact:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmcontact scmcontact) {
		scmcontactService.save(scmcontact);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmcontact
	 * @return
	 */
	@AutoLog(value = "联系人-编辑")
	@ApiOperation(value="联系人-编辑", notes="联系人-编辑")
	//@RequiresPermissions("org.br.erp:scmcontact:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmcontact scmcontact) {
		scmcontactService.updateById(scmcontact);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "联系人-通过id删除")
	@ApiOperation(value="联系人-通过id删除", notes="联系人-通过id删除")
	//@RequiresPermissions("org.br.erp:scmcontact:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmcontactService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "联系人-批量删除")
	@ApiOperation(value="联系人-批量删除", notes="联系人-批量删除")
	//@RequiresPermissions("org.br.erp:scmcontact:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmcontactService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "联系人-通过id查询")
	@ApiOperation(value="联系人-通过id查询", notes="联系人-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmcontact> queryById(@RequestParam(name="id",required=true) String id) {
		Scmcontact scmcontact = scmcontactService.getById(id);
		if(scmcontact==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmcontact);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmcontact
    */
    //@RequiresPermissions("org.br.erp:scmcontact:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmcontact scmcontact) {
        return super.exportXls(request, scmcontact, Scmcontact.class, "联系人");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmcontact:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmcontact.class);
    }

}
