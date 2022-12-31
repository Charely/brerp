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
import org.br.erp.inventory.entity.Scminventorycheckitem;
import org.br.erp.inventory.entity.Scminventorycheck;
import org.br.erp.inventory.vo.ScminventorycheckPage;
import org.br.erp.inventory.service.IScminventorycheckService;
import org.br.erp.inventory.service.IScminventorycheckitemService;
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
 * @Description: 库存盘点单
 * @Author: jeecg-boot
 * @Date:   2022-12-10
 * @Version: V1.0
 */
@Api(tags="库存盘点单")
@RestController
@RequestMapping("/inventory/scminventorycheck")
@Slf4j
public class ScminventorycheckController {
	@Autowired
	private IScminventorycheckService scminventorycheckService;
	@Autowired
	private IScminventorycheckitemService scminventorycheckitemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scminventorycheck
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存盘点单-分页列表查询")
	@ApiOperation(value="库存盘点单-分页列表查询", notes="库存盘点单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scminventorycheck>> queryPageList(Scminventorycheck scminventorycheck,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scminventorycheck> queryWrapper = QueryGenerator.initQueryWrapper(scminventorycheck, req.getParameterMap());
		Page<Scminventorycheck> page = new Page<Scminventorycheck>(pageNo, pageSize);
		IPage<Scminventorycheck> pageList = scminventorycheckService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scminventorycheckPage
	 * @return
	 */
	@AutoLog(value = "库存盘点单-添加")
	@ApiOperation(value="库存盘点单-添加", notes="库存盘点单-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScminventorycheckPage scminventorycheckPage) {
		Scminventorycheck scminventorycheck = new Scminventorycheck();
		BeanUtils.copyProperties(scminventorycheckPage, scminventorycheck);
		scminventorycheckService.saveMain(scminventorycheck, scminventorycheckPage.getScminventorycheckitemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scminventorycheckPage
	 * @return
	 */
	@AutoLog(value = "库存盘点单-编辑")
	@ApiOperation(value="库存盘点单-编辑", notes="库存盘点单-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScminventorycheckPage scminventorycheckPage) {
		Scminventorycheck scminventorycheck = new Scminventorycheck();
		BeanUtils.copyProperties(scminventorycheckPage, scminventorycheck);
		Scminventorycheck scminventorycheckEntity = scminventorycheckService.getById(scminventorycheck.getId());
		if(scminventorycheckEntity==null) {
			return Result.error("未找到对应数据");
		}
		scminventorycheckService.updateMain(scminventorycheck, scminventorycheckPage.getScminventorycheckitemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存盘点单-通过id删除")
	@ApiOperation(value="库存盘点单-通过id删除", notes="库存盘点单-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scminventorycheckService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存盘点单-批量删除")
	@ApiOperation(value="库存盘点单-批量删除", notes="库存盘点单-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scminventorycheckService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存盘点单-通过id查询")
	@ApiOperation(value="库存盘点单-通过id查询", notes="库存盘点单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scminventorycheck> queryById(@RequestParam(name="id",required=true) String id) {
		Scminventorycheck scminventorycheck = scminventorycheckService.getById(id);
		if(scminventorycheck==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scminventorycheck);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "盘点单分录通过主表ID查询")
	@ApiOperation(value="盘点单分录主表ID查询", notes="盘点单分录-通主表ID查询")
	@GetMapping(value = "/queryScminventorycheckitemByMainId")
	public Result<List<Scminventorycheckitem>> queryScminventorycheckitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scminventorycheckitem> scminventorycheckitemList = scminventorycheckitemService.selectByMainId(id);
		return Result.OK(scminventorycheckitemList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scminventorycheck
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scminventorycheck scminventorycheck) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<Scminventorycheck> queryWrapper = QueryGenerator.initQueryWrapper(scminventorycheck, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<Scminventorycheck> scminventorycheckList = scminventorycheckService.list(queryWrapper);

      // Step.3 组装pageList
      List<ScminventorycheckPage> pageList = new ArrayList<ScminventorycheckPage>();
      for (Scminventorycheck main : scminventorycheckList) {
          ScminventorycheckPage vo = new ScminventorycheckPage();
          BeanUtils.copyProperties(main, vo);
          List<Scminventorycheckitem> scminventorycheckitemList = scminventorycheckitemService.selectByMainId(main.getId());
          vo.setScminventorycheckitemList(scminventorycheckitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "库存盘点单列表");
      mv.addObject(NormalExcelConstants.CLASS, ScminventorycheckPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("库存盘点单数据", "导出人:"+sysUser.getRealname(), "库存盘点单"));
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
              List<ScminventorycheckPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScminventorycheckPage.class, params);
              for (ScminventorycheckPage page : list) {
                  Scminventorycheck po = new Scminventorycheck();
                  BeanUtils.copyProperties(page, po);
                  scminventorycheckService.saveMain(po, page.getScminventorycheckitemList());
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
		 scminventorycheckService.updatePoStatus(Arrays.asList(split),flag);
		 return Result.OK("更新成功");
	 }

	 @GetMapping("/inventorycheck")
	 public Result<String> inventorycheck(@RequestParam("ids") String ids){
		 String[] split = ids.split(",");
		 scminventorycheckService.inventorycheck(Arrays.asList(split));
		 return Result.OK("更新成功");
	 }

}
