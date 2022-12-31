package org.br.erp.po.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.http.impl.bootstrap.HttpServer;
import org.br.erp.entity.po.ScmVendorEntity;
import org.br.erp.po.entity.Scmpo;
import org.br.erp.po.entity.Scmpoitem;
import org.br.erp.po.service.IScmpoService;
import org.br.erp.po.service.IScmpoitemService;
import org.br.erp.po.vo.ScmMatReceiptReferPoVo;
import org.br.erp.po.vo.ScmVendorReferPoVo;
import org.br.erp.po.vo.ScmpoPage;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 采购订单
 * @Author: jeecg-boot
 * @Date:   2022-08-06
 * @Version: V1.0
 */
@Api(tags="采购订单")
@RestController
@RequestMapping("/po/scmpo")
@Slf4j
public class ScmpoController {
	@Autowired
	private IScmpoService scmpoService;
	@Autowired
	private IScmpoitemService scmpoitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmpo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购订单-分页列表查询")
	@ApiOperation(value="采购订单-分页列表查询", notes="采购订单-分页列表查询")
	@GetMapping(value = "/list/{isred}")
	public Result<IPage<Scmpo>> queryPageList(Scmpo scmpo,
											  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
											  HttpServletRequest req, @PathVariable("isred") String isred) {
		QueryWrapper<Scmpo> queryWrapper = QueryGenerator.initQueryWrapper(scmpo, req.getParameterMap());
		queryWrapper.eq("isred",isred);
		Page<Scmpo> page = new Page<Scmpo>(pageNo, pageSize);
		IPage<Scmpo> pageList = scmpoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	@GetMapping("/referpolist/{redporeferpo}")
	public Result<IPage<ScmMatReceiptReferPoVo>> queryMatReferPo( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																  HttpServletRequest req,
																  @PathVariable("redporeferpo") String redporeferpo)
	{
		QueryWrapper<ScmMatReceiptReferPoVo> queryWrapper=new QueryWrapper<>();

		Map<String, String[]> parameterMap = req.getParameterMap();
		if(parameterMap.containsKey("billcode")){
			String[] billcodes=(String[])parameterMap.get("billcode");
			queryWrapper.like("billcode",billcodes[0]);
		}
		if(parameterMap.containsKey("vendorid")){
			String[] vendorids=(String[])parameterMap.get("vendorid");
			queryWrapper.eq("vendorid",vendorids[0]);
		}

		if(parameterMap.containsKey("materialparam")){
			String[] material=(String[])parameterMap.get("materialparam");
			queryWrapper.like("material.materialcode",material[0]).or().like("material.materialname",material[0]);
		}

		if(parameterMap.containsKey("beginTime") && parameterMap.containsKey("endTime")){
			String[] beginTime=(String[])parameterMap.get("beginTime");
			String[] endTime=(String[])parameterMap.get("endTime");
			queryWrapper.between("podate",beginTime[0],endTime[0]);
		}
		if(redporeferpo!=null && redporeferpo.equalsIgnoreCase("1")){
			//说明是退货订单参照采购订单
			queryWrapper.eq("isred","0");
			queryWrapper.ne("returnqty","poqty");
		}else if(redporeferpo != null && redporeferpo.equalsIgnoreCase("2")){
			//是收货单参照采购订单
			queryWrapper.ne("scmpoitem.receiptreqflag","2");
		} else if(redporeferpo != null && redporeferpo.equalsIgnoreCase("3")){
			//入库红单参照退货采购订单
			queryWrapper.eq("scmpo.isred","1");
			queryWrapper.ne("scmpoitem.receiptflag","2");
		}else if(redporeferpo !=null && redporeferpo.equalsIgnoreCase("4")){
			//采购发票参照订单进行结算
			queryWrapper.ne("scmpoitem.invoiceflag","2");
			queryWrapper.eq("status","2");
			queryWrapper.orderByDesc("billcode");
			Page<ScmMatReceiptReferPoVo> page=new Page<>(pageNo,pageSize);
			List<ScmMatReceiptReferPoVo> referList = scmpoService.getPuinvoiceReferPoList(page, queryWrapper);

			Page<ScmMatReceiptReferPoVo> scmMatReceiptReferPoVoPage = page.setRecords(referList);

			return Result.ok(scmMatReceiptReferPoVoPage);
		}
		queryWrapper.eq("status","2");
		queryWrapper.orderByDesc("billcode");

		Page<ScmMatReceiptReferPoVo> page=new Page<>(pageNo,pageSize);
		List<ScmMatReceiptReferPoVo> referList = scmpoService.getReferList(page, queryWrapper);

		Page<ScmMatReceiptReferPoVo> scmMatReceiptReferPoVoPage = page.setRecords(referList);

		return Result.ok(scmMatReceiptReferPoVoPage);
	}


	 @GetMapping("/vendorreferpolist")
	 public Result<IPage<ScmVendorReferPoVo>> queryVendorReferPoList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																	 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																	 HttpServletRequest req)
	 {
		 Page<ScmVendorReferPoVo> page=new Page<>(pageNo,pageSize);
		 Map<String, String[]> parameterMap = req.getParameterMap();
		 List<ScmVendorReferPoVo> scmVendorReferPoVos = scmpoService.getvendorReferlist(page, parameterMap);


		 Page<ScmVendorReferPoVo> scmMatReceiptReferPoVoPage = page.setRecords(scmVendorReferPoVos);

		 return Result.ok(scmMatReceiptReferPoVoPage);
	 }

	
	/**
	 *   添加
	 *
	 * @param scmpoPage
	 * @return
	 */
	@AutoLog(value = "采购订单-添加")
	@ApiOperation(value="采购订单-添加", notes="采购订单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Map scmpoPage) {

		scmpoService.saveMain(scmpoPage);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmpoPage
	 * @return
	 */
	@AutoLog(value = "采购订单-编辑")
	@ApiOperation(value="采购订单-编辑", notes="采购订单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Map scmpoPage) {
//		Scmpo scmpo = new Scmpo();
//		BeanUtils.copyProperties(scmpoPage, scmpo);
//		Scmpo scmpoEntity = scmpoService.getById(scmpo.getId());
//		if(scmpoEntity==null) {
//			return Result.error("未找到对应数据");
//		}
		scmpoService.updateMain(scmpoPage);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购订单-通过id删除")
	@ApiOperation(value="采购订单-通过id删除", notes="采购订单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmpoService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购订单-批量删除")
	@ApiOperation(value="采购订单-批量删除", notes="采购订单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmpoService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购订单-通过id查询")
	@ApiOperation(value="采购订单-通过id查询", notes="采购订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmpo> queryById(@RequestParam(name="id",required=true) String id) {
		Scmpo scmpo = scmpoService.getById(id);
		if(scmpo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmpo);

	}


	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购订单分录-通过主表ID查询")
	@ApiOperation(value="采购订单分录-通过主表ID查询", notes="采购订单分录-通过主表ID查询")
	@GetMapping(value = "/queryScmpoitemByMainId")
	public Result<IPage<Scmpoitem>> queryScmpoitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmpoitem> scmpoitemList = scmpoitemService.selectByMainId(id);
		IPage <Scmpoitem> page = new Page<>();
		page.setRecords(scmpoitemList);
		page.setTotal(scmpoitemList.size());
		return Result.OK(page);
	}

	 @ApiOperation(value="采购订单分录-通过主表ID查询", notes="采购订单分录-通过主表ID查询")
	 @GetMapping(value = "/queryScmpoitemlistbyids")
	 public Result<List<Scmpoitem>> queryScmpoitemlistByIds(@RequestParam(name="ids",required=true) String ids) {

		 String[] split = ids.split(",");
		 List<Scmpoitem> scmpoitemList = scmpoitemService.listByIds(Arrays.asList(split));
		 return Result.OK(scmpoitemList);
	 }



	 /**
    * 导出excel
    *
    * @param request
    * @param scmpo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmpo scmpo) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scmpo> queryWrapper = QueryGenerator.initQueryWrapper(scmpo, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scmpo>  scmpoList = scmpoService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScmpoPage> pageList = new ArrayList<ScmpoPage>();
      for (Scmpo main : scmpoList) {
          ScmpoPage vo = new ScmpoPage();
          BeanUtils.copyProperties(main, vo);
          List<Scmpoitem> scmpoitemList = scmpoitemService.selectByMainId(main.getId());
          vo.setScmpoitemList(scmpoitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购订单列表");
      mv.addObject(NormalExcelConstants.CLASS, ScmpoPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购订单数据", "导出人:"+sysUser.getRealname(), "采购订单"));
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
              List<ScmpoPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmpoPage.class, params);
              for (ScmpoPage page : list) {
                  Scmpo po = new Scmpo();
                  BeanUtils.copyProperties(page, po);
                 // scmpoService.saveMain(po, page.getScmpoitemList());
				  //scmpoService.save(page);
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

	/**/
	 @GetMapping("/updatestatus")
	public Result<String> updatePoStatus(@RequestParam("ids") String ids,@RequestParam("flag") String flag){
		String[] split = ids.split(",");
		scmpoService.updatePoStatus(Arrays.asList(split),flag);
		return Result.OK("更新成功");
	}


	@GetMapping("/queryvendorinfobyid")
	public Result<ScmVendorEntity> getVendorList(@RequestParam("id")String id){
		ScmVendorEntity vendorList = scmpoService.getVendorList(id);
		return Result.OK(vendorList);
	}


}
