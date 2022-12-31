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
import org.br.erp.base.entity.Scmbillcoderule;
import org.br.erp.base.service.IScmbillcoderuleService;

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
 * @Description: 单据编号规则表
 * @Author: jeecg-boot
 * @Date:   2022-10-12
 * @Version: V1.0
 */
@Api(tags="单据编号规则表")
@RestController
@RequestMapping("/base/scmbillcoderule")
@Slf4j
public class ScmbillcoderuleController extends JeecgController<Scmbillcoderule, IScmbillcoderuleService> {
	@Autowired
	private IScmbillcoderuleService scmbillcoderuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmbillcoderule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "单据编号规则表-分页列表查询")
	@ApiOperation(value="单据编号规则表-分页列表查询", notes="单据编号规则表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmbillcoderule>> queryPageList(Scmbillcoderule scmbillcoderule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmbillcoderule> queryWrapper = QueryGenerator.initQueryWrapper(scmbillcoderule, req.getParameterMap());
		Page<Scmbillcoderule> page = new Page<Scmbillcoderule>(pageNo, pageSize);
		IPage<Scmbillcoderule> pageList = scmbillcoderuleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmbillcoderule
	 * @return
	 */
	@AutoLog(value = "单据编号规则表-添加")
	@ApiOperation(value="单据编号规则表-添加", notes="单据编号规则表-添加")
	//@RequiresPermissions("org.br.erp:scmbillcoderule:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmbillcoderule scmbillcoderule) {
		scmbillcoderuleService.save(scmbillcoderule);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmbillcoderule
	 * @return
	 */
	@AutoLog(value = "单据编号规则表-编辑")
	@ApiOperation(value="单据编号规则表-编辑", notes="单据编号规则表-编辑")
	//@RequiresPermissions("org.br.erp:scmbillcoderule:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmbillcoderule scmbillcoderule) {
		scmbillcoderuleService.updateById(scmbillcoderule);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "单据编号规则表-通过id删除")
	@ApiOperation(value="单据编号规则表-通过id删除", notes="单据编号规则表-通过id删除")
	//@RequiresPermissions("org.br.erp:scmbillcoderule:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmbillcoderuleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "单据编号规则表-批量删除")
	@ApiOperation(value="单据编号规则表-批量删除", notes="单据编号规则表-批量删除")
	//@RequiresPermissions("org.br.erp:scmbillcoderule:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmbillcoderuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "单据编号规则表-通过id查询")
	@ApiOperation(value="单据编号规则表-通过id查询", notes="单据编号规则表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmbillcoderule> queryById(@RequestParam(name="id",required=true) String id) {
		Scmbillcoderule scmbillcoderule = scmbillcoderuleService.getById(id);
		if(scmbillcoderule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmbillcoderule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmbillcoderule
    */
    //@RequiresPermissions("org.br.erp:scmbillcoderule:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmbillcoderule scmbillcoderule) {
        return super.exportXls(request, scmbillcoderule, Scmbillcoderule.class, "单据编号规则表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmbillcoderule:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmbillcoderule.class);
    }

}
