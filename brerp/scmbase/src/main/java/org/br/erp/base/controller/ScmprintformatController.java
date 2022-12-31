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
import org.br.erp.base.entity.Scmprintformat;
import org.br.erp.base.service.IScmprintformatService;

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
 * @Description: 打印数据源
 * @Author: jeecg-boot
 * @Date:   2022-12-18
 * @Version: V1.0
 */
@Api(tags="打印数据源")
@RestController
@RequestMapping("/base/scmprintformat")
@Slf4j
public class ScmprintformatController extends JeecgController<Scmprintformat, IScmprintformatService> {
	@Autowired
	private IScmprintformatService scmprintformatService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmprintformat
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "打印数据源-分页列表查询")
	@ApiOperation(value="打印数据源-分页列表查询", notes="打印数据源-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmprintformat>> queryPageList(Scmprintformat scmprintformat,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmprintformat> queryWrapper = QueryGenerator.initQueryWrapper(scmprintformat, req.getParameterMap());
		Page<Scmprintformat> page = new Page<Scmprintformat>(pageNo, pageSize);
		IPage<Scmprintformat> pageList = scmprintformatService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmprintformat
	 * @return
	 */
	@AutoLog(value = "打印数据源-添加")
	@ApiOperation(value="打印数据源-添加", notes="打印数据源-添加")
	//@RequiresPermissions("org.br.erp:scmprintformat:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmprintformat scmprintformat) {

		//scmprintformatService.save(scmprintformat);
		scmprintformatService.savefomrat(scmprintformat);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmprintformat
	 * @return
	 */
	@AutoLog(value = "打印数据源-编辑")
	@ApiOperation(value="打印数据源-编辑", notes="打印数据源-编辑")
	//@RequiresPermissions("org.br.erp:scmprintformat:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmprintformat scmprintformat) {
		scmprintformatService.updateById(scmprintformat);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "打印数据源-通过id删除")
	@ApiOperation(value="打印数据源-通过id删除", notes="打印数据源-通过id删除")
	//@RequiresPermissions("org.br.erp:scmprintformat:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmprintformatService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "打印数据源-批量删除")
	@ApiOperation(value="打印数据源-批量删除", notes="打印数据源-批量删除")
	//@RequiresPermissions("org.br.erp:scmprintformat:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmprintformatService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "打印数据源-通过id查询")
	@ApiOperation(value="打印数据源-通过id查询", notes="打印数据源-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmprintformat> queryById(@RequestParam(name="id",required=true) String id) {
		Scmprintformat scmprintformat = scmprintformatService.getById(id);
		if(scmprintformat==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmprintformat);
	}

	@GetMapping(value = "/querybyobject")
	public Result<Scmprintformat> queryByObjectCode(@RequestParam(name = "objectcode",required = true)String objectcode){
		Scmprintformat scmprintformat = scmprintformatService.queryPrintFormatByObjectCode(objectcode);
		if(scmprintformat==null){
			return Result.error("请先去定义对应的打印格式");
		}else{
			return Result.ok(scmprintformat);
		}
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmprintformat
    */
    //@RequiresPermissions("org.br.erp:scmprintformat:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmprintformat scmprintformat) {
        return super.exportXls(request, scmprintformat, Scmprintformat.class, "打印数据源");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmprintformat:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmprintformat.class);
    }

}
