package org.br.erp.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.br.erp.base.entity.*;
import org.br.erp.base.mapper.ScmcustomformatitemMapper;
import org.br.erp.base.mapper.ScmcustomformatMapper;
import org.br.erp.base.mapper.onlcgformfieldMapper;
import org.br.erp.base.mapper.onlcgformheadMapper;
import org.br.erp.base.service.IScmcustomformatService;
import org.br.erp.base.service.IScmobjectService;
import org.br.erp.base.vo.ColumnVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 自定义格式表
 * @Author: jeecg-boot
 * @Date:   2022-10-28
 * @Version: V1.0
 */
@Service
public class ScmcustomformatServiceImpl extends ServiceImpl<ScmcustomformatMapper, Scmcustomformat> implements IScmcustomformatService {

	@Autowired
	private ScmcustomformatMapper scmcustomformatMapper;
	@Autowired
	private ScmcustomformatitemMapper scmcustomformatitemMapper;

	@Autowired
	private IScmobjectService scmobjectService;

	@Autowired
	private onlcgformheadMapper onlcgformheadMapper;
	@Autowired
	private onlcgformfieldMapper onlcgformfieldMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(Scmcustomformat scmcustomformat, List<Scmcustomformatitem> scmcustomformatitemList) {
		scmcustomformatMapper.insert(scmcustomformat);
		if(scmcustomformatitemList!=null && scmcustomformatitemList.size()>0) {
			for(Scmcustomformatitem entity:scmcustomformatitemList) {
				//外键设置
				entity.setParentid(scmcustomformat.getId());
				scmcustomformatitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(Scmcustomformat scmcustomformat,List<Scmcustomformatitem> scmcustomformatitemList) {
		scmcustomformatMapper.updateById(scmcustomformat);
		
		//1.先删除子表数据
		scmcustomformatitemMapper.deleteByMainId(scmcustomformat.getId());
		
		//2.子表数据重新插入
		if(scmcustomformatitemList!=null && scmcustomformatitemList.size()>0) {
			for(Scmcustomformatitem entity:scmcustomformatitemList) {
				//外键设置
				entity.setParentid(scmcustomformat.getId());
				scmcustomformatitemMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		scmcustomformatitemMapper.deleteByMainId(id);
		scmcustomformatMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			scmcustomformatitemMapper.deleteByMainId(id.toString());
			scmcustomformatMapper.deleteById(id);
		}
	}

	@Override
	public List<ColumnVo> getColumnVoList(String objectCode) {
		List<ColumnVo> res=new ArrayList<>();
		QueryWrapper<Scmobject> scmobjectQueryWrapper=new QueryWrapper<>();
		scmobjectQueryWrapper.eq("objectcode",objectCode);
		List<Scmobject> list = scmobjectService.list(scmobjectQueryWrapper);
		if(list!=null && list.size()==1){
			Scmobject scmobject = list.get(0);
			QueryWrapper<Scmcustomformat> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("objectid",scmobject.getId());
			List<Scmcustomformat> scmcustomformats = scmcustomformatMapper.selectList(queryWrapper);
			if(scmcustomformats!=null && scmcustomformats.size()>0){
				Scmcustomformat scmcustomformat = scmcustomformats.get(0);
				String id = scmcustomformat.getId();
				QueryWrapper<Scmcustomformatitem> scmcustomformatitemQueryWrapper=new QueryWrapper<>();
				scmcustomformatitemQueryWrapper.eq("parentid",id);
				scmcustomformatitemQueryWrapper.orderByAsc("colorder");
				List<Scmcustomformatitem> scmcustomformatitems = scmcustomformatitemMapper.selectList(scmcustomformatitemQueryWrapper);
				for (Scmcustomformatitem scmcustomformatitem : scmcustomformatitems) {
					ColumnVo columnVo=new ColumnVo();
					columnVo.setColid(scmcustomformatitem.getColid());
					columnVo.setColname(scmcustomformatitem.getColname());
					columnVo.setColorder(scmcustomformatitem.getColorder().toString());
					columnVo.setColvisable(scmcustomformatitem.getColisvisable());
					columnVo.setColwidth(columnVo.getColwidth());
					columnVo.setFormtype(scmcustomformatitem.getColtype());
					columnVo.setColdict(scmcustomformatitem.getColdictcode());
					columnVo.setOnlyread(scmcustomformatitem.getOnlyread());
					res.add(columnVo);
				}
			}
		}
		return res;
	}

	@Override
	@Transactional
	public void importSysFormat(String objectid) {
		if(ifExistsCustomSysFormat(objectid)){
			throw new RuntimeException("当前业务对象已存在系统格式，不允许继续导入");
		}
		Scmobject scmobject = scmobjectService.getById(objectid);
		if(scmobject!=null){
			String objecttable = scmobject.getObjecttable();
			QueryWrapper<onlcgformhead> queryWrapper=new QueryWrapper<>();
			queryWrapper.eq("table_name",objecttable);
			List<onlcgformhead> onlcgformheads = onlcgformheadMapper.selectheadlist(objecttable);
			if(onlcgformheads!=null && onlcgformheads.size()>0){

				Scmcustomformat scmcustomformat=new Scmcustomformat();
				scmcustomformat.setIssys("1");
				scmcustomformat.setObjectid(objectid);
				scmcustomformat.setFormatcode(scmobject.getObjectcode());
				scmcustomformat.setFormatname(scmobject.getObjectname());
				scmcustomformatMapper.insert(scmcustomformat);

//				QueryWrapper<onlcgformfield> onlcgformfieldQueryWrapper=new QueryWrapper<>();
//				onlcgformfieldQueryWrapper.eq("cgform_head_id",onlcgformheads.get(0).getId());
				List<onlcgformfield> onlcgformfields = onlcgformfieldMapper.selectfieldlist(onlcgformheads.get(0).getId());
				if(onlcgformfields!=null && onlcgformfields.size()>0){
					for (onlcgformfield onlcgformfield : onlcgformfields) {
						if(onlcgformfield.getDbfieldname().equalsIgnoreCase("create_by")
							|| onlcgformfield.getDbfieldname().equalsIgnoreCase("update_by")
							|| onlcgformfield.getDbfieldname().equalsIgnoreCase("parentid")
						||onlcgformfield.getDbfieldname().equalsIgnoreCase("sys_org_code")
								||onlcgformfield.getDbfieldname().equalsIgnoreCase("create_time")
								||onlcgformfield.getDbfieldname().equalsIgnoreCase("update_time")
								||onlcgformfield.getDbfieldname().equalsIgnoreCase("id")
						)
							continue;
						Scmcustomformatitem scmcustomformatitem=new Scmcustomformatitem();
						scmcustomformatitem.setParentid(scmcustomformat.getId());
						scmcustomformatitem.setColid(onlcgformfield.getDbfieldname());
						scmcustomformatitem.setColname(onlcgformfield.getDbfieldtxt());
						scmcustomformatitem.setColorder(Integer.parseInt(onlcgformfield.getOrdernum()));
						scmcustomformatitem.setColwidth(onlcgformfield.getFieldlength());
						scmcustomformatitem.setColisvisable(onlcgformfield.getIsshowlist());
						scmcustomformatitem.setColtype(onlcgformfield.getFieldshowtype());
						scmcustomformatitemMapper.insert(scmcustomformatitem);
					}

				}
			}
		}
	}

	private boolean ifExistsCustomSysFormat(String objectid){
		QueryWrapper<Scmcustomformat> queryWrapper=new QueryWrapper<>();
		queryWrapper.eq("objectid",objectid);
		queryWrapper.eq("issys","1");
		List<Scmcustomformat> scmcustomformats = scmcustomformatMapper.selectList(queryWrapper);
		if(scmcustomformats!=null && scmcustomformats.size()>0){
			return true;
		}else{
			return false;
		}
	}

}
