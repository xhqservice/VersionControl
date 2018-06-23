package com.jadlwork.business.qrgl.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jadlsoft.business.BaseManager;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlwork.business.qrgl.IQRCodeManager;
import com.jadlwork.model.qrgl.ShortURLSsxmBean;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: QRCodeManager
 * @描述: TODO
 * @作者: lcx
 * @时间: 2018-1-30 上午11:44:01
 */
public class QRCodeManager extends BaseManager implements IQRCodeManager {

	@Override
	public String getQRCodeUrlBySsxm(String ssxm) {
		String shortUrl = "";
		
		/**
		 * 1、从库中获取
		 */
		if (StringUtils.isEmpty(shortUrl)) {
			Map map = getQRCodeMapperBySsxm(ssxm);
			if (map != null && map.size()>0) {
				shortUrl = (String) map.get("shorturl");
			}
		}
		
		/**
		 * 3、当前还没有，重新生成并且返回
		 */
		if (StringUtils.isEmpty(shortUrl)) {
			shortUrl = getRandomShortUrlSuffix(ssxm);
		}
		
		shortUrl = SystemConstants.shortUrl_perfix + shortUrl;
		return shortUrl;
	}
	
	@Override
	public Map getQRCodeMapperBySsxm(String ssxm) {
		Map resMap = null;
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("ssxm", ssxm);
		List list = daoUtils.find(ShortURLSsxmBean.class, condition);
		if (list != null && list.size()>0) {
			resMap = (Map) list.get(0);
		}
		return resMap;
	}
	
	@Override
	public Map getQRCodeMapperByShortUrl(String shortUrl) {
		Map resMap = null;
		Map condition = new HashMap();
		condition.put("zt", SystemConstants.ZT_TRUE);
		condition.put("shorturl", shortUrl);
		List list = daoUtils.find(ShortURLSsxmBean.class, condition);
		if (list != null && list.size()>0) {
			resMap = (Map) list.get(0);
		}
		return resMap;
	}
	
	@Override
	public int saveQRCodeMapper(ShortURLSsxmBean bean) {
		String id;
		synchronized (bean) {
			id = String.valueOf(daoUtils.getNextval("Q_SHORTURL_SSXM"));
		}
		bean.setId(id);
		bean.setZt(SystemConstants.ZT_TRUE);
		return daoUtils.save(bean);
	}
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 工具 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	
	/**
	 * 生成可用的短连接	--4位，全大写
	 * @return
	 */
	private String getRandomShortUrlSuffix(String ssxm) {
		
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] chars = str.toCharArray();
		String sti = "";
		int runNum = 0;
		while(true) {
			/**
			 * 1、生成4位随机数
			 */
			for(int i=0;i<4;i++) {
				sti = sti + chars[(int)(Math.random()*26)];
			}
			
			/**
			 * 2、查看库里面是否已存在
			 */
			Map map = getQRCodeMapperByShortUrl(sti);
			if (map == null || map.size()==0) {
				/**
				 * 3、不存在就保存返回，跳出循环
				 */
				ShortURLSsxmBean bean = new ShortURLSsxmBean();
				bean.setShorturl(sti);
				bean.setSrcurl(SystemConstants.srcUrl_perfix.replace("{@ssxm}", ssxm));
				bean.setSsxm(ssxm);
				saveQRCodeMapper(bean);
				break;
			}else {
				/**
				 * 4、已存在就重新生成
				 */
				if (runNum>50) {
					throw new RuntimeException("生成二维码跟库里面冲突超过50次，请稍后再试！");
				}
				runNum++;
			}
		}
		return sti;
	}
	
}
