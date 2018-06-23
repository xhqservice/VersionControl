package com.jadlsoft.struts.action;


import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.jadlsoft.business.CommonListManager;
import com.jadlsoft.dbutils.DicMapUtils;
import com.jadlsoft.dbutils.SQLHelper;
import com.jadlsoft.model.xtgl.UserSessionBean;
import com.jadlsoft.utils.IConstants;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlsoft.utils.XzhqhUtils;

public class CommonListAction extends BaseAction {
	private static Logger log = Logger.getLogger(CommonListAction.class);
	private static Map forwardMap = new HashMap();
	/*
	 * Spring中注入的变量
	 */
	protected CommonListManager listManager;
	private JdbcTemplate jdbcTemplate;
	private String [] listConfigList = null;//要加载的列表配置文件
	private int listcount;//每次允许导出的最大记录数
	
	private int pagesize;//每页数量
	private int pageno;//页数
	//protected boolean resume; //是否中途返回
	protected Map params = null; //中途返回时的参数
	protected List condList = new ArrayList();

	public void setListManager(CommonListManager listService) {
		this.listManager = listService;
	}
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public void setListConfigList(String[] listConfigList) {
		this.listConfigList = listConfigList;
	}
	public void setListcount(int listcount) {
		if (listcount > 25000) {
			listcount = 20000;
		}
		if (listcount < 1000) {
			listcount = 10000;
		}
		this.listcount = listcount;
	}

