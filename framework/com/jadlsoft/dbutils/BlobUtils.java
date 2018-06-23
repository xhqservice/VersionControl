package com.jadlsoft.dbutils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.LobRetrievalFailureException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import sun.misc.BASE64Decoder;

import com.jadlsoft.utils.ArrayUtils;
import com.jadlsoft.utils.DomainBeanUtils;
import com.jadlsoft.utils.SystemConstants;


/**
 * <p>Title:BlobUtils.java </p>
 * <p>Description: Blob操作的基础类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: 京安丹灵</p>
 * @date 2006-10-18
 * @author Zong
 * @author Li Banggui Modified at 2008-05-04
 * @version 1.0
*/

public class BlobUtils {
	public static Logger log = Logger.getLogger(BlobUtils.class);
	
	private JdbcOperations jdbcTemplate;
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	
	private NativeJdbcExtractor nje;
	
	
	/**
	 * Description: 保存包含BLOB类型的信息
	 * @param table 要保存到的表
	 * @param bean 包含有BLOB类型数据的bean
	 * @param field 需要修改的字段
	 * @return 1为成功
	 * care:bean需要包含blobFields字段,用来指明该bean中那个字段是BLOB类型的字段
	 */
	public int doInsertBlobTable(String table, Object bean, String field) {
		String[] fields = null;
		if(field==null || field.trim().equals("") || field.trim().equals("*")){
			fields = DomainBeanUtils.getProperties(bean);	//该对象的所有字段
		}else{
			fields = field.split(",");
		}
		/*
		 * 根据所给定的代表数据库表的bean中字段组织sql语句
		 */
		Map properties = DomainBeanUtils.getPropertiesMap(bean);
		String blobFields = properties.get(SystemConstants.DB_BLOBFIELD).toString();
		String[] blobFieldArray = blobFields.split(",");
		StringBuffer sqlBuff1 = new StringBuffer("INSERT INTO " + table + " (");
		
		StringBuffer sqlBuff2 = new StringBuffer(" (");		//值部分,即(?,?,?,......)
		for (int i = 0; i < fields.length; i++) {
			if (!SQLHelper.isSystemIdentity(fields[i]) && null!=properties.get(fields[i])) {
				sqlBuff1.append(fields[i] + ",");		//将字段追加到sql语句中
				if (!ArrayUtils.containValue(blobFieldArray,fields[i])) {		//添加占位符
					sqlBuff2.append(":" + fields[i]).append(",");
				} else {
					sqlBuff2.append("empty_blob(),");
				}
			}
		}
		/*
		 * 组织成完整的sql语句 
		 */
		sqlBuff1.deleteCharAt(sqlBuff1.length() - 1);
		sqlBuff1.append(") VALUES");
		sqlBuff2.deleteCharAt(sqlBuff2.length() - 1);
		sqlBuff1.append(sqlBuff2);
		sqlBuff1.append(")");

		namedParameterJdbcTemplate.update(sqlBuff1.toString(), new BeanPropertySqlParameterSource(bean));	//插入空BLOB
		String pklist = (String) DomainBeanUtils.getPropertyValue(bean, SystemConstants.DB_TABLEPKFIELDS);
		return doUpdateBlobTable(table, bean, pklist.split(","), fields);	//更新该条记录,保存BLOB数据
	}
	
