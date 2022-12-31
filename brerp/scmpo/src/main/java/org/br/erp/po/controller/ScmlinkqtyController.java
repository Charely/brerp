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
import org.br.erp.po.entity.Scmlinkqtyitem;
import org.br.erp.po.entity.Scmlinkqty;
import org.br.erp.po.vo.ScmlinkqtyPage;
import org.br.erp.po.service.IScmlinkqtyService;
import org.br.erp.po.service.IScmlinkqtyitemService;
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
 * @Description: 配额定义
 * @Author: jeecg-boot
 * @Date:   2022-08-27
 * @Version: V1.0
 */
@Api(tags="配额定义")
@RestController
@RequestMapping("/po/scmlinkqty")
@Slf4j
public class ScmlinkqtyController {
	@Autowired
	private IScmlinkqtyService scmlinkqtyService;
	@Autowired
	private IScmlinkqtyitemService scmlinkqtyitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmlinkqty
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "配额定义-分页列表查询")
	@ApiOperation(value="配额定义-分页列表查询", notes="配额定义-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmlinkqty>> queryPageList(Scmlinkqty scmlinkqty,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmlinkqty> queryWrapper = QueryGenerator.initQueryWrapper(scmlinkqty, req.getParameterMap());
		Page<Scmlinkqty> page = new Page<Scmlinkqty>(pageNo, pageSize);
		IPage<Scmlinkqty> pageList = scmlinkqtyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmlinkqtyPage
	 * @return
	 */
	@AutoLog(value = "配额定义-添加")
	@ApiOperation(value="配额定义-添加", notes="配额定义-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmlinkqtyPage scmlinkqtyPage) {
		Scmlinkqty scmlinkqty = new Scmlinkqty();
		BeanUtils.copyProperties(scmlinkqtyPage, scmlinkqty);
		scmlinkqtyService.saveMain(scmlinkqty, scmlinkqtyPage.getScmlinkqtyitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmlinkqtyPage
	 * @return
	 */
	@AutoLog(value = "配额定义-编辑")
	@ApiOperation(value="配额定义-编辑", notes="配额定义-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmlinkqtyPage scmlinkqtyPage) {
		Scmlinkqty scmlinkqty = new Scmlinkqty();
		BeanUtils.copyProperties(scmlinkqtyPage, scmlinkqty);
		Scmlinkqty scmlinkqtyEntity = scmlinkqtyService.getById(scmlinkqty.getId());
		if(scmlinkqtyEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmlinkqtyService.updateMain(scmlinkqty, scmlinkqtyPage.getScmlinkqtyitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "配额定义-通过id删除")
	@ApiOperation(value="配额定义-通过id删除", notes="配额定义-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmlinkqtyService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "配额定义-批量删除")
	@ApiOperation(value="配额定义-批量删除", notes="配额定义-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmlinkqtyService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "配额定义-通过id查询")
	@ApiOperation(value="配额定义-通过id查询", notes="配额定义-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmlinkqty> queryById(@RequestParam(name="id",required=true) String id) {
		Scmlinkqty scmlinkqty = scmlinkqtyService.getById(id);
		if(scmlinkqty==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmlinkqty);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "配额协议分录-通过主表ID查询")
	@ApiOperation(value="配额协议分录-通过主表ID查询", notes="配额协议分录-通过主表ID查询")
	@GetMapping(value = "/queryScmlinkqtyitemByMainId")
	public Result<IPage<Scmlinkqtyitem>> queryScmlinkqtyitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmlinkqtyitem> scmlinkqtyitemList = scmlinkqtyitemService.selectByMainId(id);
		IPage <Scmlinkqtyitem> page = new Page<>();
		page.setRecords(scmlinkqtyitemList);
		page.setTotal(scmlinkqtyitemList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmlinkqty
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmlinkqty scmlinkqty) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmlinkqty> queryWrapper = QueryGenerator.initQueryWrapper(scmlinkqty, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmlinkqty>  scmlinkqtyList = scmlinkqtyService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmlinkqtyPage> pageList = new ArrayList<ScmlinkqtyPage>();
      for (Scmlinkqty main : scmlinkqtyList) {
          ScmlinkqtyPage vo = new ScmlinkqtyPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmlinkqtyitem> scmlinkqtyitemList = scmlinkqtyitemService.selectByMainId(main.getId());
          vo.setScmlinkqtyitemList(scmlinkqtyitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "配额定义列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmlinkqtyPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("配额定义数据", "导出人:"+sysUser.getRealname(), "配额定义"));
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
              List<ScmlinkqtyPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmlinkqtyPage.class, params);
              for (ScmlinkqtyPage page : list) {
                  Scmlinkqty po = new Scmlinkqty();
                  BeanUtils.copyProperties(page, po);
                  scmlinkqtyService.saveMain(po, page.getScmlinkqtyitemList());
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