	/*
	 * 加载列表配置文件
	 */
	public void init(){
		for(int i=0;i<listConfigList.length;i++){
			getListConfig(listConfigList[i]);
		}
	}
	public static Map getForwardMap(){
		return forwardMap;
	}
	/** 
	 * Method execute
	 */
	public String execute() {
		Map forwardinfo = (Map) forwardMap.get(request.getAttribute("realservletpath"));
		if(forwardinfo == null){
			return SystemConstants.FAILURE_KEY;
		}
		condList.clear();
		setPageInfo();
		
		String queryparamter = getQueryparamter(forwardinfo);
		String queryparamtername = request.getParameter("queryparamtername");
		String zdycs = request.getParameter("zdycs");
		if(zdycs!=null && !"".equals(zdycs)){
			request.setAttribute("zdycs", zdycs);
		}
		if(queryparamtername!=null && !"".equals(queryparamtername)){
			request.setAttribute("queryparamtername", queryparamtername);
		}else{
			request.setAttribute("queryparamtername", "");
		}
		if (!setConditions(queryparamter)) {
			return SystemConstants.FAILURE_KEY;
		}
		
		request.setAttribute("queryparamter", queryparamter);
		request.setAttribute("realaction", request.getAttribute("realservletpath"));
		//修改跳转后的提示信息
		String sysmessage_key = (String) request.getSession().getAttribute(SystemConstants.SYSMESSAGE_KEY);
		if (StringUtils.isEmpty(sysmessage_key)) {
			request.setAttribute(SystemConstants.SYSMESSAGE_KEY, sysmessage_key);
			request.getSession().removeAttribute(SystemConstants.SYSMESSAGE_KEY);
		}
		
		//判断是否保存列表操作
		if (request.getParameter("saveaction") != null && request.getParameter("saveaction").equals("savelist")) {
			return doSaveList();
		} else {
			Map data = getForwardContent(request);
			request.setAttribute("detailParam",data); 
			if (data != null) {
				Iterator keys = data.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next()+"";
					if(key.equals("title")||key.equals("field")||key.equals("hiddenfield")||key.equals("columnwidth"))
						request.setAttribute(key, ((String) data.get(key)).split(","));
					else
						request.setAttribute(key, data.containsKey(key)?(String)data.get(key):"");
				}
			}
		return doGetList();
		}
	}
	
	/**
	 * 设置排序参数queryparamter
	 * @param forwardinfo
	 */
	private String getQueryparamter(Map forwardinfo) {
		String queryparamter = request.getParameter("queryparamter");
		if (StringUtils.isEmpty(queryparamter)) {
			queryparamter = "&&" + forwardinfo.get("defaultcondition");
		}
		return queryparamter + "~" + forwardinfo.get("requirecondition")
				+ isOrderby(forwardinfo, queryparamter);
	}
	
	/*
	 * 初始化分页信息
	 */
	private void setPageInfo() {
		// 此处设置每页条数，默认为10
		pagesize = StringUtils.convertIntDef(request.getParameter("pageSize"), 10);
		if (pagesize <= 0) {
			pagesize = 10;
		}
		// 此处设置显示的页码，默认为1
		pageno = StringUtils.convertIntDef(request.getParameter("pageNo"), 1);
		if (pageno <= 0) {
			pageno = 1;
		}
		String choice = request.getParameter("choice");
		if (choice == null || choice.length() == 0) {
			choice = "first";
		}
		if (choice.equals("prev")) {
			pageno--;
		} else if (choice.equals("next")) {
			pageno++;
		} else if (choice.equals("first")) {
			pageno = 1;
		} else if (choice.equals("last")) {
			pageno = 0; // 后面计算页码
		}
	}
	
	/**
	 * 实际执行的获取列表操作
	 * 2006-9-8
	 */
	protected String doGetList() {
		String realServletPath = (String) request.getAttribute("realservletpath");
		if(forwardMap.containsKey(realServletPath)) {
			Map forwardinfo = (Map) forwardMap.get(realServletPath);
			Map data = new HashMap();
			try {
				//System.out.println(condList.toString()+"++++++++++++++"); 
				data = listManager.getDataCountAndList(null, condList,super.getUserSession(), 
						(String) forwardinfo.get("datasource"), 
						(String)forwardinfo.get("class"), pagesize, pageno);
			} catch(Exception e) {
				log.error("获取信息列表失败！", e); 
			}
			
			request.setAttribute("total", data.get("total"));
			request.setAttribute("list", data.get("list"));
			request.setAttribute("pagesize", new Integer(pagesize));
			
			/*
			 * shiling 跳转页面存在userParameter参数时,传递给跳转后页面,
			 * 可以用于在查看页中嵌入列表页时控制列表页的按钮显示与否
			 */
			if(!StringUtils.isEmpty(request.getParameter("userParameter"))){
				request.setAttribute("userParameter", request.getParameter("userParameter"));
			}

			if(request.getParameter("saveaction")!=null && request.getParameter("saveaction").equals("commonlist")) {
				request.setAttribute("saveaction", request.getParameter("saveaction"));
				return SystemConstants.COMMOMLIST_KEY;
			}
			
			if(forwardinfo.get("success")!=null&&!"".equals(forwardinfo.get("success"))){
				RequestDispatcher dispatch = request.getRequestDispatcher((String)forwardinfo.get("success"));
				try {
					dispatch.forward(request, response);
					return null;
				} catch (Exception e) {
					log.error("ListAction get forward error!", e);
					return SUCCESS;
				}
			}else{
				request.setAttribute("saveaction", "commonlist");
				return SystemConstants.COMMOMLIST_KEY;
			} 
			 
		}
		return null;
	} 
	 /**
	 * 将查询条件解析成Map或List，保存在类成员conditions/condlist中
	 * @param condition 条件字符串
	 * 条件参数使用queryparamter来获得，需要在前台javascript中放在一个隐藏域中，
	 * 格式为：条件~值~条件~值，保证配对，用~隔开
	 * 或者 &&条件~操作~值~条件~操作~值，保证3个一组
	 */
	private boolean setConditions(String condition) {
		//BaseUserSession userBean = (BaseUserSession)getUserSession();
		UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(UserUtils.USER_SESSION);
		//conditions.clear();
		//condList.clear();
		if (condition == null || condition.length() == 0)
			return true;
		if (condition.startsWith("&&")) {
			//三个一组的条件
			String[] conds = condition.substring(2).split("~");
			if (conds.length % 3 > 0) {
				return false;
			}
			for (int i = 0; i < conds.length / 3; i++) {
				/*
				 * 张方俊 2010-08-24 用于未登录系统时的调用
				 */
				if(user == null || user.getXzqh() == null){
					condList.add(conds[3 * i] + "~" + conds[3 * i + 1] + "~" + conds[3 * i + 2]);
				}else{
					condList.add(conds[3 * i] + "~" + conds[3 * i + 1] + "~" + getReplaceConditions(conds[3 * i + 2],user));
				}
			}
		} else {
			//两个一组的条件
			String[] conds = condition.split("~");
			if (conds.length % 2 > 0) {
				return false;
			}
			for (int i = 0; i < conds.length / 2; i++) {
				//conditions.put(conds[2*i], conds[2*i+1]);
				/*
				 * 张方俊 2010-08-24 用于未登录系统时的调用
				 */
				if(user == null || user.getXzqh() == null){
					condList.add(conds[2 * i] + "~=~" + conds[2 * i + 1]);
				}else{
					condList.add(conds[2 * i] + "~=~" + getReplaceConditions(conds[2 * i + 1],user));
				}
			}
		}
		return true;
	}
	
	/**
	 * 将配置文件调入内存
	 */
	private static void getListConfig(String fileName) {
        try {
            URL url = CommonListAction.class.getResource(fileName);
            File file = new File(url.getFile().replaceAll("%20", " "));
    		if(file.exists() && file.isFile() && file.canRead()) {
    			SAXReader reader = new SAXReader();
    			try {
    				Document doc = reader.read(file);
    				Element root = doc.getRootElement();
    				Iterator item = root.elementIterator();
    				while(item.hasNext()) {
    					Element column = (Element) item.next();
    					String request = column.attributeValue("request");
    					Map datamap = null;
    					if(forwardMap.containsKey(request)) {
    						datamap = (Map) forwardMap.get(request);
    					}else {
    						datamap = new HashMap();
    					}
    					
    					// 迭代取所有属性，存入内存
    					Iterator columnIterator = column.attributeIterator();
    					while (columnIterator.hasNext()) {
    						Attribute attr = (Attribute) columnIterator.next();
    						datamap.put(attr.getName(), attr.getValue());
						}
    					
    					forwardMap.put(request, datamap);
    				}
    			} catch (DocumentException e) {
    				log.error("读取List映射关系错误！", e);
    			}
			}
        } catch (Throwable t) {
        	log.error("读取List映射关系错误！", t);
        } 
	}
	
	/**
	 * 从配置文件中获取保列表的配置信息
	 * @param request
	 * @return
	 */
	private Map getForwardContent(HttpServletRequest request) {
		String reqURI = (String) request.getAttribute("realservletpath");
		return (Map) forwardMap.get(reqURI);
	}

	/**
	 * 判断是否添加默认排序
	 * @param forwardinfo 参数集合
	 * @param queryparamterTemp  查询条件
	 * @return 排序条件
	 */
	private String isOrderby(Map forwardinfo, String queryparamterTemp) {
		if(StringUtils.isEmpty(queryparamterTemp)|| queryparamterTemp.indexOf("db_resultorderby") == -1) {
			if(!StringUtils.isEmpty((String)forwardinfo.get("orderby")) ) {
				return "~db_resultorderby~=~" + (String)forwardinfo.get("orderby");
			} 
		}
		return "";
	}
	
	/**
	 * 执行列表保存功能
	 * 对于列表保存分三种情况处理:
	 * 1、记录数小于等于5000条:以原来的直接输出到页面的方式导出
	 * 2、记录数小于等于listcount条:生成xml格式的xls文件并以流方式输出到客户端
	 * 3、记录数大于listcount条:跳转到中间页供用户选择,实现分批导出,每次导出listcount条
	 * 2011-5-9
	 */
	protected String doSaveList() {
		pageno = 1;
		pagesize = listcount;
		String realServletPath = (String) request.getAttribute("realservletpath");
		// 命名保存的excel文件名称
		String excelName = realServletPath.substring(realServletPath.lastIndexOf("/") + 1);
		if (excelName.indexOf(".") > 0) {
			excelName = excelName.substring(0, excelName.indexOf("."));
		}
		String choice = request.getParameter("choice");
		if (!"savelist".equals(choice)) {
			/*
			 * 第一次点列表保存时
			 */
			Map forwardinfo = (Map) forwardMap.get(realServletPath);
			List realConditions = getRealConditions(condList);
			int count = listManager.getCount(realConditions, (String) forwardinfo.get("datasource"),super.getUserSession(), (String) forwardinfo.get

("class"));
			
			if (count <= 5000) {
				/*
				 * 记录数小于等于5000条:以原来的直接输出到页面的方式导出
				 */
				Map data = getForwardContent(request);
				if (data != null) {
					request.setAttribute("description", (String) data.get("description"));
					if(!StringUtils.isEmpty((String)data.get("jsurl"))){
						request.setAttribute("jsurl", (String)data.get("jsurl"));
					}
					if(!StringUtils.isEmpty((String)data.get("hiddenfield"))){
						request.setAttribute("hiddenfield", ((String)data.get("hiddenfield")).split(","));
					}
					if(!StringUtils.isEmpty((String)data.get("columnwidth"))){
						request.setAttribute("columnwidth", ((String)data.get("columnwidth")).split(","));
					}
					request.setAttribute("titlelist", ((String) data.get("title")).split(","));
					request.setAttribute("fieldlist", ((String) data.get("field")).split(","));
				}
				
				try {
					
					data = listManager.getDataCountAndList(null, condList,super.getUserSession(),  (String) forwardinfo.get("datasource"), 

(String) forwardinfo.get("class"), pagesize, pageno);
				} catch(Exception e) { 
					log.error("获取信息列表失败！", e);  
				}
				request.setAttribute("total", new Integer(count));
				request.setAttribute("list", data.get("list"));
				request.setAttribute("pagesize", new Integer(pagesize));
				return SystemConstants.SAVELIST_KEY;
			} else if (count < listcount) { 
				/*
				 * 记录数小于等于listcount条:生成xml格式的xls文件并以流方式输出到客户端
				 */
				Document document = createExcelData(request, 0);
				outputDocument(response, document,excelName);
				return null;
			} else {
				/*
				 * 记录数大于listcount条:跳转到中间页供用户选择,实现分批导出,每次导出listcount条
				 */
				request.setAttribute("total", new Integer(count));
				request.setAttribute("pagesize", new Integer(pagesize));
				return SystemConstants.CONTINUE_KEY;
			}
		} else {
			/*
			 * 通过中间页选择导出指定页码的记录
			 */
			Document document = createExcelData(request, Integer.parseInt(request.getParameter("pageNo")));
			outputDocument(response, document,excelName);
			return null;
		}
	}
	
	/**
	 * 生成大数据量的导出文件
	 * @param request 从中获取必要的内容
	 * @param pageno 当前要导出的页码,从0开始计算
	 * @return Document
	 */
	synchronized private Document createExcelData(HttpServletRequest request, int pageno){
		Document document = DocumentHelper.createDocument();
		document.addProcessingInstruction("mso-application", "progid='Excel.Sheet'");
		Element rootElement = document.addElement("Workbook", "urn:schemas-microsoft-com:office:spreadsheet");
		rootElement.addNamespace("o", "urn:schemas-microsoft-com:office:office");
		rootElement.addNamespace("x", "urn:schemas-microsoft-com:office:excel");
		rootElement.addNamespace("ss", "urn:schemas-microsoft-com:office:spreadsheet");
		rootElement.addNamespace("html", "http://www.w3.org/TR/REC-html40");

		Element element = rootElement.addElement("Worksheet");
		element.addAttribute("ss:Name", "Sheet1");
		final Element tableElement = element.addElement("Table");
		
		Map datamap = getForwardContent(request);;
		String[] titleArray = ((String)datamap.get("title")).split(",");
		final String[] fieldArray = ((String)datamap.get("field")).split(",");
		
		Element rowElement = tableElement.addElement("Row");
		
		try {
			/*
			 * 输出表头
			 */
			for (int k = 0; k < titleArray.length; k++) {
				Element cellElement = rowElement.addElement("Cell");
				Element dataElement = cellElement.addElement("Data");
				dataElement.addAttribute("ss:Type", "String");
				dataElement.addText(titleArray[k]);
				dataElement = null;
				cellElement = null;
			}
			List realConditions = getRealConditions(condList);
			/*
			 * 拼接SQL语句,设置参数数组
			 */
			List paramList = new ArrayList();
			StringBuffer buff = new StringBuffer();
			String orderby = "";
			try {
				Iterator item = realConditions.iterator();
				while (item.hasNext()) {
					Map condition = (Map) item.next();
					String column = (String)condition.get("column");
					if (column.equals(SystemConstants.DB_ORDERBY)) {
						orderby = " order by " + condition.get("value");
						continue;
					}
					buff.append("and ").append(condition.get("column")).append(" ").append(condition.get("operation")).append(" ").append("? ");
					paramList.add(condition.get("value"));
				}
			} catch (Exception e) {
			}
			/*
			 * 生成分页的SQL语句
			 */
			String sql = "select * from " + (String)datamap.get("datasource") + " where 1 = 1 " + buff.toString() + " " + orderby;
			sql = SQLHelper.getListSqlFromSql(sql, pagesize, pageno);
			paramList.add(new Integer(pagesize * (pageno + 1)));
			paramList.add(new Integer(pagesize * pageno));

			Object[] params = new Object[paramList.size()] ;
			for (int k = 0; k < paramList.size(); k++) {
				params[k] = paramList.get(k);
			}
			/*
			 * 生成导出内容
			 */
			final DicMapUtils dmu = new DicMapUtils();
			jdbcTemplate.query(sql, params, new ResultSetExtractor() {
				public Object extractData(ResultSet rs) {
					try {
						int i = 1;
						while (rs.next()) {
							Map data = (Map) dmu.mapRow(rs, i++);
							Element rowElement = tableElement.addElement("Row");
							for (int k = 0; k < fieldArray.length; k++) {
								Element cellElement = rowElement.addElement("Cell");
								Element dataElement = cellElement.addElement("Data");
								dataElement.addAttribute("ss:Type", "String");
								Object obj = data.get(fieldArray[k]);
								if (obj == null) {
									dataElement.addText("");
								} else {
									dataElement.addText((String) obj);
								}
								dataElement = null;
								cellElement = null;
							}
							rowElement = null;
						}
					} catch (Exception e) {
						e.printStackTrace();
						e.getMessage();
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.getMessage();
		}
		return document;
	}

	/**
	 * 输出列表导出的xml文件内容到客户端
	 */
	private void outputDocument(HttpServletResponse response, Document document, String excelName) {
		try {
			response.reset();
			// response.setContentType("text/xml");
			response.setContentType("application/octet-stream");
			String header = "attachment;filename=" + excelName + ".xls";
			response.setHeader("Content-Disposition", header);
			ServletOutputStream output = response.getOutputStream();
			XMLWriter writer = new XMLWriter(output);
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理传入条件
	 * @param conditions:conditions里面包含的是string，所以要转换成含map的
	 */
	private List getRealConditions(List conditions){
		List realConditions = new ArrayList();
		for (int i = 0; i < condList.size(); i++) {
			String[] conds = ((String)condList.get(i)).split("~");
			realConditions.add(SQLHelper.createCondition(conds[0], conds[1], conds[2]));
		}
		return realConditions;
	}
	
	/**
	 * @功能：条件值变量替换
	 * @参数：
	 * @param condition 需替换条件值变量的条件
	 * @param user 登录用户信息
	 * @return
	 * @返回值：String 替换完$xzqh、$shortxzqh、$gaglkbh、$dwkbh、$shortdwkbh后的条件值
	 * create by zhaohuibin 2013-5-23 上午09:35:12
	 */
	private String getReplaceConditions(String condition, UserSessionBean user){
		try{
			String xzqh = user.getXzqh();
			if(IConstants.YHLX_QY.equals(user.getYhlx())){
				xzqh = user.getUserId();
			}
			condition = condition.replaceAll("\\Q[$xzqh]\\E", xzqh);
			
			String shortxzqh = XzhqhUtils.getXZHQH(user.getXzqh());
			if(IConstants.YHLX_QY.equals(user.getYhlx())){
				shortxzqh = XzhqhUtils.getShortYHBM(xzqh);
			}
			condition = condition.replaceAll("\\Q[$shortxzqh]\\E", shortxzqh);
			
			String kbh = user.getKbh();
			condition = condition.replaceAll("\\Q[$gaglkbh]\\E", kbh);
			condition = condition.replaceAll("\\Q[$dwkbh]\\E", kbh);
			
			String shortdwkbh = user.getDwdm();
			condition = condition.replaceAll("\\Q[$shortdwkbh]\\E", shortdwkbh);
			return condition;
		}catch(Exception e){//此处只有部分系统中的user不含dwdm时会有异常的可能，其他属性user都已经包含。
			log.error("替换条件值变量condition.replaceAll(\\Q[$shortdwkbh]\\E)等异常！", e);
			return condition;
		}
	}
}