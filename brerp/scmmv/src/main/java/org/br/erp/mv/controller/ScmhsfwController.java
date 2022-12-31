package org.br.erp.mv.controller;

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
import org.br.erp.mv.entity.Scmhsfwitem;
import org.br.erp.mv.entity.Scmhsfw;
import org.br.erp.mv.vo.ScmhsfwPage;
import org.br.erp.mv.service.IScmhsfwService;
import org.br.erp.mv.service.IScmhsfwitemService;
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
 * @Description: 存货核算范围
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Api(tags="存货核算范围")
@RestController
@RequestMapping("/mv/scmhsfw")
@Slf4j
public class ScmhsfwController {
	@Autowired
	private IScmhsfwService scmhsfwService;
	@Autowired
	private IScmhsfwitemService scmhsfwitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmhsfw
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "存货核算范围-分页列表查询")
	@ApiOperation(value="存货核算范围-分页列表查询", notes="存货核算范围-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmhsfw>> queryPageList(Scmhsfw scmhsfw,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmhsfw> queryWrapper = QueryGenerator.initQueryWrapper(scmhsfw, req.getParameterMap());
		Page<Scmhsfw> page = new Page<Scmhsfw>(pageNo, pageSize);
		IPage<Scmhsfw> pageList = scmhsfwService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmhsfwPage
	 * @return
	 */
	@AutoLog(value = "存货核算范围-添加")
	@ApiOperation(value="存货核算范围-添加", notes="存货核算范围-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmhsfwPage scmhsfwPage) {
		Scmhsfw scmhsfw = new Scmhsfw();
		BeanUtils.copyProperties(scmhsfwPage, scmhsfw);
		scmhsfwService.saveMain(scmhsfw, scmhsfwPage.getScmhsfwitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmhsfwPage
	 * @return
	 */
	@AutoLog(value = "存货核算范围-编辑")
	@ApiOperation(value="存货核算范围-编辑", notes="存货核算范围-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmhsfwPage scmhsfwPage) {
		Scmhsfw scmhsfw = new Scmhsfw();
		BeanUtils.copyProperties(scmhsfwPage, scmhsfw);
		Scmhsfw scmhsfwEntity = scmhsfwService.getById(scmhsfw.getId());
		if(scmhsfwEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmhsfwService.updateMain(scmhsfw, scmhsfwPage.getScmhsfwitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "存货核算范围-通过id删除")
	@ApiOperation(value="存货核算范围-通过id删除", notes="存货核算范围-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmhsfwService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "存货核算范围-批量删除")
	@ApiOperation(value="存货核算范围-批量删除", notes="存货核算范围-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmhsfwService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "存货核算范围-通过id查询")
	@ApiOperation(value="存货核算范围-通过id查询", notes="存货核算范围-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmhsfw> queryById(@RequestParam(name="id",required=true) String id) {
		Scmhsfw scmhsfw = scmhsfwService.getById(id);
		if(scmhsfw==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmhsfw);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "核算范围分录通过主表ID查询")
	@ApiOperation(value="核算范围分录主表ID查询", notes="核算范围分录-通主表ID查询")
	@GetMapping(value = "/queryScmhsfwitemByMainId")
	public Result<List<Scmhsfwitem>> queryScmhsfwitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmhsfwitem> scmhsfwitemList = scmhsfwitemService.selectByMainId(id);
		return Result.OK(scmhsfwitemList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmhsfw
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmhsfw scmhsfw) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmhsfw> queryWrapper = QueryGenerator.initQueryWrapper(scmhsfw, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmhsfw> scmhsfwList = scmhsfwService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmhsfwPage> pageList = new ArrayList<ScmhsfwPage>();
      for (Scmhsfw main : scmhsfwList) {
          ScmhsfwPage vo = new ScmhsfwPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmhsfwitem> scmhsfwitemList = scmhsfwitemService.selectByMainId(main.getId());
          vo.setScmhsfwitemList(scmhsfwitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "存货核算范围列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmhsfwPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("存货核算范围数据", "导出人:"+sysUser.getRealname(), "存货核算范围"));
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
              List<ScmhsfwPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmhsfwPage.class, params);
              for (ScmhsfwPage page : list) {
                  Scmhsfw po = new Scmhsfw();
                  BeanUtils.copyProperties(page, po);
                  scmhsfwService.saveMain(po, page.getScmhsfwitemList());
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
