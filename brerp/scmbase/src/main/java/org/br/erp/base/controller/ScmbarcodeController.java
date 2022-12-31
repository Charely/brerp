package org.br.erp.base.controller;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.br.erp.base.service.writebackapi.IGetVendorPo;
import org.br.erp.entity.po.ScmVendorEntity;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.br.erp.base.entity.Scmbarcode;
import org.br.erp.base.service.IScmbarcodeService;

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
 * @Description: 条码表
 * @Author: jeecg-boot
 * @Date:   2022-10-20
 * @Version: V1.0
 */
@Api(tags="条码表")
@RestController
@RequestMapping("/base/scmbarcode")
@Slf4j
public class ScmbarcodeController extends JeecgController<Scmbarcode, IScmbarcodeService> {

	 public static final String BASE64_PRE = "data:image/png;base64,";


	@Autowired
	private IScmbarcodeService scmbarcodeService;

	
	/**
	 * 分页列表查询
	 *
	 * @param scmbarcode
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "条码表-分页列表查询")
	@ApiOperation(value="条码表-分页列表查询", notes="条码表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scmbarcode>> queryPageList(Scmbarcode scmbarcode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmbarcode> queryWrapper = QueryGenerator.initQueryWrapper(scmbarcode, req.getParameterMap());
		Page<Scmbarcode> page = new Page<Scmbarcode>(pageNo, pageSize);
		IPage<Scmbarcode> pageList = scmbarcodeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scmbarcode
	 * @return
	 */
	@AutoLog(value = "条码表-添加")
	@ApiOperation(value="条码表-添加", notes="条码表-添加")
	//@RequiresPermissions("org.br.erp:scmbarcode:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scmbarcode scmbarcode) {
		scmbarcodeService.createBarCode(scmbarcode);
		scmbarcodeService.updateBarcode(scmbarcode);
		return Result.OK("添加成功！");
	}


	
	/**
	 *  编辑
	 *
	 * @param scmbarcode
	 * @return
	 */
	@AutoLog(value = "条码表-编辑")
	@ApiOperation(value="条码表-编辑", notes="条码表-编辑")
	//@RequiresPermissions("org.br.erp:scmbarcode:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scmbarcode scmbarcode) {
		scmbarcodeService.updateById(scmbarcode);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "条码表-通过id删除")
	@ApiOperation(value="条码表-通过id删除", notes="条码表-通过id删除")
	//@RequiresPermissions("org.br.erp:scmbarcode:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmbarcodeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "条码表-批量删除")
	@ApiOperation(value="条码表-批量删除", notes="条码表-批量删除")
	//@RequiresPermissions("org.br.erp:scmbarcode:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmbarcodeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "条码表-通过id查询")
	@ApiOperation(value="条码表-通过id查询", notes="条码表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scmbarcode> queryById(@RequestParam(name="id",required=true) String id) {
		Scmbarcode scmbarcode = scmbarcodeService.getById(id);
		if(scmbarcode==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scmbarcode);
	}


	@GetMapping("/queryInfoById")
	public Result<ScmVendorEntity> querybyId(@RequestParam(name="id",required = true) String id){
		ScmVendorEntity vendorInfo = scmbarcodeService.getVendorInfo(id);
		return Result.ok(vendorInfo);
	}
	 @ApiOperation(value="条码表-通过id查询", notes="条码表-通过id查询")
	 @GetMapping(value = "/queryByItemId")
	 public Result<String> queryByItemId(@RequestParam(name="id",required=true) String id) {

		QueryWrapper<Scmbarcode> queryWrapper=new QueryWrapper<>();
		String res="";
		queryWrapper.eq("billitemid",id);
		 List<Scmbarcode> list = scmbarcodeService.list(queryWrapper);
		 if(list==null || list.size() ==0) {
			 return Result.error("未找到对应数据");
		 }else {
			 for (Scmbarcode scmbarcode : list) {
				 byte[] barcode = scmbarcode.barcode;
				 if(barcode!=null){
					 String s = Base64.getEncoder().encodeToString(barcode);
					 s.replaceAll("\n","").replaceAll("\r","");
					 res=BASE64_PRE+s;
					 break;
				 }

			 }
		 }
		 if(res.equalsIgnoreCase(""))
		 {
			 return Result.error("没有对应图像");
		 }
		return Result.ok(res);
		 //return Result.OK(list.get(0));
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param scmbarcode
    */
    //@RequiresPermissions("org.br.erp:scmbarcode:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmbarcode scmbarcode) {
        return super.exportXls(request, scmbarcode, Scmbarcode.class, "条码表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("scmbarcode:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scmbarcode.class);
    }

}
