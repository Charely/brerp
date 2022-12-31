package org.br.erp.base.controller;

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

import org.apache.ibatis.annotations.Param;
import org.br.erp.base.vo.ColumnVo;
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
import org.br.erp.base.entity.Scmcustomformatitem;
import org.br.erp.base.entity.Scmcustomformat;
import org.br.erp.base.vo.ScmcustomformatPage;
import org.br.erp.base.service.IScmcustomformatService;
import org.br.erp.base.service.IScmcustomformatitemService;
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
 * @Description: 自定义格式表
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
@Api(tags="自定义格式表")
@RestController
@RequestMapping("/base/scmcustomformat")
@Slf4j
public class ScmcustomformatController {
	@Autowired
	private IScmcustomformatService scmcustomformatService;
	@Autowired
	private IScmcustomformatitemService scmcustomformatitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmcustomformat
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "自定义格式表-分页列表查询")
	@ApiOperation(value="自定义格式表-分页列表查询", notes="自定义格式表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmcustomformat>> queryPageList(Scmcustomformat scmcustomformat,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmcustomformat> queryWrapper = QueryGenerator.initQueryWrapper(scmcustomformat, req.getParameterMap());
		Page<Scmcustomformat> page = new Page<Scmcustomformat>(pageNo, pageSize);
		IPage<Scmcustomformat> pageList = scmcustomformatService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	@GetMapping("/getcolumnlist")
	public Result<List<ColumnVo>> getcolumnVoList(@RequestParam("objectcode")String objectcode){
		List<ColumnVo> columnVoList = scmcustomformatService.getColumnVoList(objectcode);
		if(columnVoList==null || columnVoList.size()==0){
			return Result.error("格式不存在");
		}else{
			return Result.ok(columnVoList);
		}
	}

	 /**
	  * 导入系统格式
	  * @param objectid
	  * @return
	  */

	 @GetMapping("/importsysformat")
	public Result<String> importSysFormat(@RequestParam("objectid") String objectid){
		scmcustomformatService.importSysFormat(objectid);
		return Result.ok("导入成功");
	}


	/**
	 *   添加
	 *
	 * @param scmcustomformatPage
	 * @return
	 */
	@AutoLog(value = "自定义格式表-添加")
	@ApiOperation(value="自定义格式表-添加", notes="自定义格式表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmcustomformatPage scmcustomformatPage) {
		Scmcustomformat scmcustomformat = new Scmcustomformat();
		BeanUtils.copyProperties(scmcustomformatPage, scmcustomformat);
		scmcustomformatService.saveMain(scmcustomformat, scmcustomformatPage.getScmcustomformatitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmcustomformatPage
	 * @return
	 */
	@AutoLog(value = "自定义格式表-编辑")
	@ApiOperation(value="自定义格式表-编辑", notes="自定义格式表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmcustomformatPage scmcustomformatPage) {
		Scmcustomformat scmcustomformat = new Scmcustomformat();
		BeanUtils.copyProperties(scmcustomformatPage, scmcustomformat);
		Scmcustomformat scmcustomformatEntity = scmcustomformatService.getById(scmcustomformat.getId());
		if(scmcustomformatEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmcustomformatService.updateMain(scmcustomformat, scmcustomformatPage.getScmcustomformatitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自定义格式表-通过id删除")
	@ApiOperation(value="自定义格式表-通过id删除", notes="自定义格式表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmcustomformatService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "自定义格式表-批量删除")
	@ApiOperation(value="自定义格式表-批量删除", notes="自定义格式表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmcustomformatService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "自定义格式表-通过id查询")
	@ApiOperation(value="自定义格式表-通过id查询", notes="自定义格式表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmcustomformat> queryById(@RequestParam(name="id",required=true) String id) {
		Scmcustomformat scmcustomformat = scmcustomformatService.getById(id);
		if(scmcustomformat==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmcustomformat);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "自定义格式表分录-通过主表ID查询")
	@ApiOperation(value="自定义格式表分录-通过主表ID查询", notes="自定义格式表分录-通过主表ID查询")
	@GetMapping(value = "/queryScmcustomformatitemByMainId")
	public Result<IPage<Scmcustomformatitem>> queryScmcustomformatitemListByMainId(@RequestParam(name="id",required=true) String id) {

		QueryWrapper<Scmcustomformatitem> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("parentid",id);
		queryWrapper.orderByAsc("colorder");
		List<Scmcustomformatitem> scmcustomformatitemList = scmcustomformatitemService.list(queryWrapper);
		//List<Scmcustomformatitem> scmcustomformatitemList = scmcustomformatitemService.selectByMainId(id);
		IPage <Scmcustomformatitem> page = new Page<>();
		page.setRecords(scmcustomformatitemList);
		page.setTotal(scmcustomformatitemList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmcustomformat
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmcustomformat scmcustomformat) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmcustomformat> queryWrapper = QueryGenerator.initQueryWrapper(scmcustomformat, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmcustomformat>  scmcustomformatList = scmcustomformatService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmcustomformatPage> pageList = new ArrayList<ScmcustomformatPage>();
      for (Scmcustomformat main : scmcustomformatList) {
          ScmcustomformatPage vo = new ScmcustomformatPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmcustomformatitem> scmcustomformatitemList = scmcustomformatitemService.selectByMainId(main.getId());
          vo.setScmcustomformatitemList(scmcustomformatitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "自定义格式表列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmcustomformatPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("自定义格式表数据", "导出人:"+sysUser.getRealname(), "自定义格式表"));
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
              List<ScmcustomformatPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmcustomformatPage.class, params);
              for (ScmcustomformatPage page : list) {
                  Scmcustomformat po = new Scmcustomformat();
                  BeanUtils.copyProperties(page, po);
                  scmcustomformatService.saveMain(po, page.getScmcustomformatitemList());
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
