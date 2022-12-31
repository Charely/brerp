package org.br.erp.inventory.base.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.br.erp.inventory.base.entity.Scminventorytype;
import org.br.erp.inventory.base.service.IScminventorytypeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 库存单据类型
 * @Author: jeecg-boot
 * @Date:   2022-10-23
 * @Version: V1.0
 */
@Api(tags="库存单据类型")
@RestController
@RequestMapping("/inventory.base/scminventorytype")
@Slf4j
public class ScminventorytypeController extends JeecgController<Scminventorytype, IScminventorytypeService> {
	@Autowired
	private IScminventorytypeService scmstocktypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scmstocktype
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "库存单据类型-分页列表查询")
	@ApiOperation(value="库存单据类型-分页列表查询", notes="库存单据类型-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scminventorytype>> queryPageList(Scminventorytype scmstocktype,
														 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
														 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
														 HttpServletRequest req) {
		QueryWrapper<Scminventorytype> queryWrapper = QueryGenerator.initQueryWrapper(scmstocktype, req.getParameterMap());
		Page<Scminventorytype> page = new Page<Scminventorytype>(pageNo, pageSize);
		IPage<Scminventorytype> pageList = scmstocktypeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmstocktype
	 * @return
	 */
	@AutoLog(value = "库存单据类型-添加")
	@ApiOperation(value="库存单据类型-添加", notes="库存单据类型-添加")
	//@RequiresPermissions("org.br.erp:scmstocktype:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scminventorytype scmstocktype) {
		scmstocktypeService.save(scmstocktype);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param scmstocktype
	 * @return
	 */
	@AutoLog(value = "库存单据类型-编辑")
	@ApiOperation(value="库存单据类型-编辑", notes="库存单据类型-编辑")
	//@RequiresPermissions("org.br.erp:scmstocktype:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scminventorytype scmstocktype) {
		scmstocktypeService.updateById(scmstocktype);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "库存单据类型-通过id删除")
	@ApiOperation(value="库存单据类型-通过id删除", notes="库存单据类型-通过id删除")
	//@RequiresPermissions("org.br.erp:scmstocktype:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmstocktypeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "库存单据类型-批量删除")
	@ApiOperation(value="库存单据类型-批量删除", notes="库存单据类型-批量删除")
	//@RequiresPermissions("org.br.erp:scmstocktype:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmstocktypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "库存单据类型-通过id查询")
	@ApiOperation(value="库存单据类型-通过id查询", notes="库存单据类型-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scminventorytype> queryById(@RequestParam(name="id",required=true) String id) {
		Scminventorytype scmstocktype = scmstocktypeService.getById(id);
		if(scmstocktype==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmstocktype);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scmstocktype
    */
    //@RequiresPermissions("org.br.erp:scmstocktype:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scminventorytype scmstocktype) {
        return super.exportXls(request, scmstocktype, Scminventorytype.class, "库存单据类型");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmstocktype:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scminventorytype.class);
    }

}
