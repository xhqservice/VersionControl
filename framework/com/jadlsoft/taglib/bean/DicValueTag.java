package com.jadlsoft.taglib.bean;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.bean.WriteTag;

import com.jadlsoft.dbutils.DicMapUtils;

 /**
 * 
 * @author 张方俊 2008-07-07
 *
 */
public class DicValueTag extends WriteTag {
	
	private static final long serialVersionUID = 8891888817151052247L;
	
	
	 //* 实际对应的缓存键
	 
	protected String actualkey = null;
	
	
	// * 指定取哪个字段的值。如T_DM_WPDM取DLDW的值，而不是默认的WPMC的值
	 
	protected String column = null;
	
	public String getActualkey(){
		return (this.actualkey);
	}

	public void setActualkey(String actualkey){
		this.actualkey = actualkey;
	}
	
	public String getColumn(){
		return (this.column);
	}

	public void setColumn(String column){
		this.column = column;
	}

	protected String formatValue(Object valueToFormat) throws JspException {
		Object value = valueToFormat;
		if (value instanceof java.lang.String) {
			// 取哪个值
			//	actualkey优先级高于property
			//	<jadlbean:write name="lzGmzBean" property="gmzzt"/>actualkey不存在以property为准
			//	<jadlbean:write name="lzGmzBean" property="zt" actualkey="gmzzt"/>以actualkey为准 
			String key = (this.actualkey == null || this.actualkey.equals("")) ? this.property : this.actualkey;
			// key = gmzzt
			
			/* * 获取dicconfig.xml中数据以属性name的值作为键, 属性key,table,text和其值组成list的map
			 * 如：DicMapLowcaseColumnRowMapper.getDicMap() = {gmzzt={key=gmzzt, table=t_dm_gmzzt, text=mc}, ...}
			 * 从配置文件dicconfig.xml中获取属性name对应值的记录:
			 * 如：columnDicMap = {key=gmzzt, table=t_dm_gmzzt, text=mc}
			 */
			Map columnDicMap = (Map) DicMapUtils.getDicMapTranslate().get(key);			
			if(columnDicMap == null){
				return (String) value;
			}
			String cacheKey = (String) columnDicMap.get("table"); // table = t_dm_gmzzt
			String keymc = (String) columnDicMap.get("text"); // keymc = mc
			
			// 去哪个名称
			//	column的值优先级高于text的值
			//	select dm, mc, dldw from t_dm_wpdm
			//	<jadlbean:write name="lzGmzBean" property="zt" actualkey="gmzzt" column="dldw"/>与mc相比以dldw为准
			if(this.column != null && !this.column.equals("")){
				keymc = this.column;
			}
			
			
			/* * 获取applicationContext.xml中com.jadlsoft.dbutils.DaoUtils类注入属性tableList的值
			 * 如：allCacheMap = {t_dm_gmzzt={0={gmzzt=0, mc=有效}, 1={gmzzt=1, mc=注销}, 2={gmzzt=2, mc=已用}}...}
			 */
			Map allCacheMap = (Map) DicMapUtils.getInstance().getDicMapCacheForTranslate();
			if(allCacheMap == null){
				return (String) value;
			}
			Map cacheMap = (Map) allCacheMap.get(cacheKey); // cacheMap = {0={gmzzt=0, mc=有效}, 1={gmzzt=1, mc=注销}, 2={gmzzt=2, mc=已用}}
			if(cacheMap == null){
				return (String) value;
			}
			
			// 处理物品代码翻译不出来情况
			if (cacheKey.equalsIgnoreCase("t_dm_wpdm")) {
				if (value.equals("00") && keymc.equals("dldw")) {
					return "发";
				} else if (value.equals("00") && keymc.equals("wpmc")) {
					return "零散品";
				}
				String wpdm = ((String)value).substring(0, 1);
				String regEx = "[a-zA-Z89]"; // 必须有[]
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(wpdm);
				if (m.matches()) { // 注意此处不要用：m.find()
					value = wpdm + "00";
				}
			}
			
			// value = "0"
			Map values = (Map) cacheMap.get(value); // values = {gmzzt=0, mc=有效}
			if(values == null){
				return (String) value;
			}
			
			return (String) values.get(keymc);	// 有效
        }
		return super.formatValue(value);
	}
}
