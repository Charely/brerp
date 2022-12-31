package org.br.erp.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.br.erp.base.entity.IMperiod;
import org.br.erp.base.entity.Mvperiod;
import org.br.erp.base.service.IIMperiodService;
import org.br.erp.base.service.IMvperiodService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
* @Description: 库存期间
* @Author: jeecg-boot
* @Date:   2022-11-27
* @Version: V1.0
*/
@Api(tags="库存期间")
@RestController
@RequestMapping("/base/imperiod")
@Slf4j
public class IMperiodController extends JeecgController<Mvperiod, IMvperiodService> {
   @Autowired
   private IIMperiodService mvperiodService;

   /**
    * 分页列表查询
    *
    * @param mvperiod
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   //@AutoLog(value = "库存期间-分页列表查询")
   @ApiOperation(value="库存期间-分页列表查询", notes="库存期间-分页列表查询")
   @GetMapping(value = "/list")
   public Result<IPage<IMperiod>> queryPageList(IMperiod mvperiod,
                                                       @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       HttpServletRequest req) {
       Map<String, String[]> parameterMap = new HashMap<>(req.getParameterMap());
       if(parameterMap.containsKey("column")){
           parameterMap.remove("column");
       }

       QueryWrapper<IMperiod> queryWrapper = QueryGenerator.initQueryWrapper(mvperiod, parameterMap);
       if(!(mvperiod.getCompanyid()!=null  && !mvperiod.getCompanyid().equalsIgnoreCase(""))){
           return  Result.OK("");
       }
       Page<IMperiod> page = new Page<IMperiod>(pageNo, pageSize);
       queryWrapper.orderByAsc("perioddate");
       IPage<IMperiod> pageList = mvperiodService.page(page, queryWrapper);
       return Result.OK(pageList);
   }


   /**
    *   添加
    *
    * @param mvperiod
    * @return
    */
   @AutoLog(value = "库存期间-添加")
   @ApiOperation(value="库存期间-添加", notes="库存期间-添加")
   //@RequiresPermissions("org.br.erp:IMperiod:add")
   @PostMapping(value = "/add")
   public Result<String> add(@RequestBody IMperiod mvperiod) {
       mvperiodService.save(mvperiod);
       return Result.OK("添加成功！");
   }

   @GetMapping("/addperiod")
   public Result<String> addPeriod(@RequestParam("companyid")String companyid){
       mvperiodService.addIMPerioid(companyid);
       return Result.ok("查询成功");
   }


   @GetMapping("/changeperiodstate")
   public Result<String> changePeriodState(@RequestParam("companyid")String companyid,@RequestParam("perioddate")String perioddate,
                                           @RequestParam("periodstate") String periodState){
       mvperiodService.changeIMPeriodState(companyid,perioddate,periodState);
       return Result.OK("更新成功");
   }

   /**
    *  编辑
    *
    * @param mvperiod
    * @return
    */
   @AutoLog(value = "库存期间-编辑")
   @ApiOperation(value="库存期间-编辑", notes="库存期间-编辑")
   //@RequiresPermissions("org.br.erp:mvperiod:edit")
   @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
   public Result<String> edit(@RequestBody IMperiod mvperiod) {
       mvperiodService.updateById(mvperiod);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "库存期间-通过id删除")
   @ApiOperation(value="库存期间-通过id删除", notes="库存期间-通过id删除")
   //@RequiresPermissions("org.br.erp:mvperiod:delete")
   @DeleteMapping(value = "/delete")
   public Result<String> delete(@RequestParam(name="id",required=true) String id) {
       mvperiodService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "库存期间-批量删除")
   @ApiOperation(value="库存期间-批量删除", notes="库存期间-批量删除")
   //@RequiresPermissions("org.br.erp:mvperiod:deleteBatch")
   @DeleteMapping(value = "/deleteBatch")
   public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.mvperiodService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   //@AutoLog(value = "库存期间-通过id查询")
   @ApiOperation(value="库存期间-通过id查询", notes="库存期间-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<IMperiod> queryById(@RequestParam(name="id",required=true) String id) {
       IMperiod mvperiod = mvperiodService.getById(id);
       if(mvperiod==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(mvperiod);
   }

//   /**
//   * 导出excel
//   *
//   * @param request
//   * @param mvperiod
//   */
//   //@RequiresPermissions("org.br.erp:mvperiod:exportXls")
//   @RequestMapping(value = "/exportXls")
//   public ModelAndView exportXls(HttpServletRequest request, IMperiod mvperiod) {
//       return super.exportXls(request, mvperiod, Mvperiod.class, "库存期间");
//   }
//
//   /**
//     * 通过excel导入数据
//   *
//   * @param request
//   * @param response
//   * @return
//   */
//   //@RequiresPermissions("mvperiod:importExcel")
//   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//       return super.importExcel(request, response, IMperiod.class);
//   }

}
