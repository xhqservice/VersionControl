/**
 * <p>Title:SearchAction.java</p>
 * <p>Description:弹出页公用的Action</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: 京安丹灵<p>
 * @date Nov 4, 2009
 * @author zhouxl
 * @version 3.0
 */

package com.jadlsoft.struts.action.search;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jadlsoft.business.search.SearchManager;
import com.jadlsoft.model.search.SearchBean;
import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.InterfaceUtils;
import com.jadlsoft.utils.StringUtils;

public class SearchAction extends BaseAction {
	private static Logger log = Logger.getLogger(SearchAction.class);
	protected static Map searchMap = new HashMap();
	private SearchBean searchBean;
	
	protected int pagesize;	  //每页数量
	protected int pageno;	  //页数
	private static int linecount = 10;
	//格式为：条件~值~条件~值，保证配对，用~隔开
	protected Map conditions = new HashMap();
	Map params;
	
	static {
		loadConfig();
	}

	private SearchManager searchManager;
	private String zxs_code;
	
	public SearchBean getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(SearchBean searchBean) {
		this.searchBean = searchBean;
	}

	public void setSearchManager(SearchManager searchManager) {
		this.searchManager = searchManager;
	}
	
	public void setZxs_code(String zxs_code) {
		this.zxs_code = zxs_code;
	}


	/** 
	 * Method execute
	 */
	public String execute() throws Exception {
		// 此处设置每页条数，默认为20
		pagesize = StringUtils.convertIntDef((String)request.getParameter("pageSize"),
				linecount);
		if (pagesize <= 0)
			pagesize = linecount;
		pageno = StringUtils.convertIntDef((String)request.getParameter("pageNo"), 1);
		if (pageno <= 0)
			pageno = 1;
		String choice = (String)request.getParameter("choice");
		if (choice == null || choice.length() == 0)
			choice = "first";
		if (choice.equals("prev")) {
			pageno--;
		} else if (choice.equals("next")) {
			pageno++;
		} else if (choice.equals("first")) {
			pageno = 1;
		} else if (choice.equals("last")) {
			pageno = 0; // 后面计算页码
		}
//		if (request.getParameter("tablename") != null
//				&& !request.getParameter("tablename").toString().equals(""))
//			return doGetListExt(mapping, form, request, response);
		return doGetList();
	}	
	
