package org.br.erp.inventory.controller;

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
import org.br.erp.inventory.entity.Scmtransferbillitem;
import org.br.erp.inventory.entity.Scmtransferbill;
import org.br.erp.inventory.vo.ScmtransferbillPage;
import org.br.erp.inventory.service.IScmtransferbillService;
import org.br.erp.inventory.service.IScmtransferbillitemService;
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
 * @Description: 移库单
 * @Author: jeecg-boot
 * @Date:   2022-11-07
 * @Version: V1.0
 */
@Api(tags="移库单")
@RestController
@RequestMapping("/inventory/scmtransferbill")
@Slf4j
public class ScmtransferbillController {
	@Autowired
	private IScmtransferbillService scmtransferbillService;
	@Autowired
	private IScmtransferbillitemService scmtransferbillitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmtransferbill
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "移库单-分页列表查询")
	@ApiOperation(value="移库单-分页列表查询", notes="移库单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmtransferbill>> queryPageList(Scmtransferbill scmtransferbill,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmtransferbill> queryWrapper = QueryGenerator.initQueryWrapper(scmtransferbill, req.getParameterMap());
		Page<Scmtransferbill> page = new Page<Scmtransferbill>(pageNo, pageSize);
		IPage<Scmtransferbill> pageList = scmtransferbillService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmtransferbillPage
	 * @return
	 */
	@AutoLog(value = "移库单-添加")
	@ApiOperation(value="移库单-添加", notes="移库单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmtransferbillPage scmtransferbillPage) {
		Scmtransferbill scmtransferbill = new Scmtransferbill();
		BeanUtils.copyProperties(scmtransferbillPage, scmtransferbill);
		scmtransferbillService.saveMain(scmtransferbill, scmtransferbillPage.getScmtransferbillitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmtransferbillPage
	 * @return
	 */
	@AutoLog(value = "移库单-编辑")
	@ApiOperation(value="移库单-编辑", notes="移库单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmtransferbillPage scmtransferbillPage) {
		Scmtransferbill scmtransferbill = new Scmtransferbill();
		BeanUtils.copyProperties(scmtransferbillPage, scmtransferbill);
		Scmtransferbill scmtransferbillEntity = scmtransferbillService.getById(scmtransferbill.getId());
		if(scmtransferbillEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmtransferbillService.updateMain(scmtransferbill, scmtransferbillPage.getScmtransferbillitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "移库单-通过id删除")
	@ApiOperation(value="移库单-通过id删除", notes="移库单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmtransferbillService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "移库单-批量删除")
	@ApiOperation(value="移库单-批量删除", notes="移库单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmtransferbillService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "移库单-通过id查询")
	@ApiOperation(value="移库单-通过id查询", notes="移库单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmtransferbill> queryById(@RequestParam(name="id",required=true) String id) {
		Scmtransferbill scmtransferbill = scmtransferbillService.getById(id);
		if(scmtransferbill==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmtransferbill);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "移库单分录-通过主表ID查询")
	@ApiOperation(value="移库单分录-通过主表ID查询", notes="移库单分录-通过主表ID查询")
	@GetMapping(value = "/queryScmtransferbillitemByMainId")
	public Result<IPage<Scmtransferbillitem>> queryScmtransferbillitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmtransferbillitem> scmtransferbillitemList = scmtransferbillitemService.selectByMainId(id);
		IPage <Scmtransferbillitem> page = new Page<>();
		page.setRecords(scmtransferbillitemList);
		page.setTotal(scmtransferbillitemList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmtransferbill
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmtransferbill scmtransferbill) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmtransferbill> queryWrapper = QueryGenerator.initQueryWrapper(scmtransferbill, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmtransferbill>  scmtransferbillList = scmtransferbillService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmtransferbillPage> pageList = new ArrayList<ScmtransferbillPage>();
      for (Scmtransferbill main : scmtransferbillList) {
          ScmtransferbillPage vo = new ScmtransferbillPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmtransferbillitem> scmtransferbillitemList = scmtransferbillitemService.selectByMainId(main.getId());
          vo.setScmtransferbillitemList(scmtransferbillitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "移库单列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmtransferbillPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("移库单数据", "导出人:"+sysUser.getRealname(), "移库单"));
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
              List<ScmtransferbillPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmtransferbillPage.class, params);
              for (ScmtransferbillPage page : list) {
                  Scmtransferbill po = new Scmtransferbill();
                  BeanUtils.copyProperties(page, po);
                  scmtransferbillService.saveMain(po, page.getScmtransferbillitemList());
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
		 scmtransferbillService.updatePoStatus(Arrays.asList(split),flag);
		 return Result.OK("更新成功");
	 }

}
