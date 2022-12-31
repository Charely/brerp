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
import org.br.erp.so.entity.Scmsalesinvoiceitem;
import org.br.erp.so.entity.Scmsaleinvoice;
import org.br.erp.so.vo.ScmsaleinvoicePage;
import org.br.erp.so.service.IScmsaleinvoiceService;
import org.br.erp.so.service.IScmsalesinvoiceitemService;
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
 * @Description: 销售发票
 * @Author: jeecg-boot
 * @Date:   2022-11-08
 * @Version: V1.0
 */
@Api(tags="销售发票")
@RestController
@RequestMapping("/so/scmsaleinvoice")
@Slf4j
public class ScmsaleinvoiceController {
	@Autowired
	private IScmsaleinvoiceService scmsaleinvoiceService;
	@Autowired
	private IScmsalesinvoiceitemService scmsalesinvoiceitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmsaleinvoice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "销售发票-分页列表查询")
	@ApiOperation(value="销售发票-分页列表查询", notes="销售发票-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmsaleinvoice>> queryPageList(Scmsaleinvoice scmsaleinvoice,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmsaleinvoice> queryWrapper = QueryGenerator.initQueryWrapper(scmsaleinvoice, req.getParameterMap());
		Page<Scmsaleinvoice> page = new Page<Scmsaleinvoice>(pageNo, pageSize);
		IPage<Scmsaleinvoice> pageList = scmsaleinvoiceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmsaleinvoicePage
	 * @return
	 */
	@AutoLog(value = "销售发票-添加")
	@ApiOperation(value="销售发票-添加", notes="销售发票-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmsaleinvoicePage scmsaleinvoicePage) {
		Scmsaleinvoice scmsaleinvoice = new Scmsaleinvoice();
		BeanUtils.copyProperties(scmsaleinvoicePage, scmsaleinvoice);
		scmsaleinvoiceService.saveMain(scmsaleinvoice, scmsaleinvoicePage.getScmsalesinvoiceitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmsaleinvoicePage
	 * @return
	 */
	@AutoLog(value = "销售发票-编辑")
	@ApiOperation(value="销售发票-编辑", notes="销售发票-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmsaleinvoicePage scmsaleinvoicePage) {
		Scmsaleinvoice scmsaleinvoice = new Scmsaleinvoice();
		BeanUtils.copyProperties(scmsaleinvoicePage, scmsaleinvoice);
		Scmsaleinvoice scmsaleinvoiceEntity = scmsaleinvoiceService.getById(scmsaleinvoice.getId());
		if(scmsaleinvoiceEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmsaleinvoiceService.updateMain(scmsaleinvoice, scmsaleinvoicePage.getScmsalesinvoiceitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "销售发票-通过id删除")
	@ApiOperation(value="销售发票-通过id删除", notes="销售发票-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmsaleinvoiceService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "销售发票-批量删除")
	@ApiOperation(value="销售发票-批量删除", notes="销售发票-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmsaleinvoiceService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售发票-通过id查询")
	@ApiOperation(value="销售发票-通过id查询", notes="销售发票-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmsaleinvoice> queryById(@RequestParam(name="id",required=true) String id) {
		Scmsaleinvoice scmsaleinvoice = scmsaleinvoiceService.getById(id);
		if(scmsaleinvoice==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmsaleinvoice);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "销售发票分录-通过主表ID查询")
	@ApiOperation(value="销售发票分录-通过主表ID查询", notes="销售发票分录-通过主表ID查询")
	@GetMapping(value = "/queryScmsalesinvoiceitemByMainId")
	public Result<IPage<Scmsalesinvoiceitem>> queryScmsalesinvoiceitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmsalesinvoiceitem> scmsalesinvoiceitemList = scmsalesinvoiceitemService.selectByMainId(id);
		IPage <Scmsalesinvoiceitem> page = new Page<>();
		page.setRecords(scmsalesinvoiceitemList);
		page.setTotal(scmsalesinvoiceitemList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmsaleinvoice
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmsaleinvoice scmsaleinvoice) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmsaleinvoice> queryWrapper = QueryGenerator.initQueryWrapper(scmsaleinvoice, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmsaleinvoice>  scmsaleinvoiceList = scmsaleinvoiceService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmsaleinvoicePage> pageList = new ArrayList<ScmsaleinvoicePage>();
      for (Scmsaleinvoice main : scmsaleinvoiceList) {
          ScmsaleinvoicePage vo = new ScmsaleinvoicePage();
          BeanUtils.copyProperties(main, vo);
          List<Scmsalesinvoiceitem> scmsalesinvoiceitemList = scmsalesinvoiceitemService.selectByMainId(main.getId());
          vo.setScmsalesinvoiceitemList(scmsalesinvoiceitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "销售发票列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmsaleinvoicePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("销售发票数据", "导出人:"+sysUser.getRealname(), "销售发票"));
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
              List<ScmsaleinvoicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmsaleinvoicePage.class, params);
              for (ScmsaleinvoicePage page : list) {
                  Scmsaleinvoice po = new Scmsaleinvoice();
                  BeanUtils.copyProperties(page, po);
                  scmsaleinvoiceService.saveMain(po, page.getScmsalesinvoiceitemList());
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

}