	/**
	 * Description: 实际执行的获取列表操作
	 */
	public String doGetList() {
		//根据表单数据构造查询语句    
		String type = (String)request.getParameter("forward");
		String notin = (String)request.getParameter("notin");//从JSP页面获取特殊情况下的过滤条件
		if(null==notin||"undefined".equals(notin))
			notin="";
		String conds = StringUtils.fillNull(request.getParameter("baseconditions"));
		String innerconds = StringUtils.fillNull(request.getParameter("innerconditions"));
		if (!StringUtils.isEmpty((String) request.getParameter("baseconditions"))
				&& !StringUtils.isEmpty(((String) request.getParameter("queryparamter")))) {
			conds += "~";
		} 
		conds += StringUtils.fillNull(request.getParameter("queryparamter"));
//		if(((String)request.getParameter("baseconditions")).length()>0 && ((String)request.getParameter("queryparamter")).length()>0) {
//			conds = (String)request.getParameter("baseconditions")+ "~" + (String)request.getParameter("queryparamter");
//		} else {
//			conds = (String)request.getParameter("baseconditions")+ (String)request.getParameter("queryparamter");
//		}
		if(searchMap.containsKey(type)){
			Map itemmap = (Map) searchMap.get(type);
			
			String sql = createSQL((String)itemmap.get("table"), conds,innerconds, (String)itemmap.get("resultcol"));
			if (sql == null || sql.equals("")) {
				log.info("查询参数错误导致不能构造sql");
				return ERROR;
			}
			if(sql.indexOf("WHERE")>0) {
				sql = sql + " AND ";
			} else {
				sql = sql + " WHERE ";
			}
            //add by huangbotao date:2012/04/28 discription:添加非常特殊情况下的过滤条件
			sql=sql+(("".equals(notin))?"1=1":notin);
			if(searchMap.containsKey(type+".filter")) {
			
				sql = sql +" AND "+ (itemmap.containsKey("filter")?itemmap.get("filter"):"1=1");
			}
	
			String orderby = (String) itemmap.get("orderby");
			if(orderby!=null && orderby.length()>0){
				sql += " ORDER BY " + orderby;
			}

			
//			String ext = sform.getString("ext");
//			if(ext != null&&!ext.equals("")){
//				//处理特殊查询
//				doExt(mapping,form,request,response,ext);
//			}
			int count = 0;
			List result = null;
			try {
				if(itemmap.containsKey("webservice")) {
					String url = InterfaceUtils.getUrlForSearch((String) itemmap.get("webservice"));
					int countWebservice = searchManager.getSearchCountByWebservice(sql,url);
					if(countWebservice == -1){
						log.info("调用webservice出错！url：" + url);
						return ERROR;
					}
					count = countWebservice;
				}else{
					count = searchManager.getSearchCount(sql);
				}
				//需要检查页码，0表示最后一页
				if(pageno<=0 || pageno * pagesize >= count) {
					pageno = (int)((count+pagesize-1)/pagesize);
				}
				if(itemmap.containsKey("forward")){
					pageno = 1;
					pagesize = count;
				}
				if(itemmap.containsKey("webservice")) {
					String url = InterfaceUtils.getUrlForSearch((String) itemmap.get("webservice"));
					List listWebservice = searchManager.getSearchListByWebservice(sql, (pageno-1)*pagesize, pagesize, url);
					if(listWebservice == null){
						log.info("调用webservice出错！url：" + url);
						return ERROR;
					}
					result = listWebservice;
				}else{
					result= searchManager.getSearchList(sql, (pageno-1)*pagesize, pagesize);
				}
			} catch(Exception e) {
				log.error("选择查询错误！", e);
			}
			request.setAttribute("total", new Integer(count));	//总条数
			request.setAttribute("list", result);				//结果列表
			request.setAttribute("pagesize", new Integer(pagesize));//每页条数
			String[] titles = ((String)itemmap.get("title")).split(",");
			request.setAttribute("titles", titles);				//标题
			request.setAttribute("titlelength", new Integer(titles.length));	//标题数量
			String[] resultcols = ((String)itemmap.get("resultcol")).split(",");
			for(int i=0;i<resultcols.length;i++){
				if(resultcols[i].indexOf(" ")>0) {
					resultcols[i] = resultcols[i].substring(resultcols[i].indexOf(" ")+1);
				}
			}
			request.setAttribute("resultcol", resultcols);		//查询结果列
			request.setAttribute("searchcol", itemmap.get("searchcol"));	//搜索列
			request.setAttribute("searchcond", itemmap.get("searchcond"));	//搜索条件操作
			request.setAttribute("searchtitle", ((String)itemmap.get("searchtitle")).split(","));	//搜索标题
			request.setAttribute("queryparamter", (String)request.getParameter("queryparamter"));
			request.setAttribute("forward", (String)request.getParameter("forward"));
			request.setAttribute("multiselect", (String)request.getParameter("multiselect"));
			request.setAttribute("zxs_code", zxs_code);
//			return mapping.findForward(itemmap.containsKey("forward") ? 
//					(String)itemmap.get("forward"): SystemConstants.SUCCESS_KEY);
			String multiselect =null;
			if(request.getParameter("multiselect")!=null&&request.getParameter("multiselect").toString().equals("1")){
				multiselect = "successMulti";
			}else if(request.getParameter("multiselect")!=null&&request.getParameter("multiselect").toString().equals("2")){
				multiselect = "successMultitwo";
			}else if(request.getParameter("multiselect")!=null&&request.getParameter("multiselect").toString().equals("3")){
				multiselect = "successMethod";
			}else{
				multiselect = "success";
			}
			String returnStr = itemmap.containsKey("forward") ? (String)itemmap.get("forward"):null;
			
			if(returnStr != null ){
				return returnStr;
			}else{
				returnStr = multiselect;
			}
			return returnStr;
		}
		
		return SUCCESS;
	}
	
