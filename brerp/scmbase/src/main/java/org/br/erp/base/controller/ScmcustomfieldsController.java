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
import org.br.erp.base.entity.Scmcustomfields;
import org.br.erp.base.service.IScmcustomfieldsService;

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
 * @Description: 自定义表字段
 * @Author: jeecg-boot
 * @Date:   2022-09-30
 * @Version: V1.0
 */
@Api(tags="自定义表字段")
@RestController
@RequestMapping("/base/scmcustomfields")
@Slf4j
public class ScmcustomfieldsController extends JeecgController<Scmcustomfields, IScmcustomfieldsService> {
	@Autowired
	private IScmcustomfieldsService scmcustomfieldsService;


	/*
	根据业务对象信息获取自定义字段信息
	 */
	 @RequestMapping(value = "/getcustomfieldsbyobjectcode",method = RequestMethod.GET)
	public Result<List<Scmcustomfields>> getCustomFieldsByObjectCode(@RequestParam("objectcode")String objectcode){
		List<Scmcustomfields> customfieldsByobjectcode = scmcustomfieldsService.getCustomfieldsByobjectcode(objectcode);
		if(customfieldsByobjectcode==null || customfieldsByobjectcode.size()==0){
			return Result.OK("");
		}else {
			return Result.OK(customfieldsByobjectcode);
		}

	}
	/**
	 * 分页列表查询
	 *
	 * @param scmcustomfields
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "自定义表字段-分页列表查询")
	@ApiOperation(value="自定义表字段-分页列表查询", notes="自定义表字段-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmcustomfields>> queryPageList(Scmcustomfields scmcustomfields,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmcustomfields> queryWrapper = QueryGenerator.initQueryWrapper(scmcustomfields, req.getParameterMap());
		Page<Scmcustomfields> page = new Page<Scmcustomfields>(pageNo, pageSize);
		IPage<Scmcustomfields> pageList = scmcustomfieldsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmcustomfields
	 * @return
	 */
	@AutoLog(value = "自定义表字段-添加")
	@ApiOperation(value="自定义表字段-添加", notes="自定义表字段-添加")
	//@RequiresPermissions("org.br.erp:scmcustomfields:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmcustomfields scmcustomfields) {
		scmcustomfieldsService.cusinsert(scmcustomfields);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmcustomfields
	 * @return
	 */
	@AutoLog(value = "自定义表字段-编辑")
	@ApiOperation(value="自定义表字段-编辑", notes="自定义表字段-编辑")
	//@RequiresPermissions("org.br.erp:scmcustomfields:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmcustomfields scmcustomfields) {
		scmcustomfieldsService.updateById(scmcustomfields);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自定义表字段-通过id删除")
	@ApiOperation(value="自定义表字段-通过id删除", notes="自定义表字段-通过id删除")
	//@RequiresPermissions("org.br.erp:scmcustomfields:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmcustomfieldsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "自定义表字段-批量删除")
	@ApiOperation(value="自定义表字段-批量删除", notes="自定义表字段-批量删除")
	//@RequiresPermissions("org.br.erp:scmcustomfields:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmcustomfieldsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "自定义表字段-通过id查询")
	@ApiOperation(value="自定义表字段-通过id查询", notes="自定义表字段-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmcustomfields> queryById(@RequestParam(name="id",required=true) String id) {
		Scmcustomfields scmcustomfields = scmcustomfieldsService.getById(id);
		if(scmcustomfields==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmcustomfields);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmcustomfields
    */
    //@RequiresPermissions("org.br.erp:scmcustomfields:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmcustomfields scmcustomfields) {
        return super.exportXls(request, scmcustomfields, Scmcustomfields.class, "自定义表字段");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmcustomfields:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmcustomfields.class);
    }

}
