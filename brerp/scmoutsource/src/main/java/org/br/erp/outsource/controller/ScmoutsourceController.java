package org.br.erp.outsource.controller;

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

import com.alibaba.fastjson.JSONObject;
import org.br.erp.base.Config.EventHandler;
import org.br.erp.base.entityvo.ScmOutSourceToOutStockModel;
import org.br.erp.outsource.entity.Scmoutsourceitembom;
import org.br.erp.outsource.vo.ScmReceiptReqReferWwVo;
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
import org.br.erp.outsource.entity.Scmoutsourceitem;
import org.br.erp.outsource.entity.Scmoutsource;
import org.br.erp.outsource.vo.ScmoutsourcePage;
import org.br.erp.outsource.service.IScmoutsourceService;
import org.br.erp.outsource.service.IScmoutsourceitemService;
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
 * @Description: ????????????
 * @Author: jeecg-boot
 * @Date:   2022-11-29
 * @Version: V1.0
 */
@Api(tags="????????????")
@RestController
@RequestMapping("/outsource/scmoutsource")
@Slf4j
public class ScmoutsourceController {
	@Autowired
	private IScmoutsourceService scmoutsourceService;
	@Autowired
	private IScmoutsourceitemService scmoutsourceitemService;


	@Autowired
	private EventHandler eventHandler;
	