	/**
	 * @doExt 获取要查询的弹出页数据(处理特殊弹出页)
	 * @param ext
	 */
//	public String doGetListExt() {
//		// 根据表单数据构造查询语句
//		String sql = createSQL(sform.getString("tablename"), sform
//				.getString("conditions"), sform.getString("resultcol"));
//		if (sql == null || sql.equals("")) {
//			log.info("查询参数错误导致不能构造sql");
//			return mapping.findForward(SystemConstants.FAILURE_KEY);
//		}
//		sql += " " + sform.getString("orderby");
//
//		String ext = sform.getString("ext");
//		if (ext != null && !ext.equals("")) {
//			// 处理特殊查询
//			doExt(mapping, form, request, response, ext);
//		}
//		int count = 0;
//		List result = null;
//		try {
//			count = searchManager.getSearchCount(sql);
//			// 需要检查页码，0表示最后一页
//			if (pageno <= 0 || pageno * pagesize >= count) {
//				pageno = (int) ((count + pagesize - 1) / pagesize);
//			}
//			result = searchManager.getSearchList(sql, (pageno - 1) * pagesize,
//					pagesize);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		request.setAttribute("total", new Integer(count));
//		request.setAttribute("list", result);
//		request.setAttribute("pagesize", new Integer(pagesize));
//
//		return mapping.findForward(sform.getString("forward"));
//	}
//	
//	/**
//	 * @doExt 处理特殊查询
//	 */
//	public void doExt(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response,String ext){
//		String[] item = ext.split("~");
//		if(item[0].equalsIgnoreCase("patchgqdwbm")){ //批量查询枪支时根据单位代码查单位下的配枪部门消息列表显示在页面下拉筐中
//			String sql = "SELECT pqdwbm_id,pqdwbm_name FROM t_dw_pqdwbm WHERE substr(pqdwbmid,1,9) = '"+item[1]+"' AND zt = '0'";
//			//log.info("[ext]"+sql);
//			List eitem = searchManager.getSearchList(sql);
//			request.setAttribute("bm", eitem);
//		}
//	}
	

