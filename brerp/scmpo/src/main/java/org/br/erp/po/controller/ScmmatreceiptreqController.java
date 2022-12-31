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

import org.br.erp.po.vo.ScmInventoryReferMatreceiptItemVo;
import org.br.erp.po.vo.ScmReferMatreceiptreqVo;
import org.br.erp.po.vo.ScmVendorReferPoVo;
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
import org.br.erp.po.entity.Scmmatreceiptreqitem;
import org.br.erp.po.entity.Scmmatreceiptreq;
import org.br.erp.po.vo.ScmmatreceiptreqPage;
import org.br.erp.po.service.IScmmatreceiptreqService;
import org.br.erp.po.service.IScmmatreceiptreqitemService;
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
 * @Description: 收料申请单
 * @Author: jeecg-boot
 * @Date:   2022-10-18
 * @Version: V1.0
 */
@Api(tags="收料申请单")
@RestController
@RequestMapping("/po/scmmatreceiptreq")
@Slf4j
public class ScmmatreceiptreqController {
	@Autowired
	private IScmmatreceiptreqService scmmatreceiptreqService;
	@Autowired
	private IScmmatreceiptreqitemService scmmatreceiptreqitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmmatreceiptreq
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "收料申请单-分页列表查询")
	@ApiOperation(value="收料申请单-分页列表查询", notes="收料申请单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmmatreceiptreq>> queryPageList(Scmmatreceiptreq scmmatreceiptreq,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmmatreceiptreq> queryWrapper = QueryGenerator.initQueryWrapper(scmmatreceiptreq, req.getParameterMap());
		Page<Scmmatreceiptreq> page = new Page<Scmmatreceiptreq>(pageNo, pageSize);
		IPage<Scmmatreceiptreq> pageList = scmmatreceiptreqService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 @GetMapping("/refermatreceiptpreqlist")
	 public Result<IPage<ScmReferMatreceiptreqVo>> queryVendorReferPoList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																		  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																		  HttpServletRequest req)
	 {
		 Page<ScmReferMatreceiptreqVo> page=new Page<>(pageNo,pageSize);
		 Map<String, String[]> parameterMap = req.getParameterMap();
		 List<ScmReferMatreceiptreqVo> scmVendorReferPoVos = scmmatreceiptreqService.getReferMatreceipreqVo(page, parameterMap);


		 Page<ScmReferMatreceiptreqVo> scmMatReceiptReferPoVoPage = page.setRecords(scmVendorReferPoVos);

		 return Result.ok(scmMatReceiptReferPoVoPage);
	 }

	 /**
	  * 根据选中的单据生成入库单信息
	  * @param ids
	  * @return
	  */

	 @GetMapping("/queryitembyitemid")
	 public Result<List<Scmmatreceiptreqitem>> queryItemByItemid(@RequestParam("ids") String ids){
		 String[] split = ids.split(",");
		 List<Scmmatreceiptreqitem> scmmatreceiptreqitemList = scmmatreceiptreqitemService.listByIds(Arrays.asList(split));
		 return Result.OK(scmmatreceiptreqitemList);
	 }
	 @GetMapping("/queryiteminfobyitemid")
	 public Result<List<ScmInventoryReferMatreceiptItemVo>> queryMatreceiptItemByItemid(@RequestParam("ids")String ids){
		 List<ScmInventoryReferMatreceiptItemVo> scmInventoryReferMatreceiptItemVos = scmmatreceiptreqitemService.getscmInventoryReferMaterItemVo(ids);

		 return Result.ok(scmInventoryReferMatreceiptItemVos);
	 }


	/**
	 *   添加
	 *
	 * @param scmmatreceiptreqPage
	 * @return
	 */
	@AutoLog(value = "收料申请单-添加")
	@ApiOperation(value="收料申请单-添加", notes="收料申请单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmmatreceiptreqPage scmmatreceiptreqPage) {
		Scmmatreceiptreq scmmatreceiptreq = new Scmmatreceiptreq();
		BeanUtils.copyProperties(scmmatreceiptreqPage, scmmatreceiptreq);
		scmmatreceiptreqService.saveMain(scmmatreceiptreq, scmmatreceiptreqPage.getScmmatreceiptreqitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmmatreceiptreqPage
	 * @return
	 */
	@AutoLog(value = "收料申请单-编辑")
	@ApiOperation(value="收料申请单-编辑", notes="收料申请单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmmatreceiptreqPage scmmatreceiptreqPage) {
		Scmmatreceiptreq scmmatreceiptreq = new Scmmatreceiptreq();
		BeanUtils.copyProperties(scmmatreceiptreqPage, scmmatreceiptreq);
		Scmmatreceiptreq scmmatreceiptreqEntity = scmmatreceiptreqService.getById(scmmatreceiptreq.getId());
		if(scmmatreceiptreqEntity==null) {
			return Result.error("未找到对应数据");
		}
		scmmatreceiptreqService.updateMain(scmmatreceiptreq, scmmatreceiptreqPage.getScmmatreceiptreqitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "收料申请单-通过id删除")
	@ApiOperation(value="收料申请单-通过id删除", notes="收料申请单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmmatreceiptreqService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "收料申请单-批量删除")
	@ApiOperation(value="收料申请单-批量删除", notes="收料申请单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmmatreceiptreqService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "收料申请单-通过id查询")
	@ApiOperation(value="收料申请单-通过id查询", notes="收料申请单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmmatreceiptreq> queryById(@RequestParam(name="id",required=true) String id) {
		Scmmatreceiptreq scmmatreceiptreq = scmmatreceiptreqService.getById(id);
		if(scmmatreceiptreq==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmmatreceiptreq);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "收料申请单分录通过主表ID查询")
	@ApiOperation(value="收料申请单分录主表ID查询", notes="收料申请单分录-通主表ID查询")
	@GetMapping(value = "/queryScmmatreceiptreqitemByMainId")
	public Result<List<Scmmatreceiptreqitem>> queryScmmatreceiptreqitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmmatreceiptreqitem> scmmatreceiptreqitemList = scmmatreceiptreqitemService.selectByMainId(id);
		return Result.OK(scmmatreceiptreqitemList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmmatreceiptreq
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmmatreceiptreq scmmatreceiptreq) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmmatreceiptreq> queryWrapper = QueryGenerator.initQueryWrapper(scmmatreceiptreq, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmmatreceiptreq> scmmatreceiptreqList = scmmatreceiptreqService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmmatreceiptreqPage> pageList = new ArrayList<ScmmatreceiptreqPage>();
      for (Scmmatreceiptreq main : scmmatreceiptreqList) {
          ScmmatreceiptreqPage vo = new ScmmatreceiptreqPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmmatreceiptreqitem> scmmatreceiptreqitemList = scmmatreceiptreqitemService.selectByMainId(main.getId());
          vo.setScmmatreceiptreqitemList(scmmatreceiptreqitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "收料申请单列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmmatreceiptreqPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收料申请单数据", "导出人:"+sysUser.getRealname(), "收料申请单"));
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
              List<ScmmatreceiptreqPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmmatreceiptreqPage.class, params);
              for (ScmmatreceiptreqPage page : list) {
                  Scmmatreceiptreq po = new Scmmatreceiptreq();
                  BeanUtils.copyProperties(page, po);
                  scmmatreceiptreqService.saveMain(po, page.getScmmatreceiptreqitemList());
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
		 scmmatreceiptreqService.updatePoStatus(Arrays.asList(split),flag);
		 return Result.ok("更新成功");
	 }


	 /*
	  * 保存收货记录
	  * */
	 @PostMapping("savebarcodevendorinfo")
	 public Result<String> saveBarCodeVendorInfo(@RequestBody  String ids){
		 String[] split = ids.split(",");
		 scmmatreceiptreqService.saveBarCodeVendorInfo(Arrays.asList(split));
		 return Result.ok("生成成功");
	 }

}
