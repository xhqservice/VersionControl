package com.jadlwork.struts.action.qrgl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadlsoft.dbutils.DicMapUtils;
import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlwork.business.qrgl.IDmglManager;
import com.jadlwork.business.qrgl.impl.DmglManager;

/**
 * 字典管理action
 * @author 罗平 下午01:58:48
 */
public class DmglAction extends BaseAction{
	/*
	 * 注入
	 */
	private IDmglManager dmglManager;

	
	
	public IDmglManager getDmglManager() {
		return dmglManager;
	}

	public void setDmglManager(IDmglManager dmglManager) {
		this.dmglManager = dmglManager;
	}

	/**
	 * 跳转到字典管理页面
	 */
	public String toDmglMain(){
		String tablename = SysConfigUtils.getProperty("dm_tablename","所属平台");  //字典表名称列表
		String table = SysConfigUtils.getProperty("dm_table","t_dm_ssxm");  //字典表列表
		String[] tablenameArr = tablename.split(",");
		String[] tableArr = table.split(",");
		if(tablenameArr.length != tableArr.length){
			throw new RuntimeException("字典表名称列表与字典表列表个数不符！");
		}
		List tableList = tranArrToMapList(tableArr,tablenameArr);
		
		request.setAttribute("tableList", tableList);
		return "listpage";
	}
	
	/**
	 * 字典查询列表
	 */
	public String edit(){
		String table = request.getParameter("table");
		String tablename = request.getParameter("tablename");
		List contentList = dmglManager.getDmContent(table);
		List commentsList = dmglManager.getDmComments(table.toUpperCase());
		
		request.setAttribute("commentsList", commentsList);
		request.setAttribute("contentList", contentList);
		request.setAttribute("table", table);
		request.setAttribute("tablename", tablename);
		request.setAttribute("total", new Integer(contentList.size()));	//总条数
		request.setAttribute("pagesize", new Integer(contentList.size()));//每页条数
		return OK;
	}

	/**
	 * 将字符串装换为list
	 * @param tableArr
	 * @param tablenameArr
	 * @return
	 */
	private List tranArrToMapList(String[] tableArr, String[] tablenameArr){
		if(tableArr.length == 0){
			return new ArrayList();
		}
		List list = new ArrayList();
		for(int i=0;i<tableArr.length;i++){
			Map map = new HashMap();
			map.put("table", tableArr[i]);
			map.put("tablename", tablenameArr[i]);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 删除字典记录
	 */
	public String remove(){
		String table = request.getParameter("table");
		String pkname = request.getParameter("pkname");
		String columnname = request.getParameter("columnname");
		String columnvalue = request.getParameter("columnvalue");
		String columntype = request.getParameter("columntype");
		synchronized (DmglManager.class) {
			dmglManager.deleteDic(table,pkname,columnname,columnvalue,columntype);
			DicMapUtils.getInstance().reCacheOneTable(table);
		}
		
		List contentList = dmglManager.getDmContent(table);
		List commentsList = dmglManager.getDmComments(table.toUpperCase());
		
		request.setAttribute("commentsList", commentsList);
		request.setAttribute("contentList", contentList);
		request.setAttribute("table", table);
		request.setAttribute("total", new Integer(contentList.size()));	//总条数
		request.setAttribute("pagesize", new Integer(contentList.size()));//每页条数
		return "edit";
	}
	
	
	/**
	 * 字典记录添加和修改
	 */
	public String update(){

		String type = request.getParameter("type");
		String table = request.getParameter("table");
		String columnname = request.getParameter("columnname");
		String columnvalue = request.getParameter("columnvalue");
		String pkcolumn = request.getParameter("pkcolumn");
		String columntype = request.getParameter("columntype");
		if("insert".equals(type)){
			List list = dmglManager.getDic(table,pkcolumn,columnname,columnvalue,columntype);
			if(list != null && list.size() != 0){
				request.setAttribute("pkexist", "pkexist");
			}else{
				dmglManager.updateDic(type,table,columnname,columnvalue,pkcolumn,columntype);
			}
		}else{
			dmglManager.updateDic(type,table,columnname,columnvalue,pkcolumn,columntype);
		}
		DicMapUtils.getInstance().reCacheOneTable(table);
		
		List contentList = dmglManager.getDmContent(table);
		List commentsList = dmglManager.getDmComments(table.toUpperCase());
		
		request.setAttribute("commentsList", commentsList);
		request.setAttribute("contentList", contentList);
		request.setAttribute("table", table);
		request.setAttribute("total", new Integer(contentList.size()));	//总条数
		request.setAttribute("pagesize", new Integer(contentList.size()));//每页条数
		return "edit";
	}
	
	
	/**
	 * 查询字典记录
	 */
	public String searchDic(){
		String table = request.getParameter("table");
		String tablename = request.getParameter("tablename");
		String columnname = request.getParameter("columnname");
		String searchkey = request.getParameter("searchkey");
		
		List contentList = dmglManager.queryDic(table, searchkey, columnname);  //查询内容列表
		List commentsList = dmglManager.getDmComments(table.toUpperCase());
		
		request.setAttribute("commentsList", commentsList);
		request.setAttribute("contentList", contentList);
		request.setAttribute("table", table);
		request.setAttribute("tablename", tablename);

		request.setAttribute("searchkey", searchkey);
		request.setAttribute("total", new Integer(contentList.size()));	//总条数
		request.setAttribute("pagesize", new Integer(contentList.size()));//每页条数
		return "edit";
	}
	
}
