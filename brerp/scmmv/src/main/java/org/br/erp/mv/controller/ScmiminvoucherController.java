package org.br.erp.im.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import com.alibaba.fastjson.JSONObject;
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
import org.br.erp.im.entity.Scmiminvoucheritem;
import org.br.erp.im.entity.Scmiminvoucher;
import org.br.erp.im.vo.ScmiminvoucherPage;
import org.br.erp.im.service.IScmiminvoucherService;
import org.br.erp.im.service.IScmiminvoucheritemService;
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
 * @Description: 入库凭证
 * @Author: jeecg-boot
 * @Date:   2022-11-26
 * @Version: V1.0
 */
@Api(tags="入库凭证")
@RestController
@RequestMapping("/im/scmiminvoucher")
@Slf4j
public class ScmiminvoucherController {
	@Autowired
	private IScmiminvoucherService scmiminvoucherService;
	@Autowired
	private IScmiminvoucheritemService scmiminvoucheritemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmiminvoucher
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "入库凭证-分页列表查询")
	@ApiOperation(value="入库凭证-分页列表查询", notes="入库凭证-分页列表查询")
	@GetMapping(value = "/list/{flag}")
	public Result<IPage<Scmiminvoucher>> queryPageList(Scmiminvoucher scmiminvoucher,
													   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													   HttpServletRequest req,
													   @PathVariable("flag")String flag) {
		QueryWrapper<Scmiminvoucher> queryWrapper = QueryGenerator.initQueryWrapper(scmiminvoucher, req.getParameterMap());
		Page<Scmiminvoucher> page = new Page<Scmiminvoucher>(pageNo, pageSize);
		if(flag !=""){
			queryWrapper.eq("voucherkind",flag);
		}
		IPage<Scmiminvoucher> pageList = scmiminvoucherService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmiminvoucherPage
	 * @return
	 */
	@AutoLog(value = "入库凭证-添加")
	@ApiOperation(value="入库凭证-添加", notes="入库凭证-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmiminvoucherPage scmiminvoucherPage) {
		Scmiminvoucher scmiminvoucher = new Scmiminvoucher();
		BeanUtils.copyProperties(scmiminvoucherPage, scmiminvoucher);
		scmiminvoucherService.saveMain(scmiminvoucher, scmiminvoucherPage.getScmiminvoucheritemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmiminvoucherPage
	 * @return
	 */
	@AutoLog(value = "入库凭证-编辑")
	@ApiOperation(value="入库凭证-编辑", notes="入库凭证-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmiminvoucherPage scmiminvoucherPage) {
		Scmiminvoucher scmiminvoucher = new Scmiminvoucher();
		BeanUtils.copyProperties(scmiminvoucherPage, scmiminvoucher);
		Scmiminvoucher scmiminvoucherEntity = scmiminvoucherService.getById(scmiminvoucher.getId());
		if(scmiminvoucherEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmiminvoucherService.updateMain(scmiminvoucher, scmiminvoucherPage.getScmiminvoucheritemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "入库凭证-通过id删除")
	@ApiOperation(value="入库凭证-通过id删除", notes="入库凭证-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmiminvoucherService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "入库凭证-批量删除")
	@ApiOperation(value="入库凭证-批量删除", notes="入库凭证-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmiminvoucherService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "入库凭证-通过id查询")
	@ApiOperation(value="入库凭证-通过id查询", notes="入库凭证-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmiminvoucher> queryById(@RequestParam(name="id",required=true) String id) {
		Scmiminvoucher scmiminvoucher = scmiminvoucherService.getById(id);
		if(scmiminvoucher==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmiminvoucher);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "入库凭证分录通过主表ID查询")
	@ApiOperation(value="入库凭证分录主表ID查询", notes="入库凭证分录-通主表ID查询")
	@GetMapping(value = "/queryScmiminvoucheritemByMainId")
	public Result<List<Scmiminvoucheritem>> queryScmiminvoucheritemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmiminvoucheritem> scmiminvoucheritemList = scmiminvoucheritemService.selectByMainId(id);
		return Result.OK(scmiminvoucheritemList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmiminvoucher
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmiminvoucher scmiminvoucher) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmiminvoucher> queryWrapper = QueryGenerator.initQueryWrapper(scmiminvoucher, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmiminvoucher> scmiminvoucherList = scmiminvoucherService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmiminvoucherPage> pageList = new ArrayList<ScmiminvoucherPage>();
      for (Scmiminvoucher main : scmiminvoucherList) {
          ScmiminvoucherPage vo = new ScmiminvoucherPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmiminvoucheritem> scmiminvoucheritemList = scmiminvoucheritemService.selectByMainId(main.getId());
          vo.setScmiminvoucheritemList(scmiminvoucheritemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "入库凭证列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmiminvoucherPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("入库凭证数据", "导出人:"+sysUser.getRealname(), "入库凭证"));
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
              List<ScmiminvoucherPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmiminvoucherPage.class, params);
              for (ScmiminvoucherPage page : list) {
                  Scmiminvoucher po = new Scmiminvoucher();
                  BeanUtils.copyProperties(page, po);
                  scmiminvoucherService.saveMain(po, page.getScmiminvoucheritemList());
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

	@PostMapping("/createvoucher")
	public Result<String> createMvoucher(@RequestBody String ids) throws ParseException {
		JSONObject jsonObject = (JSONObject) JSONObject.parse(ids);
		String idss = (String) jsonObject.get("ids");
		String flag = (String) jsonObject.get("flag");

		scmiminvoucherService.createMvouncher(Arrays.asList(idss.split(",")),flag);
		return Result.ok("生成存货凭证成功");
	}


	@GetMapping("/updatestatus")
	public Result<String> updateStatus(@RequestParam("ids") String ids,@RequestParam("flag") String flag){
		scmiminvoucherService.updateStatus(Arrays.asList(ids.split(",")),flag);
		return Result.ok("更新成功");
	}

}
