package org.br.erp.pr.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.br.erp.pr.entity.Scmpreq;
import org.br.erp.pr.entity.ScmpreqVo;
import org.br.erp.pr.entity.Scmpreqitem;
import org.br.erp.pr.service.IScmpreqService;
import org.br.erp.pr.service.IScmpreqitemService;
import org.br.erp.pr.vo.ScmpreqPage;
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
* @Description: 采购申请
* @Author: jeecg-boot
* @Date:   2022-07-27
* @Version: V1.0
*/
@Api(tags="采购申请")
@RestController
@RequestMapping("/preq/scmpreq")
@Slf4j
public class ScmpreqController {
   @Autowired
   private IScmpreqService scmpreqService;
   @Autowired
   private IScmpreqitemService scmpreqitemService;

   /**
    * 分页列表查询
    *
    * @param scmpreq
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "采购申请-分页列表查询")
   @ApiOperation(value="采购申请-分页列表查询", notes="采购申请-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<Scmpreq>> queryPageList(Scmpreq scmpreq,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
       QueryWrapper<Scmpreq> queryWrapper = QueryGenerator.initQueryWrapper(scmpreq, req.getParameterMap());
      // queryWrapper.orderByDesc("createTime");
       Page<Scmpreq> page = new Page<Scmpreq>(pageNo, pageSize);
       IPage<Scmpreq> pageList = scmpreqService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

    /**
     * 提交审批
     * @param dataid
     * @return
     */
   @GetMapping("/audit")
   public Result<String> auditPreq(@RequestParam(name = "dataid") String dataid){
      // Result res = scmpreqService.audit(dataid);
       return Result.ok("");
   }

   @GetMapping(value = "/all")
   public Result<IPage<ScmpreqVo>> queryPageList(
                                                 @RequestParam(name = "pageNo",defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req){
       Page<ScmpreqVo> page=new Page<>(pageNo,pageSize);
       IPage<ScmpreqVo> scmpreqVoIPage = scmpreqService.selectpregVoPage(page, req.getParameterMap());
       return Result.ok(scmpreqVoIPage);
   }

   /**
    *   添加
    *
    * @param scmpreqPage
    * @return
    */
   @AutoLog(value = "采购申请-添加")
   @ApiOperation(value="采购申请-添加", notes="采购申请-添加")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody ScmpreqPage scmpreqPage) {
       Scmpreq scmpreq = new Scmpreq();
       BeanUtils.copyProperties(scmpreqPage, scmpreq);

       scmpreqService.saveMain(scmpreq, scmpreqPage.getScmpreqitemList());
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param scmpreqPage
    * @return
    */
   @AutoLog(value = "采购申请-编辑")
   @ApiOperation(value="采购申请-编辑", notes="采购申请-编辑")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody ScmpreqPage scmpreqPage) {
       Scmpreq scmpreq = new Scmpreq();
       BeanUtils.copyProperties(scmpreqPage, scmpreq);
       Scmpreq scmpreqEntity = scmpreqService.getById(scmpreq.getId());
       if(scmpreqEntity==null) {
           return Result.error("未找到对应数据");
       }
       scmpreqService.updateMain(scmpreq, scmpreqPage.getScmpreqitemList());
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "采购申请-通过id删除")
   @ApiOperation(value="采购申请-通过id删除", notes="采购申请-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       scmpreqService.delMain(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "采购申请-批量删除")
   @ApiOperation(value="采购申请-批量删除", notes="采购申请-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.scmpreqService.delBatchMain(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功！");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "采购申请-通过id查询")
   @ApiOperation(value="采购申请-通过id查询", notes="采购申请-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<Scmpreq> queryById(@RequestParam(name="id",required=true) String id) {
       Scmpreq scmpreq = scmpreqService.getById(id);
       if(scmpreq==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(scmpreq);

   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "采购申请表体-通过主表ID查询")
   @ApiOperation(value="采购申请表体-通过主表ID查询", notes="采购申请表体-通过主表ID查询")
   @GetMapping(value = "/queryScmpreqitemByMainId")
   public Result<IPage<Scmpreqitem>> queryScmpreqitemListByMainId(@RequestParam(name="id",required=true) String id) {
       List<Scmpreqitem> scmpreqitemList = scmpreqitemService.selectByMainId(id);
       IPage <Scmpreqitem> page = new Page<>();
       page.setRecords(scmpreqitemList);
       page.setTotal(scmpreqitemList.size());
       return Result.OK(page);
   }
   @GetMapping(value = "/queryScmpreqitemById")
   @ApiOperation(value="采购申请表体-通过分录ID查询", notes="采购申请表体-通过分录ID查询")
   public Result<List<ScmpreqitemVo>> queryScmpreqItemById(@RequestParam(name = "ids",required = true) String ids){
       String[] split = ids.split(",");
       List<ScmpreqitemVo> scmpreqitems = scmpreqitemService.selectByIds(Arrays.asList(split));
       return Result.OK(scmpreqitems);
   }
    /**
     * 汇总采购申请
     * @param ids
     * @return
     */
   public Result<IPage<Scmpreq>> getSumPreq(@RequestParam(name = "ids",required = true) String ids){

       //todo 将所有ids分解为列表
       String[] split = ids.split(",");
       List<Scmpreq> scmpreqs = scmpreqService.selectScmpreqList(Arrays.asList(split));
       List<String> headids=new ArrayList<>();
       scmpreqs.forEach(head->{
           if(head.getId()!=null){
               headids.add(head.getId());
           }
       });
       List<Scmpreqitem> scmpreqitems = scmpreqitemService.selectByMainIds(headids);
       Scmpreq newPreq=new Scmpreq();
       Scmpreqitem scmpreqitem=new Scmpreqitem();
       //newPreq.setId();
       return null;
   }

   /**
   * 导出excel
   *
   * @param request
   * @param scmpreq
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, Scmpreq scmpreq) {
     // Step.1 组装查询条件查询数据
     QueryWrapper<Scmpreq> queryWrapper = QueryGenerator.initQueryWrapper(scmpreq, request.getParameterMap());
     LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

    //配置选中数据查询条件
     String selections = request.getParameter("selections");
     if(oConvertUtils.isNotEmpty(selections)) {
          List<String> selectionList = Arrays.asList(selections.split(","));
          queryWrapper.in("id",selectionList);
     }
     //Step.2 获取导出数据
     List<Scmpreq>  scmpreqList = scmpreqService.list(queryWrapper);

     // Step.3 组装pageList
     List<ScmpreqPage> pageList = new ArrayList<ScmpreqPage>();
     for (Scmpreq main : scmpreqList) {
         ScmpreqPage vo = new ScmpreqPage();
         BeanUtils.copyProperties(main, vo);
         List<Scmpreqitem> scmpreqitemList = scmpreqitemService.selectByMainId(main.getId());
         vo.setScmpreqitemList(scmpreqitemList);
         pageList.add(vo);
     }

     // Step.4 AutoPoi 导出Excel
     ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
     mv.addObject(NormalExcelConstants.FILE_NAME, "采购申请列表");
     mv.addObject(NormalExcelConstants.CLASS, ScmpreqPage.class);
     mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购申请数据", "导出人:"+sysUser.getRealname(), "采购申请"));
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
             List<ScmpreqPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmpreqPage.class, params);
             for (ScmpreqPage page : list) {
                 Scmpreq po = new Scmpreq();
                 BeanUtils.copyProperties(page, po);
                 scmpreqService.saveMain(po, page.getScmpreqitemList());
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