	/**
	 * Description: 更新包含BLOB类型的信息
	 * @param table 要更新到的表
	 * @param bean 包含有BLOB类型数据的bean
	 * @param pk 主键字段数字
	 * @param fields 需要修改的字段
	 * @return 1为成功
	 * care:bean需要包含blobFields字段,用来指明该bean中那个字段是BLOB类型的字段
	 */
	public int doUpdateBlobTable(String table, Object blobbean, String [] pk, String[] fields) {
		final Object bean = blobbean;
		final Map properties = DomainBeanUtils.getPropertiesMap(bean);
		
		/*
		 * 为主键字段数组赋值
		 */
		final Object[] pkValue  = new Object [pk.length];
		for (int i=0;i<pk.length;i++){
			pkValue[i] = properties.get(pk[i]);
		}

		/*
		 * 根据所给定的代表数据库表的bean中字段组织sql语句 
		 */
		String blobFields = properties.get(SystemConstants.DB_BLOBFIELD).toString();
		final String[] blobFieldArray = blobFields.split(",");
		StringBuffer sql = new StringBuffer("UPDATE " + table + " SET ");
		for(int i=0;i<blobFieldArray.length;i++){
			if(ArrayUtils.containValue(fields, blobFieldArray[i])){
				sql.append(blobFieldArray[i] + "=?,");
			}else{
				blobFieldArray[i] = "";
			}
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" WHERE 1 = 1 ");
		for(int i=0;i<pk.length;i++){
			sql.append(" AND ").append(pk[i]).append(" =?");
		}
		
		/*
		 * 设置Blob操作所用的Oracle Lob Handle
		 */
		OracleLobHandler olhandle = new OracleLobHandler();
		olhandle.setNativeJdbcExtractor(nje);

		/*
		 * 更新数据
		 */
		jdbcTemplate.execute(
				sql.toString(),
				new AbstractLobCreatingPreparedStatementCallback(
						olhandle) {
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						int k = 1;
						try {
							for(int i=0;i<blobFieldArray.length;i++){
								if(blobFieldArray[i].length()>0){
									if(properties.get(blobFieldArray[i]) instanceof String ){
										lobCreator.setBlobAsBytes(ps, k++, ((String)properties.get(blobFieldArray[i])).getBytes());
									}else{
										lobCreator.setBlobAsBytes(ps, k++, (byte[]) properties.get(blobFieldArray[i]));
									} 
									
								}
							}
							for(int i=0;i<pkValue.length;i++){
								ps.setObject(k++, pkValue[i]);
							}
						} catch (Exception e) {
							log.error("更新BLOB记录失败!对象:"
									+ bean.getClass().getName(), e);
						}
					}
				});
		return 1;
	}
	
	/**
	 * Description: 通过Stream或byte[]的数据来修改Blob字段
	 * @param sql 执行修改的SQL语句
	 * @param dataMap 数据
	 * @param fields 修改的字段名称
	 * @param lengths 数据长度，主要用于Stream
	 * @return 是否成功，1为成功
	 * May 19, 2008
	 */
	public int doUpdateBlob(String sql, final Map dataMap, final String [] fields, final int[] lengths) {
		OracleLobHandler olhandle = new OracleLobHandler();
		olhandle.setNativeJdbcExtractor(nje);

		/*
		 * 更新数据
		 */
		jdbcTemplate.execute(
				sql,
				new AbstractLobCreatingPreparedStatementCallback(
						olhandle) {
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						int k = 1;
						try {
							for (int i = 0; i < fields.length; i++) {
								Object data = dataMap.get(fields[i]);
								if (data instanceof InputStream) {
									lobCreator.setBlobAsBinaryStream(ps, k++, (InputStream) data, lengths[i]);
								}else{
									if(data instanceof byte[]) {
										lobCreator.setBlobAsBytes(ps, k++, (byte[]) data);
									}else{
										ps.setObject(k++, data);
									}
									
								}
							}
						} catch (Exception e) {
							log.error("更新BLOB记录失败!" + ps, e);
						}
					}
				});
		return 1;
	}
	
	/**
	 * Description: 获取包含BLOB类型的信息
	 * @param sql 获取Blob对象的sql语句
	 * @param bean 包含有BLOB类型数据的bean
	 * @param pkValue 主键字段值
	 * return 包含Blob类型数据的对象
	 */
	public Object doGetBlobTable(String sql, final Object bean, String [] pkValue) {
		Map properties = DomainBeanUtils.getPropertiesMap(bean);
		final String [] fields = DomainBeanUtils.getProperties(bean);	//该bean的所有字段
		final String blobFields = properties.get(SystemConstants.DB_BLOBFIELD).toString();		//记录该bean代表的数据库表中所包含的BLOB字段
		final OracleLobHandler lobHandler = new OracleLobHandler();
		
		try {
			jdbcTemplate.query(sql, pkValue,
					new AbstractLobStreamingResultSetExtractor() {
						protected void handleNoRowFound()
								throws LobRetrievalFailureException {
							throw new IncorrectResultSizeDataAccessException(
									"获取BLOB字段信息错误!对象:" + bean.getClass().getName(),
									1, 0);
						}

						public void streamData(ResultSet rs) throws SQLException,
								IOException {
							int k = 1;
							for (int i = 0; i < fields.length; i++) {
								if (!fields[i].equals(SystemConstants.DB_BLOBFIELD) && !SQLHelper.isSystemIdentity(fields[i])) {
									if (blobFields.indexOf(fields[i]) == -1) {
										DomainBeanUtils.setProperty(bean,
												fields[i], rs.getObject(k++));
									} else {
										DomainBeanUtils.setProperty(bean,
												fields[i],
												lobHandler.getBlobAsBytes(rs,
														k++));
									}
								}
							}
						}
					});
		} catch (DataAccessException e) {
			log.error(e);
		}
		return bean;
	}
	
	/**
	 * Description: 获取包含BLOB类型的信息列表
	 * @param sql 获取Blob对象的sql语句,该语句中所查询的字段必须是Blob类型
	 * @param blobFields BLOB类型字段列表
	 * @param pkValue 主键字段值
	 * return 包含Blob类型数据的List
	 */
	public List doGetBlobList(String sql, final String [] blobFields, String [] pkValue) {
		final List list = new ArrayList();		
		final OracleLobHandler lobHandler = new OracleLobHandler();
		
		jdbcTemplate.query(sql, pkValue, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map map = new HashMap();
				for (int i = 0; i < blobFields.length; i++) {
					map.put(blobFields[i], lobHandler.getBlobAsBytes(rs, i + 1));
				}
				list.add(map);
				return map;
			}
		});
		return list;
	}
	
	/**
	 * Description: 获取包含BLOB类型的信息列表
	 * @param sql 获取Blob对象的sql语句
	 * @param blobFields BLOB类型字段列表
	 * return 包含Blob类型数据的List
	 */
	public List doGetListWithBlob(String sql, final String blobFields) {
		final List list = new ArrayList();
		final OracleLobHandler lobHandler = new OracleLobHandler();
		String sqlSplit = sql.toLowerCase();
		final String[] fieldArray = sqlSplit.substring(sqlSplit.indexOf("select") + 6,
				sqlSplit.indexOf("from")).trim().split(",");
		jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				do {
					Map map = new HashMap();
					for (int i = 0; i < fieldArray.length; i++) {
						if (blobFields.indexOf(fieldArray[i]) >= 0) {
							map.put(fieldArray[i],
									lobHandler.getBlobAsBytes(rs, i + 1));
						} else {
							map.put(fieldArray[i], rs.getObject(i + 1));
						}
					}
					list.add(map);
				} while (rs.next());
				return null;
			}
		});
		return list;
	}
	
	/**
	 * Description: 获取BLOB字段的byte[]数组
	 * @param sql 获取Blob对象的sql语句
	 * return 包含Blob类型数据的Map
	 */
	public Map doGetByteBlob(String sql) {
		final Map map = new HashMap();
		final OracleLobHandler lobHandler = new OracleLobHandler();
		String sqlSplit = sql.toLowerCase();
		final String[] fieldArray = sqlSplit.substring(sqlSplit.indexOf("select") + 6,
				sqlSplit.indexOf("from")).trim().split(",");
		jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				for (int i = 0; i < fieldArray.length; i++) {
					map.put(fieldArray[i], lobHandler.getBlobAsBytes(rs, i + 1));
				}
				return null;
			}
		});
		return map;
	}
	
	/**
	 * Description: 获取BLOB字段的byte[]值
	 * @param sql  sql语句
	 * @param condition 条件
	 * @return 包含Blob数据的Map
	 * May 19, 2008
	 */
	public Map doGetByteBlob(String sql, Map condition) {
		final Map map = new HashMap();
		final OracleLobHandler lobHandler = new OracleLobHandler();
		String sqlSplit = sql.toLowerCase();
		final String[] fieldArray = sqlSplit.substring(sqlSplit.indexOf("select") + 6,
				sqlSplit.indexOf("from")).trim().split(",");
		namedParameterJdbcTemplate.query(sql, condition, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				for (int i = 0; i < fieldArray.length; i++) {
					map.put(fieldArray[i], lobHandler.getBlobAsBytes(rs, i + 1));
				}
				return null;
			}
		});
		return map;
	}
		
	public void setJdbcTemplate(JdbcOperations jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void setNje(NativeJdbcExtractor nje) {
		this.nje = nje;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcOperations namedParameterJdbcTelplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTelplate;
	}
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 方法说明：
	 * 创建日期：2013-3-25下午02:01:28
	 * 作者：wzf
	 * 修改日期：
	 */
		public int saveBlob(final Object bean) {
		int updateRows = 0;
		StringBuffer ins_sql = new StringBuffer();
		StringBuffer value_sql = new StringBuffer();
		final StringBuffer columnNames = new StringBuffer();
		final Map data = new HashMap();//保存在组织Insert语句时
		
		String table_name = SQLHelper.getExcuteTableName(bean);
		Field[] fields = bean.getClass().getDeclaredFields();
		ins_sql.append("insert into " + table_name + "(");
		value_sql.append("(");

		for (int i = 0; i < fields.length; i++) {
			String columnName = fields[i].getName();
			if (!columnName.toLowerCase().equals("serialversionuid")
					&& !SQLHelper.isSystemIdentity(columnName)) {
				Object value = DomainBeanUtils.getPropertyValue(bean,
						columnName);
				if ((columnName
						.equals(SystemConstants.DB_TIMESTAMPFIELD))
						|| (value != null && !value.toString().equals(""))) {
		 			ins_sql.append(columnName + ",");
					value_sql.append("?,");

					data.put(columnName, value);
					columnNames.append(columnName).append(",");
				}
			}
		}
		if (ins_sql.charAt(ins_sql.length() - 1) == ',') {
			ins_sql.replace(ins_sql.length() - 1, ins_sql.length(), ")");
			value_sql.replace(value_sql.length() - 1, value_sql.length(),
					")");
		}
		ins_sql.append(" values " + value_sql.toString());

		final String blobField = getObjectBlobFields(bean);
		OracleLobHandler olhandle = new OracleLobHandler();
		olhandle.setNativeJdbcExtractor(nje);
		Object obj = jdbcTemplate.execute(ins_sql.toString(),
				new AbstractLobCreatingPreparedStatementCallback(olhandle) {
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException,
							DataAccessException {
						String [] columnNameArray = columnNames.toString().split(",");
						try {
							for (int i=0;i<columnNameArray.length;i++) {
			        			if (blobField.indexOf(columnNameArray[i]) == -1) {
									if(data.get(columnNameArray[i]) instanceof String){
				        				ps.setString(i + 1, (String)data.get(columnNameArray[i]));
				        			}else if(data.get(columnNameArray[i]) instanceof Integer){
				        				ps.setInt(i + 1, ((Integer)data.get(columnNameArray[i])).intValue());
				        			}else if(data.get(columnNameArray[i]) instanceof BigDecimal){
				        				ps.setBigDecimal(i + 1, ((BigDecimal)data.get(columnNameArray[i])));
				        			}else if(data.get(columnNameArray[i]) instanceof Date){
				        				ps.setTimestamp(i + 1, new Timestamp(((Date)data.get(columnNameArray[i])).getTime()));
				        			}
								} else {
									if(data.get(columnNameArray[i]) instanceof String){
				        				lobCreator.setBlobAsBytes(ps, i + 1,
												new BASE64Decoder().decodeBuffer((String)(data.get(columnNameArray[i]))));
				        			}else if(data.get(columnNameArray[i]) instanceof byte []){
				        				lobCreator.setBlobAsBytes(ps, i + 1,
												(byte [])(data.get(columnNameArray[i])));
				        			}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		updateRows = ((Integer)obj).intValue();
		return updateRows;
	}
		public static String getObjectBlobFields(Object cls_pojo) {
			Object blobfields = DomainBeanUtils.getPropertyValue(cls_pojo,
					SystemConstants.DB_BLOBFIELD);
			if (blobfields != null && blobfields.getClass() == String.class) {
				return (String) blobfields;
			} else {
				return null;
			}
		}
}
