package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.ResourceUtil;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictModelMany;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.controller.mapper.SysDictItemMapper;
import org.jeecg.modules.system.controller.mapper.SysDictMapper;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictMapper sysDictMapper;
    @Autowired
    private SysDictItemMapper sysDictItemMapper;

	/**
	 * 通过查询指定code 获取字典
	 * @param code
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code", unless = "#result == null ")
	public List<DictModel> queryDictItemsByCode(String code) {
		log.debug("无缓存dictCache的时候调用这里！");
		return sysDictMapper.queryDictItemsByCode(code);
	}

	@Override
	@Cacheable(value = CacheConstant.SYS_ENABLE_DICT_CACHE,key = "#code", unless = "#result == null ")
	public List<DictModel> queryEnableDictItemsByCode(String code) {
		log.debug("无缓存dictCache的时候调用这里！");
		return sysDictMapper.queryEnableDictItemsByCode(code);
	}

	@Override
	public Map<String, List<DictModel>> queryDictItemsByCodeList(List<String> dictCodeList) {
		List<DictModelMany> list = sysDictMapper.queryDictItemsByCodeList(dictCodeList);
		Map<String, List<DictModel>> dictMap = new HashMap(5);
		for (DictModelMany dict : list) {
			List<DictModel> dictItemList = dictMap.computeIfAbsent(dict.getDictCode(), i -> new ArrayList<>());
			dict.setDictCode(null);
			dictItemList.add(new DictModel(dict.getValue(), dict.getText()));
		}
		return dictMap;
	}

	@Override
	public Map<String, List<DictModel>> queryAllDictItems() {
		Map<String, List<DictModel>> res = new HashMap(5);
		List<SysDict> ls = sysDictMapper.selectList(null);
		LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<SysDictItem>();
		queryWrapper.eq(SysDictItem::getStatus, 1);
		queryWrapper.orderByAsc(SysDictItem::getSortOrder);
		List<SysDictItem> sysDictItemList = sysDictItemMapper.selectList(queryWrapper);

		for (SysDict d : ls) {
			List<DictModel> dictModelList = sysDictItemList.stream().filter(s -> d.getId().equals(s.getDictId())).map(item -> {
				DictModel dictModel = new DictModel();
				dictModel.setText(item.getItemText());
				dictModel.setValue(item.getItemValue());
				return dictModel;
			}).collect(Collectors.toList());
			res.put(d.getDictCode(), dictModelList);
		}
		//update-begin-author:taoyan date:2022-7-8 for: 系统字典数据应该包括自定义的java类-枚举
		Map<String, List<DictModel>> enumRes = ResourceUtil.getEnumDictData();
		res.putAll(enumRes);
		//update-end-author:taoyan date:2022-7-8 for: 系统字典数据应该包括自定义的java类-枚举
		log.debug("-------登录加载系统字典-----" + res.toString());
		return res;
	}

	/**
	 * 通过查询指定code 获取字典值text
	 * @param code
	 * @param key
	 * @return
	 */

	@Override
	@Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code+':'+#key", unless = "#result == null ")
	public String queryDictTextByKey(String code, String key) {
		log.debug("无缓存dictText的时候调用这里！");
		return sysDictMapper.queryDictTextByKey(code, key);
	}

	@Override
	public Map<String, List<DictModel>> queryManyDictByKeys(List<String> dictCodeList, List<String> keys) {
		List<DictModelMany> list = sysDictMapper.queryManyDictByKeys(dictCodeList, keys);
		Map<String, List<DictModel>> dictMap = new HashMap(5);
		for (DictModelMany dict : list) {
			List<DictModel> dictItemList = dictMap.computeIfAbsent(dict.getDictCode(), i -> new ArrayList<>());
			dictItemList.add(new DictModel(dict.getValue(), dict.getText()));
		}
		//update-begin-author:taoyan date:2022-7-8 for: 系统字典数据应该包括自定义的java类-枚举
		Map<String, List<DictModel>> enumRes = ResourceUtil.queryManyDictByKeys(dictCodeList, keys);
		dictMap.putAll(enumRes);
		//update-end-author:taoyan date:2022-7-8 for: 系统字典数据应该包括自定义的java类-枚举
		return dictMap;
	}

	/**
	 * 通过查询指定table的 text code 获取字典
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @return
	 */
	@Override
	//@Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
	public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
		log.debug("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCode(table,text,code);
	}

	@Override
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
		log.debug("无缓存dictTableList的时候调用这里！");
		return sysDictMapper.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
	}
	
	/**
	 * 通过查询指定table的 text code 获取字典值text
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @param key
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE, unless = "#result == null ")
	public String queryTableDictTextByKey(String table,String text,String code, String key) {
		log.debug("无缓存dictTable的时候调用这里！");
		return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
	}

	@Override
	public List<DictModel> queryTableDictTextByKeys(String table, String text, String code, List<String> keys) {
		//update-begin-author:taoyan date:20220113 for: @dict注解支持 dicttable 设置where条件
		String filterSql = null;
		if(table.toLowerCase().indexOf(DataBaseConstant.SQL_WHERE)>0){
			String[] arr = table.split(" (?i)where ");
			table = arr[0];
			filterSql = arr[1];
		}
		return sysDictMapper.queryTableDictByKeysAndFilterSql(table, text, code, filterSql, keys);
		//update-end-author:taoyan date:20220113 for: @dict注解支持 dicttable 设置where条件
	}

	@Override
	public List<String> queryTableDictByKeys(String table, String text, String code, String keys) {
		return this.queryTableDictByKeys(table, text, code, keys, true);
	}

	/**
	 * 通过查询指定table的 text code 获取字典，包含text和value
	 * dictTableCache采用redis缓存有效期10分钟
	 * @param table
	 * @param text
	 * @param code
	 * @param keys (逗号分隔)
	 * @param delNotExist 是否移除不存在的项，默认为true，设为false如果某个key不存在数据库中，则直接返回key本身
	 * @return
	 */
	@Override
	//update-begin--Author:lvdandan  Date:20201204 for：JT-36【online】树形列表bug修改后，还是显示原来值 暂时去掉缓存
	//@Cacheable(value = CacheConstant.SYS_DICT_TABLE_BY_KEYS_CACHE)
	//update-end--Author:lvdandan  Date:20201204 for：JT-36【online】树形列表bug修改后，还是显示原来值 暂时去掉缓存
	public List<String> queryTableDictByKeys(String table, String text, String code, String keys, boolean delNotExist) {
		if(oConvertUtils.isEmpty(keys)){
			return null;
		}
		String[] keyArray = keys.split(",");

		//update-begin-author:taoyan date:2022-4-24 for: 下拉搜索组件，表单编辑页面回显下拉搜索的文本的时候，因为表名后配置了条件，导致sql执行失败，
		String filterSql = null;
		if(table.toLowerCase().indexOf("where")!=-1){
			String[] arr = table.split(" (?i)where ");
			table = arr[0];
			filterSql = arr[1];
		}
		List<DictModel> dicts = sysDictMapper.queryTableDictByKeysAndFilterSql(table, text, code, filterSql, Arrays.asList(keyArray));
		//update-end-author:taoyan date:2022-4-24 for: 下拉搜索组件，表单编辑页面回显下拉搜索的文本的时候，因为表名后配置了条件，导致sql执行失败，
		List<String> texts = new ArrayList<>(dicts.size());

		// update-begin--author:sunjianlei--date:20210514--for：新增delNotExist参数，设为false不删除数据库里不存在的key ----
		// 查询出来的顺序可能是乱的，需要排个序
		for (String key : keyArray) {
			List<DictModel> res = dicts.stream().filter(i -> key.equals(i.getValue())).collect(Collectors.toList());
			if (res.size() > 0) {
				texts.add(res.get(0).getText());
			} else if (!delNotExist) {
				texts.add(key);
			}
		}
		// update-end--author:sunjianlei--date:20210514--for：新增delNotExist参数，设为false不删除数据库里不存在的key ----

		return texts;
	}

    /**
     * 根据字典类型id删除关联表中其对应的数据
     */
    @Override
    public boolean deleteByDictId(SysDict sysDict) {
        sysDict.setDelFlag(CommonConstant.DEL_FLAG_1);
        return  this.updateById(sysDict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {
		int insert=0;
    	try{
			 insert = sysDictMapper.insert(sysDict);
			if (sysDictItemList != null) {
				for (SysDictItem entity : sysDictItemList) {
                    //update-begin---author:wangshuai ---date:20220211  for：[JTC-1168]如果字典项值为空，则字典项忽略导入------------
				    if(oConvertUtils.isEmpty(entity.getItemValue())){
				        return -1;
                    }
                    //update-end---author:wangshuai ---date:20220211  for：[JTC-1168]如果字典项值为空，则字典项忽略导入------------
					entity.setDictId(sysDict.getId());
					entity.setStatus(1);
					sysDictItemMapper.insert(entity);
				}
			}
		}catch(Exception e){
			return insert;
		}
		return insert;
    }

	@Override
	public List<DictModel> queryAllDepartBackDictModel() {
		return baseMapper.queryAllDepartBackDictModel();
	}

	@Override
	public List<DictModel> queryAllUserBackDictModel() {
		return baseMapper.queryAllUserBackDictModel();
	}
	
//	@Override
//	public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword) {
//		return baseMapper.queryTableDictItems(table, text, code, "%"+keyword+"%");
//	}

	@Override
	public List<DictModel> queryLittleTableDictItems(String table, String text, String code, String condition, String keyword, int pageSize) {
    	Page<DictModel> page = new Page<DictModel>(1, pageSize);
		page.setSearchCount(false);

		//【issues/3713】字典接口存在SQL注入风险
		SqlInjectionUtil.specialFilterContentForDictSql(code);

		String filterSql = getFilterSql(table, text, code, condition, keyword);
		IPage<DictModel> pageList = baseMapper.queryTableDictWithFilter(page, table, text, code, filterSql);
		return pageList.getRecords();
	}

	/**
	 * 获取条件语句
	 * @param text
	 * @param code
	 * @param condition
	 * @param keyword
	 * @return
	 */
	private String getFilterSql(String table, String text, String code, String condition, String keyword){
		String keywordSql = null, filterSql = "", sqlWhere = " where ";
		// update-begin-author:sunjianlei date:20220112 for: 【JTC-631】判断如果 table 携带了 where 条件，那么就使用 and 查询，防止报错
        if (table.toLowerCase().contains(sqlWhere)) {
            sqlWhere = " and ";
		}
		// update-end-author:sunjianlei date:20220112 for: 【JTC-631】判断如果 table 携带了 where 条件，那么就使用 and 查询，防止报错
		if(oConvertUtils.isNotEmpty(keyword)){
			// 判断是否是多选
			if (keyword.contains(SymbolConstant.COMMA)) {
                //update-begin--author:scott--date:20220105--for：JTC-529【表单设计器】 编辑页面报错，in参数采用双引号导致 ----
				String inKeywords = "'" + String.join("','", keyword.split(",")) + "'";
                //update-end--author:scott--date:20220105--for：JTC-529【表单设计器】 编辑页面报错，in参数采用双引号导致----
				keywordSql = "(" + text + " in (" + inKeywords + ") or " + code + " in (" + inKeywords + "))";
			} else {
				keywordSql = "("+text + " like '%"+keyword+"%' or "+ code + " like '%"+keyword+"%')";
			}
		}
		if(oConvertUtils.isNotEmpty(condition) && oConvertUtils.isNotEmpty(keywordSql)){
			filterSql+= sqlWhere + condition + " and " + keywordSql;
		}else if(oConvertUtils.isNotEmpty(condition)){
			filterSql+= sqlWhere + condition;
		}else if(oConvertUtils.isNotEmpty(keywordSql)){
			filterSql+= sqlWhere + keywordSql;
		}
		return filterSql;
	}
	@Override
	public List<DictModel> queryAllTableDictItems(String table, String text, String code, String condition, String keyword) {
		String filterSql = getFilterSql(table, text, code, condition, keyword);
		List<DictModel> ls = baseMapper.queryAllTableDictItems(table, text, code, filterSql);
    	return ls;
	}

	@Override
	public List<TreeSelectModel> queryTreeList(Map<String, String> query,String table, String text, String code, String pidField,String pid,String hasChildField) {
		return baseMapper.queryTreeList(query, table, text, code, pidField, pid, hasChildField);
	}

	@Override
	public void deleteOneDictPhysically(String id) {
		this.baseMapper.deleteOneById(id);
		this.sysDictItemMapper.delete(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictId,id));
	}

	@Override
	public void updateDictDelFlag(int delFlag, String id) {
		baseMapper.updateDictDelFlag(delFlag,id);
	}

	@Override
	public List<SysDict> queryDeleteList() {
		return baseMapper.queryDeleteList();
	}

	@Override
	public List<DictModel> queryDictTablePageList(DictQuery query, int pageSize, int pageNo) {
		Page page = new Page(pageNo,pageSize,false);
		Page<DictModel> pageList = baseMapper.queryDictTablePageList(page, query);
		return pageList.getRecords();
	}

	@Override
	public List<DictModel> getDictItems(String dictCode) {
		List<DictModel> ls;
		if (dictCode.contains(SymbolConstant.COMMA)) {
			//关联表字典（举例：sys_user,realname,id）
			String[] params = dictCode.split(",");
			if (params.length < 3) {
				// 字典Code格式不正确
				return null;
			}
			//SQL注入校验（只限制非法串改数据库）
			//update-begin-author:taoyan date:2022-7-4 for: issues/I5BNY9 指定带过滤条件的字典table在生成代码后失效
			// 表名后也有可能带条件and语句 不能走filterContent方法
			SqlInjectionUtil.specialFilterContentForDictSql(params[0]);
			final String[] sqlInjCheck = {params[1], params[2]};
			//update-end-author:taoyan date:2022-7-4 for: issues/I5BNY9 指定带过滤条件的字典table在生成代码后失效
			//【issues/3713】字典接口存在SQL注入风险
			SqlInjectionUtil.filterContent(sqlInjCheck);
			if (params.length == 4) {
				// SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
				SqlInjectionUtil.specialFilterContentForDictSql(params[3]);
				ls = this.queryTableDictItemsByCodeAndFilter(params[0], params[1], params[2], params[3]);
			} else if (params.length == 3) {
				ls = this.queryTableDictItemsByCode(params[0], params[1], params[2]);
			} else {
				// 字典Code格式不正确
				return null;
			}
		} else {
			//字典表
			ls = this.queryDictItemsByCode(dictCode);
		}
		return ls;
	}

	@Override
	public List<DictModel> loadDict(String dictCode, String keyword, Integer pageSize) {
		//【issues/3713】字典接口存在SQL注入风险
		SqlInjectionUtil.specialFilterContentForDictSql(dictCode);

		if (dictCode.contains(SymbolConstant.COMMA)) {
			//update-begin-author:taoyan date:20210329 for: 下拉搜索不支持表名后加查询条件
			String[] params = dictCode.split(",");
			String condition = null;
			if (params.length != 3 && params.length != 4) {
				// 字典Code格式不正确
				return null;
			} else if (params.length == 4) {
				condition = params[3];
				// update-begin-author:taoyan date:20220314 for: online表单下拉搜索框表字典配置#{sys_org_code}报错 #3500
				if(condition.indexOf(SymbolConstant.SYS_VAR_PREFIX)>=0){
					condition =  QueryGenerator.getSqlRuleValue(condition);
				}
				// update-end-author:taoyan date:20220314 for: online表单下拉搜索框表字典配置#{sys_org_code}报错 #3500
			}

			// 字典Code格式不正确 [表名为空]
			if(oConvertUtils.isEmpty(params[0])){
				return null;
			}
			List<DictModel> ls;
			if (pageSize != null) {
				ls = this.queryLittleTableDictItems(params[0], params[1], params[2], condition, keyword, pageSize);
			} else {
				ls = this.queryAllTableDictItems(params[0], params[1], params[2], condition, keyword);
			}
			//update-end-author:taoyan date:20210329 for: 下拉搜索不支持表名后加查询条件
			return ls;
		} else {
			// 字典Code格式不正确
			return null;
		}
	}

	@Override
	public void updateTableStatus(String tableName, String statusField, String statusValue, String idField, String idValue) {
		sysDictMapper.updateTableStatus(tableName,statusField,statusValue,idField,idValue);
	}

}
