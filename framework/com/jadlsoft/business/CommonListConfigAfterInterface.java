package com.jadlsoft.business;

import java.util.List;

import com.jadlsoft.dbutils.DaoUtils;

/**
 * 增加对查询结果的处理，主要是翻译
 * @author 张方俊 2012-09-24 : 上午10:10:33
 *
 */
public interface CommonListConfigAfterInterface {
	public List transResultList(List result , DaoUtils daoUtils);
}
