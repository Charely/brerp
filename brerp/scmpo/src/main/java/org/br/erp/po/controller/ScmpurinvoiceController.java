package org.br.erp.po.controller;

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
import org.br.erp.po.entity.Scmpurinvoiceitem;
import org.br.erp.po.entity.Scmpurinvoice;
import org.br.erp.po.vo.ScmpurinvoicePage;
import org.br.erp.po.service.IScmpurinvoiceService;
import org.br.erp.po.service.IScmpurinvoiceitemService;
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
 * @Description: 采购发票
 * @Author: jeecg-boot
 * @Date:   2022-11-09
 * @Version: V1.0
 */
@Api(tags="采购发票")
@RestController
@RequestMapping("/po/scmpurinvoice")
@Slf4j
public class ScmpurinvoiceController {
	@Autowired
	private IScmpurinvoiceService scmpurinvoiceService;
	@Autowired
	private IScmpurinvoiceitemService scmpurinvoiceitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmpurinvoice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购发票-分页列表查询")
	@ApiOperation(value="采购发票-分页列表查询", notes="采购发票-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmpurinvoice>> queryPageList(Scmpurinvoice scmpurinvoice,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmpurinvoice> queryWrapper = QueryGenerator.initQueryWrapper(scmpurinvoice, req.getParameterMap());
		Page<Scmpurinvoice> page = new Page<Scmpurinvoice>(pageNo, pageSize);
		IPage<Scmpurinvoice> pageList = scmpurinvoiceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmpurinvoicePage
	 * @return
	 */
	@AutoLog(value = "采购发票-添加")
	@ApiOperation(value="采购发票-添加", notes="采购发票-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmpurinvoicePage scmpurinvoicePage) {
		Scmpurinvoice scmpurinvoice = new Scmpurinvoice();
		BeanUtils.copyProperties(scmpurinvoicePage, scmpurinvoice);
		scmpurinvoiceService.saveMain(scmpurinvoice, scmpurinvoicePage.getScmpurinvoiceitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmpurinvoicePage
	 * @return
	 */
	@AutoLog(value = "采购发票-编辑")
	@ApiOperation(value="采购发票-编辑", notes="采购发票-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmpurinvoicePage scmpurinvoicePage) {
		Scmpurinvoice scmpurinvoice = new Scmpurinvoice();
		BeanUtils.copyProperties(scmpurinvoicePage, scmpurinvoice);
		Scmpurinvoice scmpurinvoiceEntity = scmpurinvoiceService.getById(scmpurinvoice.getId());
		if(scmpurinvoiceEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmpurinvoiceService.updateMain(scmpurinvoice, scmpurinvoicePage.getScmpurinvoiceitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购发票-通过id删除")
	@ApiOperation(value="采购发票-通过id删除", notes="采购发票-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmpurinvoiceService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购发票-批量删除")
	@ApiOperation(value="采购发票-批量删除", notes="采购发票-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmpurinvoiceService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购发票-通过id查询")
	@ApiOperation(value="采购发票-通过id查询", notes="采购发票-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmpurinvoice> queryById(@RequestParam(name="id",required=true) String id) {
		Scmpurinvoice scmpurinvoice = scmpurinvoiceService.getById(id);
		if(scmpurinvoice==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmpurinvoice);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购发票分录-通过主表ID查询")
	@ApiOperation(value="采购发票分录-通过主表ID查询", notes="采购发票分录-通过主表ID查询")
	@GetMapping(value = "/queryScmpurinvoiceitemByMainId")
	public Result<IPage<Scmpurinvoiceitem>> queryScmpurinvoiceitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmpurinvoiceitem> scmpurinvoiceitemList = scmpurinvoiceitemService.selectByMainId(id);
		IPage <Scmpurinvoiceitem> page = new Page<>();
		page.setRecords(scmpurinvoiceitemList);
		page.setTotal(scmpurinvoiceitemList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmpurinvoice
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmpurinvoice scmpurinvoice) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmpurinvoice> queryWrapper = QueryGenerator.initQueryWrapper(scmpurinvoice, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmpurinvoice>  scmpurinvoiceList = scmpurinvoiceService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmpurinvoicePage> pageList = new ArrayList<ScmpurinvoicePage>();
      for (Scmpurinvoice main : scmpurinvoiceList) {
          ScmpurinvoicePage vo = new ScmpurinvoicePage();
          BeanUtils.copyProperties(main, vo);
          List<Scmpurinvoiceitem> scmpurinvoiceitemList = scmpurinvoiceitemService.selectByMainId(main.getId());
          vo.setScmpurinvoiceitemList(scmpurinvoiceitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购发票列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmpurinvoicePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购发票数据", "导出人:"+sysUser.getRealname(), "采购发票"));
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
              List<ScmpurinvoicePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmpurinvoicePage.class, params);
              for (ScmpurinvoicePage page : list) {
                  Scmpurinvoice po = new Scmpurinvoice();
                  BeanUtils.copyProperties(page, po);
                  scmpurinvoiceService.saveMain(po, page.getScmpurinvoiceitemList());
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
