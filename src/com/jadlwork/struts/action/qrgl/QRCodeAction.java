package com.jadlwork.struts.action.qrgl;

import java.io.File;

import org.apache.log4j.Logger;

import com.jadlsoft.struts.action.BaseAction;
import com.jadlsoft.utils.JsonUtil;
import com.jadlsoft.utils.JsonUtils;
import com.jadlsoft.utils.ResponseUtils;
import com.jadlsoft.utils.StringUtils;
import com.jadlsoft.utils.SysConfigUtils;
import com.jadlsoft.utils.SystemConstants;
import com.jadlwork.business.qrgl.IQRCodeManager;
import com.jadlwork.model.ResultBean;
import com.jadlwork.servlet.ShortUrlServlet;
import com.jadlwork.utils.FileUtils;
import com.jadlwork.utils.ewm.QRCodeUtils;

/**
 * Copyright © 2018京安丹灵. All rights reserved.
 * @类名: QRCodeAction
 * @描述: 二维码Action
 * @作者: lcx
 * @时间: 2018-1-30 上午10:38:48
 */
public class QRCodeAction extends BaseAction {

	private IQRCodeManager qrCodeManager;
	
	private Logger logger = Logger.getLogger(QRCodeAction.class);
	
	public String createQRCode() {
		return "querynew";
	}
	
	/**
	 * ajax创建二维码，填充二维码
	 */
	public void fillQRCode() {
		ResultBean resultBean = new ResultBean(SystemConstants.STATUSCODE_OK, "");
		/**
		 * 1、获取待生成的内容
		 */
		String sclx = request.getParameter("sclx");
		String content = request.getParameter("content");
		String txt = "";	//生成二维码的内容
		if (SystemConstants.QRCODE_SCLX_APPSTORE.equals(sclx)) {
			if (StringUtils.isEmpty(content)) {
				txt = "";
			}else {
				txt = qrCodeManager.getQRCodeUrlBySsxm(content);
				String s = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf(request.getRequestURI()));
				txt = s + txt;
			}
		}else {
			txt = content == null ? "" : content;
		}
		
		/**
		 * 2、调用工具生成二维码
		 */
		//获取临时文件保存地址
		
		String backupDir = SysConfigUtils.getProperty("war.dir.backup","appbackup");
		//文件存储在VersionControlCenter/appbackup/qrcodeimgs/ssxm/file
		String fromDirStr = File.separator + backupDir + File.separator + "qrcodeimgs" + File.separator + content;
		String savePath = request.getSession().getServletContext().getRealPath(fromDirStr);
		
		String imgFilePath = savePath + File.separator + "qrcode.png";
		try {
			QRCodeUtils.generateQRImage(txt, imgFilePath, "PNG");
		} catch (Exception e) {
			try {
				ResponseUtils.renderResultBean(response, resultBean, SystemConstants.STATUSCODE_FALSE, "生成二维码出错！");
				return;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		
		/**
		 * 3、将文件地址返回，供页面查看
		 */
		resultBean.setArg1(fromDirStr+File.separator+"qrcode.png");
		resultBean.setArg2(txt);
		try {
			ResponseUtils.render(response, JsonUtil.bean2json(resultBean));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存图片到本地
	 */
	public void writeImgTofile() {
		String ssxm = request.getParameter("ssxm");
		if (StringUtils.isEmpty(ssxm)) {
			throw new RuntimeException("请求错误！");
		}
		
		/**
		 * 1、查找对应的二维码图片
		 */
		String backupDir = SysConfigUtils.getProperty("war.dir.backup","appbackup");
		//文件存储在VersionControlCenter/appbackup/qrcodeimgs/ssxm/file
		String fromDirStr = File.separator + backupDir + File.separator + "qrcodeimgs" + File.separator + ssxm;
		String savePath = request.getSession().getServletContext().getRealPath(fromDirStr);
		
		String filePath = savePath + File.separator + "qrcode.png";
		File file = new File(filePath);
		if (!file.isFile()) {
			throw new RuntimeException("二维码不存在！");
		}
		
		/**
		 * 2、下载
		 */
		try {
			FileUtils.downloadFile(request, response, file, "");
		} catch (Exception e) {
			logger.info("下载异常或者用户取消下载！");
		}
		
	}
	
	public void setQrCodeManager(IQRCodeManager qrCodeManager) {
		this.qrCodeManager = qrCodeManager;
	}
}
