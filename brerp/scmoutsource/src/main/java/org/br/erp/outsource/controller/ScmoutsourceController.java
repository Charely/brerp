package org.br.erp.outsource.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.br.erp.base.Config.EventHandler;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.br.erp.outsource.entity.Scmoutsourceitembom;
import org.br.erp.outsource.vo.ScmReceiptReqReferWwVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.br.erp.outsource.entity.Scmoutsourceitem;
import org.br.erp.outsource.entity.Scmoutsource;
import org.br.erp.outsource.vo.ScmoutsourcePage;
import org.br.erp.outsource.service.IScmoutsourceService;
import org.br.erp.outsource.service.IScmoutsourceitemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 委外订单
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Api(tags="委外订单")
@RestController
@RequestMapping("/outsource/scmoutsource")
@Slf4j
public class ScmoutsourceController {
	@Autowired
	private IScmoutsourceService scmoutsourceService;
	@Autowired
	private IScmoutsourceitemService scmoutsourceitemService;


	@Autowired
	private EventHandler eventHandler;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmoutsource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "委外订单-分页列表查询")
	@ApiOperation(value="委外订单-分页列表查询", notes="委外订单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmoutsource>> queryPageList(Scmoutsource scmoutsource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmoutsource> queryWrapper = QueryGenerator.initQueryWrapper(scmoutsource, req.getParameterMap());
		Page<Scmoutsource> page = new Page<Scmoutsource>(pageNo, pageSize);
		IPage<Scmoutsource> pageList = scmoutsourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmoutsourcePage
	 * @return
	 */
	@AutoLog(value = "委外订单-添加")
	@ApiOperation(value="委外订单-添加", notes="委外订单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmoutsourcePage scmoutsourcePage) {
		Scmoutsource scmoutsource = new Scmoutsource();
		BeanUtils.copyProperties(scmoutsourcePage, scmoutsource);
		scmoutsourceService.saveMain(scmoutsource, scmoutsourcePage.getScmoutsourceitemList(),scmoutsourcePage);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmoutsourcePage
	 * @return
	 */
	@AutoLog(value = "委外订单-编辑")
	@ApiOperation(value="委外订单-编辑", notes="委外订单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmoutsourcePage scmoutsourcePage) {
		Scmoutsource scmoutsource = new Scmoutsource();
		BeanUtils.copyProperties(scmoutsourcePage, scmoutsource);
		Scmoutsource scmoutsourceEntity = scmoutsourceService.getById(scmoutsource.getId());
		if(scmoutsourceEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmoutsourceService.updateMain(scmoutsource, scmoutsourcePage.getScmoutsourceitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "委外订单-通过id删除")
	@ApiOperation(value="委外订单-通过id删除", notes="委外订单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmoutsourceService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "委外订单-批量删除")
	@ApiOperation(value="委外订单-批量删除", notes="委外订单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmoutsourceService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "委外订单-通过id查询")
	@ApiOperation(value="委外订单-通过id查询", notes="委外订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmoutsource> queryById(@RequestParam(name="id",required=true) String id) {
		Scmoutsource scmoutsource = scmoutsourceService.getById(id);
		if(scmoutsource==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmoutsource);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "委外订单分录通过主表ID查询")
	@ApiOperation(value="委外订单分录主表ID查询", notes="委外订单分录-通主表ID查询")
	@GetMapping(value = "/queryScmoutsourceitemByMainId")
	public Result<List<Scmoutsourceitem>> queryScmoutsourceitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmoutsourceitem> scmoutsourceitemList = scmoutsourceitemService.selectByMainId(id);
		return Result.OK(scmoutsourceitemList);
	}

	 @ApiOperation(value="采购订单分录-通过主表ID查询", notes="采购订单分录-通过主表ID查询")
	 @GetMapping(value = "/queryScmoursourceItemlistbyids")
	 public Result<List<Scmoutsourceitem>> queryScmpoitemlistByIds(@RequestParam(name="ids",required=true) String ids) {

		 String[] split = ids.split(",");
		 List<Scmoutsourceitem> scmoutsourceitems = scmoutsourceitemService.listByIds(Arrays.asList(split));
		 return Result.OK(scmoutsourceitems);
	 }


	@GetMapping("/queryscmoutsourcebominfo")
	public Result<List<Scmoutsourceitembom>> queryScmoutsourceitemBomListByitemid(@RequestParam(name = "itemid")String itemid){
		List<Scmoutsourceitembom> scmoutsourceitemboms = scmoutsourceitemService.selectBomInfoByMainId(itemid);
		return Result.OK(scmoutsourceitemboms);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmoutsource
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmoutsource scmoutsource) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmoutsource> queryWrapper = QueryGenerator.initQueryWrapper(scmoutsource, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmoutsource> scmoutsourceList = scmoutsourceService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmoutsourcePage> pageList = new ArrayList<ScmoutsourcePage>();
      for (Scmoutsource main : scmoutsourceList) {
          ScmoutsourcePage vo = new ScmoutsourcePage();
          BeanUtils.copyProperties(main, vo);
          List<Scmoutsourceitem> scmoutsourceitemList = scmoutsourceitemService.selectByMainId(main.getId());
          vo.setScmoutsourceitemList(scmoutsourceitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "委外订单列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmoutsourcePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("委外订单数据", "导出人:"+sysUser.getRealname(), "委外订单"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ScmoutsourcePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmoutsourcePage.class, params);
              for (ScmoutsourcePage page : list) {
                  Scmoutsource po = new Scmoutsource();
                  BeanUtils.copyProperties(page, po);
                  scmoutsourceService.saveMain(po, page.getScmoutsourceitemList(),page);
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

	 @GetMapping("/updatestatus")
	 public Result<String> updatePoStatus(@RequestParam("ids") String ids,@RequestParam("flag") String flag){
		 String[] split = ids.split(",");
		 scmoutsourceService.updateStatus(Arrays.asList(split),flag);
		 return Result.OK("更新成功");
	 }

	 @PostMapping("/outstock")
	 public Result<String> wwoutStock(@RequestBody String ids){
		 JSONObject jsonObject = (JSONObject) JSON.parse(ids);
		 String ids1 = jsonObject.get("ids").toString();

		 String[] split = ids1.split(",");
		 try {
			 scmoutsourceService.wwoutStock(Arrays.asList(split));
			 return Result.ok("出库成功");
		 }catch(Exception exception){
			 return Result.error(exception.getMessage());
		 }

	 }

	 @ApiOperation(value="委外订单-分页列表查询", notes="委外订单-分页列表查询")
	 @GetMapping(value = "/receiptreqreferwwlist")
	 public Result<IPage<ScmReceiptReqReferWwVo>> getReceiptReferWwList(ScmReceiptReqReferWwVo scmReceiptReqReferWwVo,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		// QueryWrapper<ScmReceiptReqReferWwVo> queryWrapper = QueryGenerator.initQueryWrapper(scmReceiptReqReferWwVo, req.getParameterMap());
		 Page<ScmReceiptReqReferWwVo> page = new Page<ScmReceiptReqReferWwVo>(pageNo, pageSize);
		 List<ScmReceiptReqReferWwVo> reciptReqReferWw = scmoutsourceService.getReciptReqReferWw(page, req.getParameterMap());
		 page.setRecords(reciptReqReferWw);
		 return Result.OK(page);
	 }



}
