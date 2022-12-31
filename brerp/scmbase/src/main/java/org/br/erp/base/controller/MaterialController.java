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
import javax.websocket.server.PathParam;

import com.alibaba.fastjson.JSONObject;
import org.br.erp.base.entity.Materialpo;
import org.br.erp.base.entity.Materialsale;
import org.br.erp.base.entity.Materialstock;
import org.br.erp.base.vo.MaterialPage;
import org.br.erp.base.vo.MaterialVo;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.base.entity.Material;
import org.br.erp.base.service.IMaterialService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: 产品表
 * @Author: jeecg-boot
 * @Date:   2022-08-20
 * @Version: V1.0
 */
@Api(tags="产品表")
@RestController
@RequestMapping("/base/material")
@Slf4j
public class MaterialController extends JeecgController<Material, IMaterialService> {
	@Autowired
	private IMaterialService materialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param material
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "产品表-分页列表查询")
	@ApiOperation(value="产品表-分页列表查询", notes="产品表-分页列表查询")
	@GetMapping(value = {"/list/{materialtype}","/list"})
	public Result<IPage<Material>> queryPageList(Material material,
												 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												 HttpServletRequest req,
												 @PathVariable(required = false) String materialtype) {
		QueryWrapper<Material> queryWrapper = QueryGenerator.initQueryWrapper(material, req.getParameterMap());
		if(materialtype!=null){
			if(materialtype.equalsIgnoreCase("0")){
				queryWrapper.eq("ispo","true");
			}else if(materialtype.equalsIgnoreCase("1")){
				queryWrapper.eq("issale","true");
			}else if(materialtype.equalsIgnoreCase("2")){
				queryWrapper.eq("isgd","true");
			}
		}
		Page<Material> page = new Page<Material>(pageNo, pageSize);
		IPage<Material> pageList = materialService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	@RequestMapping("/querymaterialsalebyparentid")
	public Result<List<Materialsale>> querymaterialsalebyparentid(String id){
		List<Materialsale> materisaleByMaterialid = materialService.getMaterisaleByMaterialid(id);
		return Result.OK(materisaleByMaterialid);
	}

	 @RequestMapping("/querymaterialpobyparentid")
	public Result<List<Materialpo>> querymaterialbpobyparentid(String id){
		List<Materialpo> materialpoByMaterialid = materialService.getMaterialpoByMaterialid(id);
		return Result.ok(materialpoByMaterialid);
	}

	 @RequestMapping("/querymaterialstockbyparentid")
	 public Result<List<Materialstock>> querymaterialstockbyparentid(String id){
		 List<Materialstock> materialpoByMaterialid = materialService.getMaterialstockByMaterialid(id);
		 return Result.ok(materialpoByMaterialid);
	 }
	/**
	 *   添加
	 *
	 * @param material
	 * @return
	 */
	@AutoLog(value = "产品表-添加")
	@ApiOperation(value="产品表-添加", notes="产品表-添加")
	//@RequiresPermissions("org.br.erp:material:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody MaterialPage materialPage) {
		materialService.saveMainObject(materialPage);
		return Result.OK("添加成功！");
	}


	@GetMapping("/getmaterialinfobymaterialid")
	public Result<MaterialVo> getMaterialInfoByMaterialid(@RequestParam(name="materialid") String materialid){
		MaterialVo materialVoInfoByMaterialID = materialService.getMaterialVoInfoByMaterialID(materialid);
		return Result.ok(materialVoInfoByMaterialID);
	}
	
	/**
	 *  编辑
	 *
	 * @param material
	 * @return
	 */
	@AutoLog(value = "产品表-编辑")
	@ApiOperation(value="产品表-编辑", notes="产品表-编辑")
	//@RequiresPermissions("org.br.erp:material:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody MaterialPage materialPage) {
		materialService.updateByMainId(materialPage);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "产品表-通过id删除")
	@ApiOperation(value="产品表-通过id删除", notes="产品表-通过id删除")
	//@RequiresPermissions("org.br.erp:material:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		materialService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "产品表-批量删除")
	@ApiOperation(value="产品表-批量删除", notes="产品表-批量删除")
	//@RequiresPermissions("org.br.erp:material:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.materialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "产品表-通过id查询")
	@ApiOperation(value="产品表-通过id查询", notes="产品表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Material> queryById(@RequestParam(name="id",required=true) String id) {
		Material material = materialService.getById(id);
		if(material==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(material);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param material
    */
    //@RequiresPermissions("org.br.erp:material:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Material material) {
        return super.exportXls(request, material, Material.class, "产品表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("material:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Material.class);
    }

	@RequestMapping(value = "/getmaterialbymaterialtypeid",method = RequestMethod.GET)
	public Result<List<Material>> getMaterialByMaterialTypeid(@RequestParam(value = "code") String code){
		List<Material> materialByMatrerialTypeid = materialService.getMaterialByMatrerialTypeid(code);
		return Result.OK(materialByMatrerialTypeid);
	}


	 @RequestMapping(value = "/getmaterialbymaterialid",method = RequestMethod.GET)
	public Result<Material> getMaterialByMaterialId(@RequestParam(value = "materialid")String materialid){
		Material material = materialService.getMaterialbyMaterialId(materialid);
		return Result.OK(material);
	}


	@PostMapping("/updatestatus")
	public Result<String> updatestatus(@RequestBody String params){
		JSONObject jsonObject = JSON.parseObject(params);
		String ids = jsonObject.get("ids").toString();
		String statusflag = jsonObject.get("statusflag").toString();
		String[] split = ids.split(",");
		materialService.updatestatus(Arrays.asList(split),statusflag);
		return Result.ok("更新成功");
	}
}
