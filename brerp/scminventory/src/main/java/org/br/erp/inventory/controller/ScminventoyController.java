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
import javax.websocket.server.PathParam;

import org.br.erp.inventory.base.entity.Scminventorytype;
import org.br.erp.inventory.base.service.IScminventorytypeService;
import org.br.erp.inventory.entity.Scminventoryitem;
import org.br.erp.inventory.vo.ScmPoInvoiceReferInstock;
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
import org.br.erp.inventory.entity.Scminventoy;
import org.br.erp.inventory.vo.ScminventoyPage;
import org.br.erp.inventory.service.IScminventoyService;
import org.br.erp.inventory.service.IScminventoyitemService;
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
 * @Description: 库存单据
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Api(tags="库存单据")
@RestController
@RequestMapping("/inventory/scminventoy")
@Slf4j
public class ScminventoyController {
	@Autowired
	private IScminventoyService scminventoyService;
	@Autowired
	private IScminventoyitemService scminventoyitemService;

	@Autowired
	private IScminventorytypeService scminventorytypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scminventoy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存单据-分页列表查询")
	@ApiOperation(value="库存单据-分页列表查询", notes="库存单据-分页列表查询")
	@GetMapping(value = "/list/{inventorytype}")
	public Result<IPage<Scminventoy>> queryPageList(@PathVariable String inventorytype, Scminventoy scminventoy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		scminventoy.setInventorytype("");
		QueryWrapper<Scminventoy> queryWrapper = QueryGenerator.initQueryWrapper(scminventoy, req.getParameterMap());
		Page<Scminventoy> page = new Page<Scminventoy>(pageNo, pageSize);
		if(inventorytype.equalsIgnoreCase("out")){
			//出库
			QueryWrapper<Scminventorytype> scminventorytypeQueryWrapper=new QueryWrapper<>();
			//scminventorytypeQueryWrapper.eq("instock","false").or().eq("instock",null);
			scminventorytypeQueryWrapper.eq("outstock","true");
			List<Scminventorytype> list = scminventorytypeService.list(scminventorytypeQueryWrapper);
			if(list!=null && list.size()>0) {
				if(list!=null && list.size()>0) {
					List<String> ids=new ArrayList<>();
					for (Scminventorytype scminventorytype : list) {
						ids.add(scminventorytype.getTypecode());
					}
					queryWrapper.in("inventorytype",ids);
				}
			}
		}else if(inventorytype.equalsIgnoreCase("in")){
			//入库
			QueryWrapper<Scminventorytype> scminventorytypeQueryWrapper=new QueryWrapper<>();
			scminventorytypeQueryWrapper.eq("instock","true");
//			scminventorytypeQueryWrapper.eq("outstock","false").or().eq("outstock",null);
			List<Scminventorytype> list = scminventorytypeService.list(scminventorytypeQueryWrapper);
			if(list!=null && list.size()>0) {
				List<String> ids=new ArrayList<>();
				for (Scminventorytype scminventorytype : list) {
					ids.add(scminventorytype.getTypecode());
				}
				queryWrapper.in("inventorytype",ids);
			}
		}
		IPage<Scminventoy> pageList = scminventoyService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scminventoyPage
	 * @return
	 */
	@AutoLog(value = "库存单据-添加")
	@ApiOperation(value="库存单据-添加", notes="库存单据-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Map scminventoyPage) {
//		Scminventoy scminventoy = new Scminventoy();
//		BeanUtils.copyProperties(scminventoyPage, scminventoy);
		scminventoyService.saveMain(scminventoyPage);
		return Result.OK("添加成功！");
	}

	@GetMapping("/create1000")
	public Result<String> create1000item(@RequestParam(value = "id") String id){
		scminventoyService.create1000item(id);
		return Result.ok("成功");
	}
	
	/**
	 *  编辑
	 *
	 * @param scminventoyPage
	 * @return
	 */
	@AutoLog(value = "库存单据-编辑")
	@ApiOperation(value="库存单据-编辑", notes="库存单据-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScminventoyPage scminventoyPage) {
		Scminventoy scminventoy = new Scminventoy();
		BeanUtils.copyProperties(scminventoyPage, scminventoy);
		Scminventoy scminventoyEntity = scminventoyService.getById(scminventoy.getId());
		if(scminventoyEntity==null) {
			return Result.error("未找到对应数据");
		}
		scminventoyService.updateMain(scminventoy, scminventoyPage.getScminventoyitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存单据-通过id删除")
	@ApiOperation(value="库存单据-通过id删除", notes="库存单据-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scminventoyService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存单据-批量删除")
	@ApiOperation(value="库存单据-批量删除", notes="库存单据-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scminventoyService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存单据-通过id查询")
	@ApiOperation(value="库存单据-通过id查询", notes="库存单据-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scminventoy> queryById(@RequestParam(name="id",required=true) String id) {
		Scminventoy scminventoy = scminventoyService.getById(id);
		if(scminventoy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scminventoy);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存单据分录-通过主表ID查询")
	@ApiOperation(value="库存单据分录-通过主表ID查询", notes="库存单据分录-通过主表ID查询")
	@GetMapping(value = "/queryScminventoyitemByMainId")
	public Result<IPage<Scminventoryitem>> queryScminventoyitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scminventoryitem> scminventoyitemList = scminventoyitemService.selectByMainId(id);
		IPage <Scminventoryitem> page = new Page<>();
		page.setRecords(scminventoyitemList);
		page.setTotal(scminventoyitemList.size());
		return Result.OK(page);
	}


	 /**
	  * 获取采购入库单分录信息
	  * @param ids
	  * @return
	  */
	 @GetMapping(value = "/queryscminventoryitembyids")
	public Result<List<Scminventoryitem>> queryScminventoryItemListsByMainids(@RequestParam(name = "ids")String ids){
		 List<Scminventoryitem> scminventoryitems = scminventoyitemService.listByIds(Arrays.asList(ids.split(",")));
		 return Result.ok(scminventoryitems);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param scminventoy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scminventoy scminventoy) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scminventoy> queryWrapper = QueryGenerator.initQueryWrapper(scminventoy, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scminventoy>  scminventoyList = scminventoyService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScminventoyPage> pageList = new ArrayList<ScminventoyPage>();
      for (Scminventoy main : scminventoyList) {
          ScminventoyPage vo = new ScminventoyPage();
          BeanUtils.copyProperties(main, vo);
          List<Scminventoryitem> scminventoyitemList = scminventoyitemService.selectByMainId(main.getId());
          vo.setScminventoyitemList(scminventoyitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "库存单据列表");
      mv.addObject(NormalExcelConstants.CLASS, ScminventoyPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("库存单据数据", "导出人:"+sysUser.getRealname(), "库存单据"));
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
              List<ScminventoyPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScminventoyPage.class, params);
              for (ScminventoyPage page : list) {
                  Scminventoy po = new Scminventoy();
                  BeanUtils.copyProperties(page, po);
                  //scminventoyService.saveMain(po, page.getScminventoyitemList());
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
		 scminventoyService.updatePoStatus(Arrays.asList(split),flag);
		 return Result.OK("更新成功");
	 }


	 @GetMapping("/referinstock")
	 public Result<List<ScmPoInvoiceReferInstock>> getReferInstockData(HttpServletRequest request){
		 Map<String, String[]> parameterMap = request.getParameterMap();
		 List<ScmPoInvoiceReferInstock> referInstockData = scminventoyService.getReferInstockData(parameterMap);
		 return Result.ok(referInstockData);
	 }

}
