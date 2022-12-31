package org.br.erp.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.br.erp.base.entity.Material;
import org.br.erp.base.entity.Materialpo;
import org.br.erp.base.entity.Scmcustomfields;
import org.br.erp.base.service.IMaterialService;
import org.br.erp.base.service.IScmcustomfieldsService;
import org.br.erp.base.service.IScmobjectService;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ERPUtils {

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private  IScmcustomfieldsService scmcustomfieldsService;

    public static ERPUtils erpUtils;

    @PostConstruct
    public void init(){
        erpUtils=this;
        erpUtils.materialService=this.materialService;
        erpUtils.scmcustomfieldsService=this.scmcustomfieldsService;
    }

    /*获取时间
     * */
    public static Timestamp getTimestamp(String prDate, String pattern) {
        Timestamp timestamp = null;
        try {
            if(pattern.equalsIgnoreCase("")){
                pattern="yyyy-MM-dd";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = simpleDateFormat.parse(prDate);
            timestamp = new Timestamp(date.getTime());
        } catch (ParseException e) {
            System.out.println("日期格式转化异常");
        }
        return timestamp;
    }


    public static String getNowDate(){
        return getDateString(new Date(),"");
    }
    public static String getDateString(Date date, String format) {
        if(format.equalsIgnoreCase("")){
            format="yyyy-MM-dd";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /*
     * 根据物料、需求时间、数量来获取订货日期
     *
     * */
    public static Date getPrOrderDate(String materialid, String prqty, Date preqdate) {
        Material material = erpUtils.materialService.getById(materialid);

        List<Materialpo> materialpoByMaterialid = erpUtils.materialService.getMaterialpoByMaterialid(materialid);


        //固定采购周期
        Integer fixedPoDate = 0;
        //日供量
        BigDecimal fixedGl = BigDecimal.ZERO;
        if(materialpoByMaterialid!=null && materialpoByMaterialid.size()>0){
            fixedPoDate=materialpoByMaterialid.get(0).getFixedPoDate();
            fixedGl=materialpoByMaterialid.get(0).getFixedGl();
        }

        //根据需求日期来计算订货日期
        BigDecimal qty = new BigDecimal(prqty);

        if(fixedGl!=null && fixedGl.compareTo(BigDecimal.ZERO)>0 && fixedPoDate!=null){
            double time=	 (double)(Math.ceil(qty.divide(fixedGl, 10, RoundingMode.HALF_UP).doubleValue()))+fixedPoDate;
            Calendar cal = Calendar.getInstance();
            cal.setTime(preqdate);
            cal.add(Calendar.DAY_OF_MONTH,(int)-time);
            return cal.getTime();
        }else{
            return preqdate;
        }
    }

    /*
     *
     * 获取当前登录用户信息
     * */
    public static LoginUser getLoginUser(){
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return  user;
    }


    public static boolean isNoneOrEmpty(Object objectValue){
        if(objectValue==null){
            return true;
        }else{
            return StringUtils.isEmpty(objectValue.toString());
        }
    }


    public static Map mapToClass(Map<Object, Object> map,Class<?> clazz,String objectCode){
        if(map == null){
            return null;
        }
        Map res=new HashMap();
        Map cusRes=new HashMap();

        Map<Object,Object> mapCopy=new HashMap<>();
        mapCopy.putAll(map);

        Object obj = null;

        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();//获取到所有属性，不包括继承的属性
            Field[] supFields = obj.getClass().getSuperclass().getDeclaredFields();//获取传入类的父类的所有属性
            for(Field field : fields){
                int mod = field.getModifiers();//获取字段的修饰符
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }
                field.setAccessible(true);
                for (Object objectKey : map.keySet()) {
                    if(objectKey.toString().toLowerCase(Locale.ROOT).equalsIgnoreCase(field.getName().toLowerCase(Locale.ROOT))){
                        if(field.getType().getSimpleName().equalsIgnoreCase("Date")) {
                            String objectKeyDateValue = (String)map.get(objectKey);
                            if(objectKeyDateValue == null){
                                break;
                            }
                            field.set(obj,ERPUtils.getTimestamp(objectKeyDateValue,"yyyy-MM-dd"));
                            mapCopy.remove(objectKey);
                            break;
                        }else if(field.getType().getSimpleName().equalsIgnoreCase("String")){

//                            if(map.get(objectKey).getClass().getSimpleName().equalsIgnoreCase("Boolean")){
//                                if(map.get(objectKey).equals(true)){
//                                    field.set(obj,"1");
//                                }else if(map.get(objectKey).equals(false)){
//                                    field.set(obj,"0");
//                                }
//                            }else {
//
//                            }
                            field.set(obj, String.valueOf(map.get(objectKey)));
                            mapCopy.remove(objectKey);
                                break;
                        } else if(field.getType().getSimpleName().equalsIgnoreCase("BigDecimal")){
                            BigDecimal bigDecimal = new BigDecimal(String.valueOf(map.get(objectKey)));
                            field.set(obj,bigDecimal);
                            mapCopy.remove(objectKey);
                            break;
                        } else if(field.getType().getSimpleName().equalsIgnoreCase("Boolean")){
                            field.set(obj,map.get(objectKey));
                            mapCopy.remove(objectKey);
                            break;
                        }
                    }
                }
               // field.set(obj, map.get(field.getName()));//根据属性名称去map获取value
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        res.put("ret",obj);
        if(map!=null && map.keySet().size()>0){
            List<Scmcustomfields> customfieldsByobjectcode = erpUtils.scmcustomfieldsService.getCustomfieldsByobjectcode(objectCode);
            if(customfieldsByobjectcode!=null && customfieldsByobjectcode.size()>0) {
                //说明还有自定义字段存在
                for (Object cusKey : map.keySet()) {
                    for (Scmcustomfields scmcustomfields : customfieldsByobjectcode) {
                        if (scmcustomfields.getCustomcode().toLowerCase(Locale.ROOT).equalsIgnoreCase(cusKey.toString().toLowerCase(Locale.ROOT))) {
                            cusRes.put(cusKey, map.get(cusKey));
                        }
                    }
                }
            }
        }
        if(cusRes!=null && cusRes.keySet().size()>0){
            res.put("cusret",cusRes);
        }
        return res;
    }

    public static boolean ifHtppReqParamContainKey(Map paramMap,String key){
        return paramMap.containsKey(key);
    }

    public static  String getHttpReqParam(Map paramMap,String key){
       if(ifHtppReqParamContainKey(paramMap,key)){
          String[] keys= (String[])paramMap.get(key);
          return keys[0];
       }else{
           return "";
       }
    }


    /**
     * 判断两个数之间的大小
     * @param one 第一个数
     * @param two 第二个数
     * @return 如果大，返回true，如果小，返回false
     */
    public static boolean bigthenothers(String one,String two){
        BigDecimal first=new BigDecimal(one);
        BigDecimal second=new BigDecimal(two);
        if(first.compareTo(second) >=0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean stringequals(String one ,String two){
        return new BigDecimal(one).compareTo(new BigDecimal(two)) == 0;
    }


    public static String convertDoubleToString(double v){
        Double doublevalue=new Double(v);
        return String.valueOf(doublevalue.intValue());
    }

    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @param format
     * @return
     */
    public static String getFirstDay(int year,int month,String format) {
        Calendar cale = Calendar.getInstance();

        cale.set(Calendar.YEAR, year);    //赋值年份
        cale.set(Calendar.MONTH, month - 1);//赋值月份
        int lastDay = cale.getActualMinimum(Calendar.DAY_OF_MONTH);//获取月最大天数
        cale.set(Calendar.DAY_OF_MONTH, lastDay);//设置日历中月份的最大天数
        if (format.equalsIgnoreCase("")) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);//格式化日期yyyy-MM-dd
        String lastDayOfMonth = sdf.format(cale.getTime());
        return lastDayOfMonth;
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year,int month)
    {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return  sdf.format(cal.getTime());
    }


    public static boolean twoStringEqural(String a,String b){
        BigDecimal bigDecimala = new BigDecimal(a);
        BigDecimal bigDecimalb = new BigDecimal(b);
        return bigDecimala.compareTo(bigDecimalb) >=0;
    }

   public static BigDecimal getBigDecimal(String a){
        return new BigDecimal(a);
   }


   public static String getSqlInStringFromIds(String ids){
        String res="";
       String[] split = ids.split(",");
       for(int i=0;i<split.length;i++){
           if(split[i].equalsIgnoreCase(",")){
               continue;
           }else{
               res+="'"+split[i]+"',";
           }
       }
       String substring = res.substring(0, res.lastIndexOf(","));
       return substring;
   }

}
