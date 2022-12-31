package org.br.erp.pr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.br.erp.pr.entity.Scmpr;
import org.br.erp.pr.entity.Scmpritem;
import org.br.erp.pr.service.IScmprService;
import org.br.erp.pr.service.IScmpritemService;
import org.br.erp.pr.vo.ScmprPage;
import org.br.erp.pr.vo.ScmprReferVo;
import org.br.erp.pr.vo.ScmpreqitemVo;
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
* @Description: 采购计划
* @Author: jeecg-boot
* @Date:   2022-08-01
* @Version: V1.0
*/
@Api(tags="采购计划")
@RestController
@RequestMapping("/pr/scmpr")
@Slf4j
public class ScmprController {
   @Autowired
   private IScmprService scmprService;
   @Autowired
   private IScmpritemService scmpritemService;

   /**
    * 分页列表查询
    *
    * @param scmpr
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "采购计划-分页列表查询")
   @ApiOperation(value="采购计划-分页列表查询", notes="采购计划-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<Scmpr>> queryPageList(Scmpr scmpr,
                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                             HttpServletRequest req) {
       QueryWrapper<Scmpr> queryWrapper = QueryGenerator.initQueryWrapper(scmpr, req.getParameterMap());
       queryWrapper.orderByDesc("create_time");
       Page<Scmpr> page = new Page<Scmpr>(pageNo, pageSize);
       IPage<Scmpr> pageList = scmprService.page(page, queryWrapper);
       return Result.OK(pageList);
   }
    @GetMapping(value = "/queryscmpritembyids")
    @ApiOperation(value="采购申请表体-通过分录ID查询", notes="采购申请表体-通过分录ID查询")
    public Result<List<Scmpritem>> queryScmpreqItemById(@RequestParam(name = "ids",required = true) String ids){
        String[] split = ids.split(",");
        List<Scmpritem> scmpreqitems = scmpritemService.listByIds(Arrays.asList(split));
        return Result.OK(scmpreqitems);
    }
   /**
    *   添加
    *
    * @param scmprPage
    * @return
    */
   @AutoLog(value = "采购计划-添加")
   @ApiOperation(value="采购计划-添加", notes="采购计划-添加")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody Map scmprPage) {

       scmprService.saveMain(scmprPage);

       return Result.OK("添加成功！");
   }

   @GetMapping("/poreferprlist")
    public Result<IPage<Scmpr>> getPoReferPrList( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                  HttpServletRequest req){

       return Result.OK(null);
    }

   /**
    *  编辑
    *
    * @param scmprPage
    * @return
    */
   @AutoLog(value = "采购计划-编辑")
   @ApiOperation(value="采购计划-编辑", notes="采购计划-编辑")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody Map scmprPage) {
      scmprService.updateMain(scmprPage);
       //scmprService.updateMain(scmprPage,scmpr, scmprPage.getScmpritemList());
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "采购计划-通过id删除")
   @ApiOperation(value="采购计划-通过id删除", notes="采购计划-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       scmprService.delMain(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "采购计划-批量删除")
   @ApiOperation(value="采购计划-批量删除", notes="采购计划-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.scmprService.delBatchMain(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功！");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "采购计划-通过id查询")
   @ApiOperation(value="采购计划-通过id查询", notes="采购计划-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<Scmpr> queryById(@RequestParam(name="id",required=true) String id) {
       Scmpr scmpr = scmprService.getById(id);
       if(scmpr==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(scmpr);

   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "采购计划分录通过主表ID查询")
   @ApiOperation(value="采购计划分录主表ID查询", notes="采购计划分录-通主表ID查询")
   @GetMapping(value = "/queryScmpritemByMainId")
   public Result<List<Scmpritem>> queryScmpritemListByMainId(@RequestParam(name="id",required=true) String id) {
       List<Scmpritem> scmpritemList = scmpritemService.selectByMainId(id);
       return Result.OK(scmpritemList);
   }


    @ApiOperation(value="采购计划分录主表ID查询", notes="采购计划分录-通主表ID查询")
    @GetMapping(value = "/queryScmpritemListByIds")
    public Result<List<Scmpritem>> queryScmpritemListByIds(@RequestParam(name="ids",required=true) String ids) {
        String[] split = ids.split(",");
        List<Scmpritem> scmpritemList = scmpritemService.listByIds(Arrays.asList(split));
        return Result.OK(scmpritemList);
    }

   /**
   * 导出excel
   *
   * @param request
   * @param scmpr
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, Scmpr scmpr) {
     // Step.1 组装查询条件查询数据
     QueryWrapper<Scmpr> queryWrapper = QueryGenerator.initQueryWrapper(scmpr, request.getParameterMap());
     LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
     String selections = request.getParameter("selections");
     if(oConvertUtils.isNotEmpty(selections)) {
        List<String> selectionList = Arrays.asList(selections.split(","));
        queryWrapper.in("id",selectionList);
     }
     //Step.2 获取导出数据
     List<Scmpr> scmprList = scmprService.list(queryWrapper);

     // Step.3 组装pageList
     List<ScmprPage> pageList = new ArrayList<ScmprPage>();
     for (Scmpr main : scmprList) {
         ScmprPage vo = new ScmprPage();
         BeanUtils.copyProperties(main, vo);
         List<Scmpritem> scmpritemList = scmpritemService.selectByMainId(main.getId());
         vo.setScmpritemList(scmpritemList);
         pageList.add(vo);
     }

     // Step.4 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     mv.addObject(NormalExcelConstants.FILE_NAME, "采购计划列表");
     mv.addObject(NormalExcelConstants.CLASS, ScmprPage.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购计划数据", "导出人:"+sysUser.getRealname(), "采购计划"));
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
             List<ScmprPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmprPage.class, params);
             for (ScmprPage page : list) {
                 Scmpr po = new Scmpr();
                 BeanUtils.copyProperties(page, po);
                 //scmprService.saveMain(page,po, page.getScmpritemList());
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


    /**
     * 根据需求日期计算出订货日期
     * @param materialid
     * @param prqty
     * @param preqdate
     * @return
     */
    @GetMapping("/getprdate")
   public Result<String> getPrOrderDate(@RequestParam(name = "materialid")String materialid, @RequestParam(name = "prqty") String prqty, @RequestParam(name = "preqdate")String preqdate){
       String prOrderDate = scmprService.getPrOrderDate(materialid, prqty, preqdate);
       return Result.OK(prOrderDate);
   }

   @GetMapping("/getprreferlist")
   public Result<IPage<ScmprReferVo>> getPrReferList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                    HttpServletRequest req)
   {

       Map<String, String[]> parameterMap = req.getParameterMap();
       Page<ScmprReferVo> page=new Page<>(pageNo,pageSize);
       IPage<ScmprReferVo> referPrVo = scmprService.getReferPrVo(page, parameterMap);
       return Result.ok(referPrVo);
   }

    /**
     * 根据货源清单生成采购订单
     * @param ids
     * @return
     */
   @GetMapping("/createpo")
   public Result<Object> createpo(@RequestParam("ids") String ids){
        //根据采购计划的单据信息生成对应的采购订单
         scmprService.createpo(ids);
         return Result.ok("生成成功");
   }


   @GetMapping("/createpofromvendorlink")
   public Result<Object> createpofromvendorlink(@RequestParam("ids") String ids){
        scmprService.createporfromvendoritem(ids);
        return Result.ok("生成成功");
   }

}