	/**
	 * Description: 将配置文件调入内存
	 */
	private static void loadConfig() {
		URL url = SearchAction.class.getResource("search.xml");
		String configFile = url.getFile().replaceAll("%20", " ");
		File file = new File(configFile);
		if (file.exists() && file.isFile() && file.canRead()) {
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				Iterator item = root.elementIterator();
				while (item.hasNext()) {
					Element searchitem = (Element) item.next();
					String name = searchitem.getName();
					if (name.equals("linecount")) {
						linecount = StringUtils.convertIntDef(searchitem
								.attributeValue("value"), 20);
					} else {
						Map itemmap = new HashMap();
						Iterator prop = searchitem.elementIterator();
						while (prop.hasNext()) {
							Element propitem = (Element) prop.next();
							itemmap.put(propitem.getName(), propitem
									.getTextTrim());
						}
						searchMap.put(name, itemmap);
					}
				}
				log.info("成功读取选择查询配置文件！");
			} catch (DocumentException e) {
				log.error("读取Search配置错误！", e);
			}
		}
	}	

	/**
	 * @createSQL 构造SQL语句
	 * @param tName 表或视图名称
	 * @param conditions 条件
	 * @param resultcol 结果列
	 * @param innerconditions sql内部条件
	 * @return sql语句
	 */
	private String createSQL(String tName, String conditions,String innerconditions, String resultcol) {
		if (tName == null || tName.equals("")) {
			return null;
		}
		if(!innerconditions.equals("")){
			String[] innercondstr = innerconditions.split("~");
			for (int i = 0; i < innercondstr.length; i++) {
				String[] wh = innercondstr[i].split("@");
				if (wh.length == 2) {
					tName = tName.replaceAll(":"+wh[0], wh[1]);
				}		
			}
		}
		String[] tname = tName.split(",");
		if (tname.length > 0 && resultcol.equalsIgnoreCase("*")) {
			resultcol = tname[0] + ".*";
			for (int i = 1; i < tname.length; i++) {
				resultcol += "," + tname[i] + ".*";
			}
		}

		String sql = "";
		if (resultcol == null || resultcol.equals("")) {
			resultcol = "*";
		}
		if (conditions == null || conditions.equals("")) {
			return "SELECT " + resultcol + " FROM " + tName;
		}
		String[] condstr = conditions.split("~");
		for (int i = 0; i < condstr.length; i++) {
			sql += " and " + createWhere(condstr[i]);
		}
		return "SELECT " + resultcol + " FROM " + tName + " WHERE 1=1" + sql;
	}


	/**
	 * @createWhere 构造where条件
	 * @param where where条件
	 * @return where语句
	 */
	private String createWhere(String where) {
		String[] wh = where.split("@");
		if (wh.length != 3) {
			return "1=1";
		}
		String colname = wh[0];
		String oper = wh[1];
		String colvalue = wh[2];
		if (oper.equalsIgnoreCase("equals") || oper.equalsIgnoreCase("equal")) { // 等于
			return colname + " = '" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("is") || colvalue == null
				|| colvalue.equals("null")) {
			return colname + " is null";
		} else if (oper.equalsIgnoreCase("equalsInt")
				|| oper.equalsIgnoreCase("equalInt")) { // 等于一个数字
			return colname + " = " + colvalue;
		} else if (oper.equalsIgnoreCase("notequals")
				|| oper.equalsIgnoreCase("notequal")) { // 不等于
			return colname + " != '" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("notequalsInt")
				|| oper.equalsIgnoreCase("notequalInt")) { // 不等于一个数字
			return colname + " != " + colvalue;
		} else if (oper.equalsIgnoreCase("more")) { // 大于
			return colname + " > '" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("moreInt")) { // 大于一个数字
			return colname + " > " + colvalue;
		} else if (oper.equalsIgnoreCase("less")) { // 小于
			return colname + " < '" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("lessInt")) { // 小于一个数字
			return colname + " < " + colvalue;
		} else if (oper.equalsIgnoreCase("moreequal")
				|| oper.equalsIgnoreCase("moreequals")) { // 大于等于
			return colname + " >= '" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("moreequalInt")
				|| oper.equalsIgnoreCase("moreequalsInt")) { // 大于等于一个数字
			return colname + " >= " + colvalue;
		} else if (oper.equalsIgnoreCase("lessequal")
				|| oper.equalsIgnoreCase("lessequals")) { // 小于等于
			return colname + " <= '" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("lessequalInt")
				|| oper.equalsIgnoreCase("lessequalsInt")) { // 小于等于一个数字
			return colname + " <= " + colvalue;
		} else if (oper.equalsIgnoreCase("like")) {
			return colname + " like '%" + colvalue + "%'";
		} else if (oper.equalsIgnoreCase("likeLeft")) {
			return colname + " like '" + colvalue + "%'";
		} else if (oper.equalsIgnoreCase("likeRight")) {
			return colname + " like '%" + colvalue + "'";
		} else if (oper.equalsIgnoreCase("levellikeLeft")) {
			return createLevelCondition(colname, colvalue + "%", true);
		} else if (oper.equalsIgnoreCase("levelequals")
				|| oper.equalsIgnoreCase("levelequal")) { // 等于
			return createLevelCondition(colname, colvalue, true);
		} else if (oper.equalsIgnoreCase("in")) {
			String[] colvalues = colvalue.split(",");
			if (colvalues.length < 1) {
				return "1=1";
			} else {
				String returnValue = colname + " in ('";
				for (int i = 0; i < colvalues.length; i++) {
					returnValue += colvalues[i] + "','";
					if (i == colvalues.length - 1) {
						returnValue += colvalues[i];
					}
				}
				return returnValue + "')";
			}
		} else {  //特殊情况下直接输出操作符
			return colname + oper + colvalue;
		}
	}

	private String createLevelCondition(String colname, String colvalue,
			boolean isLongNo) {
		if (isLongNo) {
			if (colvalue.indexOf("%") >= 0) {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE " + colname
						+ " LIKE xzqh || '%' AND levelno LIKE '" + colvalue
						+ "')";
			} else {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE " + colname
						+ " LIKE xzqh || '%' AND levelno = '" + colvalue + "')";
			}
		} else {
			if (colvalue.indexOf("%") >= 0) {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE xzqh=" + colname
						+ " AND levelno LIKE '" + colvalue + "')";
			} else {
				return "EXISTS(SELECT * FROM t_wh_qgdwdm WHERE xzqh=" + colname
						+ " AND levelno = '" + colvalue + "')";
			}
		}
	}
}
