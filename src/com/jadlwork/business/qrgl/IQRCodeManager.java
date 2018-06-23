package com.jadlwork.business.qrgl;

import java.util.Map;

import com.jadlwork.model.qrgl.ShortURLSsxmBean;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: IQRCodeManager
 * @描述: 二维码相关manager接口
 * @作者: lcx
 * @时间: 2018-1-30 上午11:43:33
 */
public interface IQRCodeManager {

	/**
	 * 根据所属项目获取待生成二维码的url
	 * @param ssxm 所属项目
	 * @return
	 */
	String getQRCodeUrlBySsxm(String ssxm);
	
	/**
	 * 根据所属项目获取二维码url短连接和所属项目的映射
	 * @param ssxm
	 * @return
	 */
	Map getQRCodeMapperBySsxm(String ssxm);
	
	/**
	 * 根据短连接获取二维码url短连接和所属项目的映射
	 * @param shortUrl
	 * @return
	 */
	Map getQRCodeMapperByShortUrl(String shortUrl);
	
	/**
	 * 保存映射bean
	 * @param bean
	 * @return
	 */
	int saveQRCodeMapper(ShortURLSsxmBean bean);
	
}
