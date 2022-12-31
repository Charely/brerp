package org.jeecg.modules.system.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.SysDictTree;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.security.DictQueryBlackListHandler;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.vo.SysDictPage;
import org.jeecgframework.poi.excel.ExcelImportCheckUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController {

	@Autowired
	private ISysDictService sysDictService;
	@Autowired
	private ISysDictItemService sysDictItemService;
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private DictQueryBlackListHandler dictQueryBlackListHandler;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysDict>> queryPageList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
		Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
		IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
		log.debug("查询当前页："+pageList.getCurrent());
		log.debug("查询当前页数量："+pageList.getSize());
		log.debug("查询结果数量："+pageList.getRecords().size());
		log.debug("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * @功能：获取树形字典数据
	 * @param sysDict
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/treeList", method = RequestMethod.GET)
	public Result<List<SysDictTree>> treeList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<List<SysDictTree>> result = new Result<>();
		LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
		// 构造查询条件
		String dictName = sysDict.getDictName();
		if(oConvertUtils.isNotEmpty(dictName)) {
			query.like(true, SysDict::getDictName, dictName);
		}
		query.orderByDesc(true, SysDict::getCreateTime);
		List<SysDict> list = sysDictService.list(query);
		List<SysDictTree> treeList = new ArrayList<>();
		for (SysDict node : list) {
			treeList.add(new SysDictTree(node));
		}
		result.setSuccess(true);
		result.setResult(treeList);
		return result;
	}

	/**
	 * 获取全部字典数据
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryAllDictItems", method = RequestMethod.GET)
	public Result<?> queryAllDictItems(HttpServletRequest request) {
		Map<String, List<DictModel>> res = new HashMap(5);
		res = sysDictService.queryAllDictItems();
		return Result.ok(res);
	}

	/**
	 * 获取字典数据
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
	public Result<String> getDictText(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
		log.info(" dictCode : "+ dictCode);
		Result<String> result = new Result<String>();
		String text = null;
		try {
			text = sysDictService.queryDictTextByKey(dictCode, key);
			 result.setSuccess(true);
			 result.setResult(text);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}
		return result;
	}


	/**
	 * 获取字典数据 【接口签名验证】
	 * @param dictCode 字典code
	 * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
	 * @return
	 */
	@RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> getDictItems(@PathVariable("dictCode") String dictCode, @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
		log.info(" dictCode : "+ dictCode);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		//update-begin-author:taoyan date:20220317 for: VUEN-222【安全机制】字典接口、online报表、online图表等接口，加一些安全机制
		if(!dictQueryBlackListHandler.isPass(dictCode)){
			return result.error500(dictQueryBlackListHandler.getError());
		}
		//update-end-author:taoyan date:20220317 for: VUEN-222【安全机制】字典接口、online报表、online图表等接口，加一些安全机制
		try {
			List<DictModel> ls = sysDictService.getDictItems(dictCode);
			if (ls == null) {
				result.error500("字典Code格式不正确！");
				return result;
			}
			result.setSuccess(true);
			result.setResult(ls);
			log.debug(result.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
			return result;
		}
		return result;
	}

	/**
	 * 【接口签名验证】
	 * 【JSearchSelectTag下拉搜索组件专用接口】
	 * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
	 * @param dictCode 字典code格式：table,text,code
	 * @return
	 */
	@RequestMapping(value = "/loadDict/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> loadDict(@PathVariable("dictCode") String dictCode,
			@RequestParam(name="keyword",required = false) String keyword,
			@RequestParam(value = "sign",required = false) String sign,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		log.info(" 加载字典表数据,加载关键字: "+ keyword);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		//update-begin-author:taoyan date:20220317 for: VUEN-222【安全机制】字典接口、online报表、online图表等接口，加一些安全机制
		if(!dictQueryBlackListHandler.isPass(dictCode)){
			return result.error500(dictQueryBlackListHandler.getError());
		}
		//update-end-author:taoyan date:20220317 for: VUEN-222【安全机制】字典接口、online报表、online图表等接口，加一些安全机制
		try {
			List<DictModel> ls = sysDictService.loadDict(dictCode, keyword, pageSize);
			if (ls == null) {
				result.error500("字典Code格式不正确！");
				return result;
			}
			result.setSuccess(true);
			result.setResult(ls);
			log.info(result.toString());
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}
	}

	/**
	 * 【接口签名验证】
	 * 【给表单设计器的表字典使用】下拉搜索模式，有值时动态拼接数据
	 * @param dictCode
	 * @param keyword 当前控件的值，可以逗号分割
	 * @param sign
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/loadDictOrderByValue/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> loadDictOrderByValue(
			@PathVariable("dictCode") String dictCode,
			@RequestParam(name = "keyword") String keyword,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		// 首次查询查出来用户选中的值，并且不分页
		Result<List<DictModel>> firstRes = this.loadDict(dictCode, keyword, sign, null);
		if (!firstRes.isSuccess()) {
			return firstRes;
		}
		// 然后再查询出第一页的数据
		Result<List<DictModel>> result = this.loadDict(dictCode, "", sign, pageSize);
		if (!result.isSuccess()) {
			return result;
		}
		// 合并两次查询的数据
		List<DictModel> firstList = firstRes.getResult();
		List<DictModel> list = result.getResult();
		for (DictModel firstItem : firstList) {
			// anyMatch 表示：判断的条件里，任意一个元素匹配成功，返回true
			// allMatch 表示：判断条件里的元素，所有的都匹配成功，返回true
			// noneMatch 跟 allMatch 相反，表示：判断条件里的元素，所有的都匹配失败，返回true
			boolean none = list.stream().noneMatch(item -> item.getValue().equals(firstItem.getValue()));
			// 当元素不存在时，再添加到集合里
			if (none) {
				list.add(0, firstItem);
			}
		}
		return result;
	}

	/**
	 * 【接口签名验证】
	 * 根据字典code加载字典text 返回
	 * @param dictCode 顺序：tableName,text,code
	 * @param keys 要查询的key
	 * @param sign
	 * @param delNotExist 是否移除不存在的项，默认为true，设为false如果某个key不存在数据库中，则直接返回key本身
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadDictItem/{dictCode}", method = RequestMethod.GET)
	public Result<List<String>> loadDictItem(@PathVariable("dictCode") String dictCode,@RequestParam(name="key") String keys, @RequestParam(value = "sign",required = false) String sign,@RequestParam(value = "delNotExist",required = false,defaultValue = "true") boolean delNotExist,HttpServletRequest request) {
		Result<List<String>> result = new Result<>();
		//update-begin-author:taoyan date:20220317 for: VUEN-222【安全机制】字典接口、online报表、online图表等接口，加一些安全机制
		if(!dictQueryBlackListHandler.isPass(dictCode)){
			return result.error500(dictQueryBlackListHandler.getError());
		}
		//update-end-author:taoyan date:20220317 for: VUEN-222【安全机制】字典接口、online报表、online图表等接口，加一些安全机制
		try {
			if(dictCode.indexOf(SymbolConstant.COMMA)!=-1) {
				String[] params = dictCode.split(SymbolConstant.COMMA);
				if(params.length!=3) {
					result.error500("字典Code格式不正确！");
					return result;
				}
				List<String> texts = sysDictService.queryTableDictByKeys(params[0], params[1], params[2], keys, delNotExist);

				result.setSuccess(true);
				result.setResult(texts);
				log.info(result.toString());
			}else {
				result.error500("字典Code格式不正确！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}

	/**
	 * 【接口签名验证】
	 * 根据表名——显示字段-存储字段 pid 加载树形数据
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
	public Result<List<TreeSelectModel>> loadTreeData(@RequestParam(name="pid") String pid,@RequestParam(name="pidField") String pidField,
												  @RequestParam(name="tableName") String tbname,
												  @RequestParam(name="text") String text,
												  @RequestParam(name="code") String code,
												  @RequestParam(name="hasChildField") String hasChildField,
												  @RequestParam(name="condition") String condition,
												  @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
		Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
		Map<String, String> query = null;
		if(oConvertUtils.isNotEmpty(condition)) {
			query = JSON.parseObject(condition, Map.class);
		}
		// SQL注入漏洞 sign签名校验(表名,label字段,val字段,条件)
		String dictCode = tbname+","+text+","+code+","+condition;
        SqlInjectionUtil.filterContent(dictCode);
		List<TreeSelectModel> ls = sysDictService.queryTreeList(query,tbname, text, code, pidField, pid,hasChildField);
		result.setSuccess(true);
		result.setResult(ls);
		return result;
	}

	/**
	 * 【APP接口】根据字典配置查询表字典数据（目前暂未找到调用的地方）
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Deprecated
	@GetMapping("/queryTableData")
	public Result<List<DictModel>> queryTableData(DictQuery query,
												  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
												  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
												  @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request){
		Result<List<DictModel>> res = new Result<List<DictModel>>();
		// SQL注入漏洞 sign签名校验
		String dictCode = query.getTable()+","+query.getText()+","+query.getCode();
        SqlInjectionUtil.filterContent(dictCode);
		List<DictModel> ls = this.sysDictService.queryDictTablePageList(query,pageSize,pageNo);
		res.setResult(ls);
		res.setSuccess(true);
		return res;
	}

	/**
	 * @功能：新增
	 * @param sysDict
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysDict> add(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		try {
			sysDict.setCreateTime(new Date());
			sysDict.setDelFlag(CommonConstant.DEL_FLAG_0);
			sysDictService.save(sysDict);
			result.success("保存成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * @功能：编辑
	 * @param sysDict
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/edit", method = { RequestMethod.PUT,RequestMethod.POST })
	public Result<SysDict> edit(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		SysDict sysdict = sysDictService.getById(sysDict.getId());
		if(sysdict==null) {
			result.error500("未找到对应实体");
		}else {
			sysDict.setUpdateTime(new Date());
			boolean ok = sysDictService.updateById(sysDict);
			if(ok) {
				result.success("编辑成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：删除
	 * @param id
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value={CacheConstant.SYS_DICT_CACHE, CacheConstant.SYS_ENABLE_DICT_CACHE}, allEntries=true)
	public Result<SysDict> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysDict> result = new Result<SysDict>();
		boolean ok = sysDictService.removeById(id);
		if(ok) {
			result.success("删除成功!");
		}else{
			result.error500("删除失败!");
		}
		return result;
	}

	/**
	 * @功能：批量删除
	 * @param ids
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value= {CacheConstant.SYS_DICT_CACHE, CacheConstant.SYS_ENABLE_DICT_CACHE}, allEntries=true)
	public Result<SysDict> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDict> result = new Result<SysDict>();
		if(oConvertUtils.isEmpty(ids)) {
			result.error500("参数不识别！");
		}else {
			sysDictService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * @功能：刷新缓存
	 * @return
	 */
	@RequestMapping(value = "/refleshCache")
	public Result<?> refleshCache() {
		Result<?> result = new Result<SysDict>();
		//清空字典缓存
		Set keys = redisTemplate.keys(CacheConstant.SYS_DICT_CACHE + "*");
		Set keys7 = redisTemplate.keys(CacheConstant.SYS_ENABLE_DICT_CACHE + "*");
		Set keys2 = redisTemplate.keys(CacheConstant.SYS_DICT_TABLE_CACHE + "*");
		Set keys21 = redisTemplate.keys(CacheConstant.SYS_DICT_TABLE_BY_KEYS_CACHE + "*");
		Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE + "*");
		Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE + "*");
		Set keys5 = redisTemplate.keys( "jmreport:cache:dict*");
		Set keys6 = redisTemplate.keys( "jmreport:cache:dictTable*");
		redisTemplate.delete(keys);
		redisTemplate.delete(keys2);
		redisTemplate.delete(keys21);
		redisTemplate.delete(keys3);
		redisTemplate.delete(keys4);
		redisTemplate.delete(keys5);
		redisTemplate.delete(keys6);
		redisTemplate.delete(keys7);
		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(SysDict sysDict,HttpServletRequest request) {
		// Step.1 组装查询条件
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<SysDictPage> pageList = new ArrayList<SysDictPage>();

		List<SysDict> sysDictList = sysDictService.list(queryWrapper);
		for (SysDict dictMain : sysDictList) {
			SysDictPage vo = new SysDictPage();
			BeanUtils.copyProperties(dictMain, vo);
			// 查询机票
			List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
			vo.setSysDictItemList(sysDictItemList);
			pageList.add(vo);
		}

		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "数据字典");
		// 注解对象Class
		mv.addObject(NormalExcelConstants.CLASS, SysDictPage.class);
		// 自定义表格参数
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据字典列表", "导出人:"+user.getRealname(), "数据字典"));
		// 导出数据列表
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param
	 * @return
	 */
	//@RequiresRoles({"admin"})
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
 		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
			MultipartFile file = entity.getValue();
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				//导入Excel格式校验，看匹配的字段文本概率
				Boolean t = ExcelImportCheckUtil.check(file.getInputStream(), SysDictPage.class, params);
				if(t!=null && !t){
					throw new RuntimeException("导入Excel校验失败 ！");
				}
				List<SysDictPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysDictPage.class, params);
				// 错误信息
				List<String> errorMessage = new ArrayList<>();
				int successLines = 0, errorLines = 0;
				for (int i=0;i< list.size();i++) {
					SysDict po = new SysDict();
					BeanUtils.copyProperties(list.get(i), po);
					po.setDelFlag(CommonConstant.DEL_FLAG_0);
					try {
						Integer integer = sysDictService.saveMain(po, list.get(i).getSysDictItemList());
						if(integer>0){
							successLines++;
                        //update-begin---author:wangshuai ---date:20220211  for：[JTC-1168]如果字典项值为空，则字典项忽略导入------------
						}else if(integer == -1){
                            errorLines++;
                            errorMessage.add("字典名称：" + po.getDictName() + "，对应字典列表的字典项值不能为空，忽略导入。");
                        }else{
                        //update-end---author:wangshuai ---date:20220211  for：[JTC-1168]如果字典项值为空，则字典项忽略导入------------
							errorLines++;
							int lineNumber = i + 1;
                            //update-begin---author:wangshuai ---date:20220209  for：[JTC-1168]字典编号不能为空------------
                            if(oConvertUtils.isEmpty(po.getDictCode())){
                                errorMessage.add("第 " + lineNumber + " 行：字典编码不能为空，忽略导入。");
                            }else{
                                errorMessage.add("第 " + lineNumber + " 行：字典编码已经存在，忽略导入。");
                            }
                            //update-end---author:wangshuai ---date:20220209  for：[JTC-1168]字典编号不能为空------------
                        }
					}  catch (Exception e) {
						errorLines++;
						int lineNumber = i + 1;
						errorMessage.add("第 " + lineNumber + " 行：字典编码已经存在，忽略导入。");
					}
				}
				return ImportExcelUtil.imporReturnRes(errorLines,successLines,errorMessage);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Result.error("文件导入失败:"+e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}


	/**
	 * 查询被删除的列表
	 * @return
	 */
	@RequestMapping(value = "/deleteList", method = RequestMethod.GET)
	public Result<List<SysDict>> deleteList() {
		Result<List<SysDict>> result = new Result<List<SysDict>>();
		List<SysDict> list = this.sysDictService.queryDeleteList();
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}

	/**
	 * 物理删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deletePhysic/{id}", method = RequestMethod.DELETE)
	public Result<?> deletePhysic(@PathVariable("id") String id) {
		try {
			sysDictService.deleteOneDictPhysically(id);
			return Result.ok("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败!");
		}
	}

	/**
	 * 逻辑删除的字段，进行取回
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/back/{id}", method = RequestMethod.PUT)
	public Result<?> back(@PathVariable("id") String id) {
		try {
			sysDictService.updateDictDelFlag(0,id);
			return Result.ok("操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("操作失败!");
		}
	}

}