	/**
	 * ??????????????????
	 *
	 * @param scmoutsource
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "????????????-??????????????????")
	@ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<IPage<Scmoutsource>> queryPageList(Scmoutsource scmoutsource,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Scmoutsource> queryWrapper = QueryGenerator.initQueryWrapper(scmoutsource, req.getParameterMap());
		Page<Scmoutsource> page = new Page<Scmoutsource>(pageNo, pageSize);
		IPage<Scmoutsource> pageList = scmoutsourceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   ??????
	 *
	 * @param scmoutsourcePage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ScmoutsourcePage scmoutsourcePage) {
		Scmoutsource scmoutsource = new Scmoutsource();
		BeanUtils.copyProperties(scmoutsourcePage, scmoutsource);
		scmoutsourceService.saveMain(scmoutsource, scmoutsourcePage.getScmoutsourceitemList(),scmoutsourcePage);
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param scmoutsourcePage
	 * @return
	 */
	@AutoLog(value = "????????????-??????")
	@ApiOperation(value="????????????-??????", notes="????????????-??????")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ScmoutsourcePage scmoutsourcePage) {
		Scmoutsource scmoutsource = new Scmoutsource();
		BeanUtils.copyProperties(scmoutsourcePage, scmoutsource);
		Scmoutsource scmoutsourceEntity = scmoutsourceService.getById(scmoutsource.getId());
		if(scmoutsourceEntity==null) {
			return Result.error("?????????????????????");
		}
		scmoutsourceService.updateMain(scmoutsource, scmoutsourcePage.getScmoutsourceitemList());
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scmoutsourceService.delMain(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "????????????-????????????")
	@ApiOperation(value="????????????-????????????", notes="????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scmoutsourceService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "????????????-??????id??????")
	@ApiOperation(value="????????????-??????id??????", notes="????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<Scmoutsource> queryById(@RequestParam(name="id",required=true) String id) {
		Scmoutsource scmoutsource = scmoutsourceService.getById(id);
		if(scmoutsource==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(scmoutsource);

	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "??????????????????????????????ID??????")
	@ApiOperation(value="????????????????????????ID??????", notes="??????????????????-?????????ID??????")
	@GetMapping(value = "/queryScmoutsourceitemByMainId")
	public Result<List<Scmoutsourceitem>> queryScmoutsourceitemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<Scmoutsourceitem> scmoutsourceitemList = scmoutsourceitemService.selectByMainId(id);
		return Result.OK(scmoutsourceitemList);
	}

	 @ApiOperation(value="??????????????????-????????????ID??????", notes="??????????????????-????????????ID??????")
	 @GetMapping(value = "/queryScmoursourceItemlistbyids")
	 public Result<List<Scmoutsourceitem>> queryScmpoitemlistByIds(@RequestParam(name="ids",required=true) String ids) {

		 String[] split = ids.split(",");
		 List<Scmoutsourceitem> scmoutsourceitems = scmoutsourceitemService.listByIds(Arrays.asList(split));
		 return Result.OK(scmoutsourceitems);
	 }


	@GetMapping("/queryscmoutsourcebominfo")
	public Result<List<Scmoutsourceitembom>> queryScmoutsourceitemBomListByitemid(@RequestParam(name = "itemid")String itemid){
		List<Scmoutsourceitembom> scmoutsourceitemboms = scmoutsourceitemService.selectBomInfoByMainId(itemid);
		return Result.OK(scmoutsourceitemboms);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param scmoutsource
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scmoutsource scmoutsource) {
      // Step.1 ??????????????????????????????
      QueryWrapper<Scmoutsource> queryWrapper = QueryGenerator.initQueryWrapper(scmoutsource, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //??????????????????????????????
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 ??????????????????
      List<Scmoutsource> scmoutsourceList = scmoutsourceService.list(queryWrapper);

      // Step.3 ??????pageList
      List<ScmoutsourcePage> pageList = new ArrayList<ScmoutsourcePage>();
      for (Scmoutsource main : scmoutsourceList) {
          ScmoutsourcePage vo = new ScmoutsourcePage();
          BeanUtils.copyProperties(main, vo);
          List<Scmoutsourceitem> scmoutsourceitemList = scmoutsourceitemService.selectByMainId(main.getId());
          vo.setScmoutsourceitemList(scmoutsourceitemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????????????????");
      mv.addObject(NormalExcelConstants.CLASS, ScmoutsourcePage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:"+sysUser.getRealname(), "????????????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * ??????excel????????????
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
          // ????????????????????????
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ScmoutsourcePage> list = ExcelImportUtil.importExcel(file.getInputStream(), ScmoutsourcePage.class, params);
              for (ScmoutsourcePage page : list) {
                  Scmoutsource po = new Scmoutsource();
                  BeanUtils.copyProperties(page, po);
                  scmoutsourceService.saveMain(po, page.getScmoutsourceitemList(),page);
              }
              return Result.OK("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("?????????????????????");
    }

	 @GetMapping("/updatestatus")
	 public Result<String> updatePoStatus(@RequestParam("ids") String ids,@RequestParam("flag") String flag){
		 String[] split = ids.split(",");
		 scmoutsourceService.updateStatus(Arrays.asList(split),flag);
		 return Result.OK("????????????");
	 }

	 @PostMapping("/outstock")
	 public Result<String> wwoutStock(@RequestBody String ids){
		 JSONObject jsonObject = (JSONObject) JSON.parse(ids);
		 String ids1 = jsonObject.get("ids").toString();

		 String[] split = ids1.split(",");
		 try {
			 scmoutsourceService.wwoutStock(Arrays.asList(split));
			 return Result.ok("????????????");
		 }catch(Exception exception){
			 return Result.error(exception.getMessage());
		 }

	 }

	 @ApiOperation(value="????????????-??????????????????", notes="????????????-??????????????????")
	 @GetMapping(value = "/receiptreqreferwwlist")
	 public Result<IPage<ScmReceiptReqReferWwVo>> getReceiptReferWwList(ScmReceiptReqReferWwVo scmReceiptReqReferWwVo,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		// QueryWrapper<ScmReceiptReqReferWwVo> queryWrapper = QueryGenerator.initQueryWrapper(scmReceiptReqReferWwVo, req.getParameterMap());
		 Page<ScmReceiptReqReferWwVo> page = new Page<ScmReceiptReqReferWwVo>(pageNo, pageSize);
		 List<ScmReceiptReqReferWwVo> reciptReqReferWw = scmoutsourceService.getReciptReqReferWw(page, req.getParameterMap());
		 page.setRecords(reciptReqReferWw);
		 return Result.OK(page);
	 }



}
