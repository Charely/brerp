package org.br.erp.base.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xkcoding.http.support.Http;
import org.br.erp.base.utils.ERPUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.base.entity.Scmprintdatasource;
import org.br.erp.base.service.IScmprintdatasourceService;

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
 * @Date:   2022-12-17
 * @Version: V1.0
 */
@Api(tags="打印数据源")
@RestController
@RequestMapping("/base/scmprintdatasource")
@Slf4j
public class ScmprintdatasourceController extends JeecgController<Scmprintdatasource, IScmprintdatasourceService> {
	@Autowired
	private IScmprintdatasourceService scmprintdatasourceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmprintdatasource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "打印数据源-分页列表查询")
	@ApiOperation(value="打印数据源-分页列表查询", notes="打印数据源-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmprintdatasource>> queryPageList(Scmprintdatasource scmprintdatasource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmprintdatasource> queryWrapper = QueryGenerator.initQueryWrapper(scmprintdatasource, req.getParameterMap());
		Page<Scmprintdatasource> page = new Page<Scmprintdatasource>(pageNo, pageSize);
		IPage<Scmprintdatasource> pageList = scmprintdatasourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	@GetMapping(value = "/queryprintdatasourcebyobjectcode")
	public Result<List<Scmprintdatasource>> queryPrintDatasourceByObjectCode(HttpServletRequest req){
		Map<String, String[]> parameterMap = req.getParameterMap();
		List<Scmprintdatasource> scmprintdatasources = scmprintdatasourceService.queryPrintDataSourceByObjectCode(parameterMap);
		return Result.ok(scmprintdatasources);
	}

	@GetMapping("/queryprintdatabyobjectcode")
	public Result<JSONArray> queryPrintDataSourceByObjectCodejson(HttpServletRequest req){
		Map<String, String[]> parameterMap = req.getParameterMap();
		String objectcode = ERPUtils.getHttpReqParam(parameterMap, "objectcode");
		String ids = ERPUtils.getHttpReqParam(parameterMap, "ids");
		JSONArray jsonArray = scmprintdatasourceService.queryPrintDataByObject(objectcode, ids);
		if(jsonArray==null || jsonArray.size() == 0){
			return Result.error("不存在打印格式");
		}else{
			return Result.ok(jsonArray);
		}
	}


	@GetMapping(value = "/queryprintdatasourcebyobjectcodenew")
	public Result<List<JSONObject>> queryPrintDatasourceByObjectCodeNew(HttpServletRequest req){
		Map<String, String[]> parameterMap = req.getParameterMap();
		//String objectcode = ERPUtils.getHttpReqParam(parameterMap, "objectcode");
		List<JSONObject> res=new ArrayList<>();
		List<Scmprintdatasource> scmprintdatasources = scmprintdatasourceService.queryPrintDataSourceByObjectCode(parameterMap);
		if(scmprintdatasources!=null && scmprintdatasources.size()>0){
			Scmprintdatasource scmprintdatasource = scmprintdatasources.get(0);
			String printdatasource = scmprintdatasource.getPrintdatasource();
			JSONObject jsonObject = (JSONObject) JSONObject.parse(printdatasource);
			JSONObject mainJson = (JSONObject) jsonObject.get("main");
			if(mainJson!=null ){
				for (String key : mainJson.keySet()) {
					if(key!="keyid" && key!="datasource"){
						JSONObject curObject=new JSONObject();
						curObject.put("tid","aProviderModule."+key);
						curObject.put("title",mainJson.get(key));
						curObject.put("data","");
						curObject.put("type","text");
						res.add(curObject);
					}
				}
			}
		}
		return Result.ok(res);
	}
	/**
	 *   添加
	 *
	 * @param scmprintdatasource
	 * @return
	 */
	@AutoLog(value = "打印数据源-添加")
	@ApiOperation(value="打印数据源-添加", notes="打印数据源-添加")
	//@RequiresPermissions("org.br.erp:scmprintdatasource:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmprintdatasource scmprintdatasource) {
		scmprintdatasourceService.save(scmprintdatasource);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmprintdatasource
	 * @return
	 */
	@AutoLog(value = "打印数据源-编辑")
	@ApiOperation(value="打印数据源-编辑", notes="打印数据源-编辑")
	//@RequiresPermissions("org.br.erp:scmprintdatasource:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmprintdatasource scmprintdatasource) {
		scmprintdatasourceService.updateById(scmprintdatasource);
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
	//@RequiresPermissions("org.br.erp:scmprintdatasource:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmprintdatasourceService.removeById(id);
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
	//@RequiresPermissions("org.br.erp:scmprintdatasource:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmprintdatasourceService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Scmprintdatasource> queryById(@RequestParam(name="id",required=true) String id) {
		Scmprintdatasource scmprintdatasource = scmprintdatasourceService.getById(id);
		if(scmprintdatasource==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmprintdatasource);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmprintdatasource
    */
    //@RequiresPermissions("org.br.erp:scmprintdatasource:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmprintdatasource scmprintdatasource) {
        return super.exportXls(request, scmprintdatasource, Scmprintdatasource.class, "打印数据源");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmprintdatasource:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmprintdatasource.class);
    }

}
