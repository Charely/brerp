package org.br.erp.po.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.br.erp.po.entity.Materialvendorlink;
import org.br.erp.po.entity.Materialvendorlinkitem;
import org.br.erp.po.service.IMaterialvendorlinkService;
import org.br.erp.po.service.IMaterialvendorlinkitemService;
import org.br.erp.po.vo.MaterialvendorlinkPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Description: 货源清单
* @Author: jeecg-boot
* @Date:   2022-08-07
* @Version: V1.0
*/
@Api(tags="货源清单")
@RestController
@RequestMapping("/po/materialvendorlink")
@Slf4j
public class MaterialvendorlinkController {
   @Autowired
   private IMaterialvendorlinkService materialvendorlinkService;
   @Autowired
   private IMaterialvendorlinkitemService materialvendorlinkitemService;

   /**
    * 分页列表查询
    *
    * @param materialvendorlink
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "货源清单-分页列表查询")
   @ApiOperation(value="货源清单-分页列表查询", notes="货源清单-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<Materialvendorlink>> queryPageList(Materialvendorlink materialvendorlink,
                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                          HttpServletRequest req) {
       QueryWrapper<Materialvendorlink> queryWrapper = QueryGenerator.initQueryWrapper(materialvendorlink, req.getParameterMap());
       Page<Materialvendorlink> page = new Page<Materialvendorlink>(pageNo, pageSize);
       IPage<Materialvendorlink> pageList = materialvendorlinkService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

   /**
    *   添加
    *
    * @param materialvendorlinkPage
    * @return
    */
   @AutoLog(value = "货源清单-添加")
   @ApiOperation(value="货源清单-添加", notes="货源清单-添加")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody MaterialvendorlinkPage materialvendorlinkPage) {
       Materialvendorlink materialvendorlink = new Materialvendorlink();
       BeanUtils.copyProperties(materialvendorlinkPage, materialvendorlink);
       materialvendorlinkService.saveMain(materialvendorlink, materialvendorlinkPage.getMaterialvendorlinkitemList());
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param materialvendorlinkPage
    * @return
    */
   @AutoLog(value = "货源清单-编辑")
   @ApiOperation(value="货源清单-编辑", notes="货源清单-编辑")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody MaterialvendorlinkPage materialvendorlinkPage) {
       Materialvendorlink materialvendorlink = new Materialvendorlink();
       BeanUtils.copyProperties(materialvendorlinkPage, materialvendorlink);
       Materialvendorlink materialvendorlinkEntity = materialvendorlinkService.getById(materialvendorlink.getId());
       if(materialvendorlinkEntity==null) {
           return Result.error("未找到对应数据");
       }
       materialvendorlinkService.updateMain(materialvendorlink, materialvendorlinkPage.getMaterialvendorlinkitemList());
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "货源清单-通过id删除")
   @ApiOperation(value="货源清单-通过id删除", notes="货源清单-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       materialvendorlinkService.delMain(id);
       return Result.OK("删除成功!");
   }

   @GetMapping(value = "getVendorLinkItem")
   @ApiOperation(value = "货源清单-根据物料ID查找")
   public Result<List<Materialvendorlinkitem>> getVendorLinkItem(@RequestParam("materialid")String materialid){
       List<Materialvendorlinkitem> vendorItemsByMaterialid = materialvendorlinkService.getVendorItemsByMaterialid(materialid);
       return Result.ok(vendorItemsByMaterialid);
   }


   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "货源清单-批量删除")
   @ApiOperation(value="货源清单-批量删除", notes="货源清单-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.materialvendorlinkService.delBatchMain(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功！");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "货源清单-通过id查询")
   @ApiOperation(value="货源清单-通过id查询", notes="货源清单-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<Materialvendorlink> queryById(@RequestParam(name="id",required=true) String id) {
       Materialvendorlink materialvendorlink = materialvendorlinkService.getById(id);
       if(materialvendorlink==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(materialvendorlink);

   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "货源清单分录-通过主表ID查询")
   @ApiOperation(value="货源清单分录-通过主表ID查询", notes="货源清单分录-通过主表ID查询")
   @GetMapping(value = "/queryMaterialvendorlinkitemByMainId")
   public Result<IPage<Materialvendorlinkitem>> queryMaterialvendorlinkitemListByMainId(@RequestParam(name="id",required=true) String id) {
       List<Materialvendorlinkitem> materialvendorlinkitemList = materialvendorlinkitemService.selectByMainId(id);
       IPage <Materialvendorlinkitem> page = new Page<>();
       page.setRecords(materialvendorlinkitemList);
       page.setTotal(materialvendorlinkitemList.size());
       return Result.OK(page);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param materialvendorlink
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, Materialvendorlink materialvendorlink) {
     // Step.1 组装查询条件查询数据
     QueryWrapper<Materialvendorlink> queryWrapper = QueryGenerator.initQueryWrapper(materialvendorlink, request.getParameterMap());
     LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

    //配置选中数据查询条件
     String selections = request.getParameter("selections");
     if(oConvertUtils.isNotEmpty(selections)) {
          List<String> selectionList = Arrays.asList(selections.split(","));
          queryWrapper.in("id",selectionList);
     }
     //Step.2 获取导出数据
     List<Materialvendorlink>  materialvendorlinkList = materialvendorlinkService.list(queryWrapper);

     // Step.3 组装pageList
     List<MaterialvendorlinkPage> pageList = new ArrayList<MaterialvendorlinkPage>();
     for (Materialvendorlink main : materialvendorlinkList) {
         MaterialvendorlinkPage vo = new MaterialvendorlinkPage();
         BeanUtils.copyProperties(main, vo);
         List<Materialvendorlinkitem> materialvendorlinkitemList = materialvendorlinkitemService.selectByMainId(main.getId());
         vo.setMaterialvendorlinkitemList(materialvendorlinkitemList);
         pageList.add(vo);
     }

     // Step.4 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     mv.addObject(NormalExcelConstants.FILE_NAME, "货源清单列表");
     mv.addObject(NormalExcelConstants.CLASS, MaterialvendorlinkPage.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("货源清单数据", "导出人:"+sysUser.getRealname(), "货源清单"));
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
             List<MaterialvendorlinkPage> list = ExcelImportUtil.importExcel(file.getInputStream(), MaterialvendorlinkPage.class, params);
             for (MaterialvendorlinkPage page : list) {
                 Materialvendorlink po = new Materialvendorlink();
                 BeanUtils.copyProperties(page, po);
                 materialvendorlinkService.saveMain(po, page.getMaterialvendorlinkitemList());
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
