package org.br.erp.so.controller;

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

import org.br.erp.so.vo.ScmOutStockReferSoVo;
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
import org.br.erp.so.entity.Scmsoitem;
import org.br.erp.so.entity.Scmso;
import org.br.erp.so.vo.ScmsoPage;
import org.br.erp.so.service.IScmsoService;
import org.br.erp.so.service.IScmsoitemService;
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
 * @Description: 销售订单
 * @Author: jeecg-boot
 * @Date:   2022-10-30
 * @Version: V1.0
 */
@Api(tags="销售订单")
@RestController
@RequestMapping("/so/scmso")
@Slf4j
public class ScmsoController {
	@Autowired
	private IScmsoService scmsoService;
	@Autowired
	private IScmsoitemService scmsoitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmso
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售订单-分页列表查询")
	@ApiOperation(value="销售订单-分页列表查询", notes="销售订单-分页列表查询")
	@GetMapping(value = "/list/{isred}")
	public Result<IPage<Scmso>> queryPageList(Scmso scmso,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req,
										@PathVariable("isred")String isred) {
		QueryWrapper<Scmso> queryWrapper = QueryGenerator.initQueryWrapper(scmso, req.getParameterMap());
		Page<Scmso> page = new Page<Scmso>(pageNo, pageSize);
		if(isred.equalsIgnoreCase("0")){
			queryWrapper.eq("isred","0");
		}else if(isred.equalsIgnoreCase("1")){
			queryWrapper.eq("isred","1");
		}
		IPage<Scmso> pageList = scmsoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmsoPage
	 * @return
	 */
	@AutoLog(value = "销售订单-添加")
	@ApiOperation(value="销售订单-添加", notes="销售订单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmsoPage scmsoPage) {
		Scmso scmso = new Scmso();
		BeanUtils.copyProperties(scmsoPage, scmso);
		scmsoService.saveMain(scmso, scmsoPage.getScmsoitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmsoPage
	 * @return
	 */
	@AutoLog(value = "销售订单-编辑")
	@ApiOperation(value="销售订单-编辑", notes="销售订单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmsoPage scmsoPage) {
		Scmso scmso = new Scmso();
		BeanUtils.copyProperties(scmsoPage, scmso);
		Scmso scmsoEntity = scmsoService.getById(scmso.getId());
		if(scmsoEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmsoService.updateMain(scmso, scmsoPage.getScmsoitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售订单-通过id删除")
	@ApiOperation(value="销售订单-通过id删除", notes="销售订单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmsoService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售订单-批量删除")
	@ApiOperation(value="销售订单-批量删除", notes="销售订单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmsoService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售订单-通过id查询")
	@ApiOperation(value="销售订单-通过id查询", notes="销售订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmso> queryById(@RequestParam(name="id",required=true) String id) {
		Scmso scmso = scmsoService.getById(id);
		if(scmso==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmso);

	}

	 /**
	  *
	  * @param ids
	  * @return
	  */
	 @GetMapping("/queryitembyitemid")
	public Result<List<Scmsoitem>> queryItemByItemid(@RequestParam("ids") String ids){
		 String[] split = ids.split(",");
		 List<Scmsoitem> scmsoitems = scmsoitemService.listByIds(Arrays.asList(split));
		 return Result.OK(scmsoitems);
	 }
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售订单分录-通过主表ID查询")
	@ApiOperation(value="销售订单分录-通过主表ID查询", notes="销售订单分录-通过主表ID查询")
	@GetMapping(value = "/queryScmsoitemByMainId")
	public Result<IPage<Scmsoitem>> queryScmsoitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmsoitem> scmsoitemList = scmsoitemService.selectByMainId(id);
		IPage <Scmsoitem> page = new Page<>();
		page.setRecords(scmsoitemList);
		page.setTotal(scmsoitemList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmso
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmso scmso) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmso> queryWrapper = QueryGenerator.initQueryWrapper(scmso, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmso>  scmsoList = scmsoService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmsoPage> pageList = new ArrayList<ScmsoPage>();
      for (Scmso main : scmsoList) {
          ScmsoPage vo = new ScmsoPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmsoitem> scmsoitemList = scmsoitemService.selectByMainId(main.getId());
          vo.setScmsoitemList(scmsoitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售订单列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmsoPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售订单数据", "导出人:"+sysUser.getRealname(), "销售订单"));
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
              List<ScmsoPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmsoPage.class, params);
              for (ScmsoPage page : list) {
                  Scmso po = new Scmso();
                  BeanUtils.copyProperties(page, po);
                  scmsoService.saveMain(po, page.getScmsoitemList());
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

	@GetMapping("/getrefersolist/{isred}")
	public Result<IPage<ScmOutStockReferSoVo>> getReferList( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															 HttpServletRequest req,
															 @PathVariable("isred")String isred)
	{
		Page<ScmOutStockReferSoVo> page=new Page<>(pageNo,pageSize);
		Map<String, String[]> parameterMap = req.getParameterMap();
		List<ScmOutStockReferSoVo> referSOlist = scmsoService.getReferSOlist(page, parameterMap,isred);
		page.setRecords(referSOlist);
		return  Result.OK(page);
	}

	 @GetMapping("/updatestatus")
	 public Result<String> updatePoStatus(@RequestParam("ids") String ids,@RequestParam("flag") String flag){
		 String[] split = ids.split(",");
		 scmsoService.updatePoStatus(Arrays.asList(split),flag);
		 return Result.OK("更新成功");
	 }

}
