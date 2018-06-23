/**
 * <p>Title:ImageUtils.java </p>
 * <p>Description: 照片处理类</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: 京安丹灵</p>
 * @date 2010-08-12
 * @author ZongShuai
 * @version 3.0
*/

package com.jadlsoft.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;

public class ImageUtils {
	
	/**
	 * 剪切图像
	 * @param inputStream 上传图像流
	 * @param ryzpBean 截图参数bean
	 * @return 图片的字节数组
	 * @throws IOException
	 */
	

	public static String cutImage(InputStream imgInputStream, int w, int h, int x1, int y1, int sw,
			int sh) throws IOException {
		Image img;
		ImageFilter cropFilter;
		// 读取源图像
		BufferedImage bi = ImageIO.read(imgInputStream);
		String returnValue = "";
		if (sw >= w && sh >= h) {
			Image image = bi.getScaledInstance(sw, sh, Image.SCALE_DEFAULT);
			// 剪切起始坐标点
			int x = x1;
			int y = y1;
			int destWidth = w; // 切片宽度
			int destHeight = h; // 切片高度
			// 图片比例
			double pw = sw;
			double ph = sh;
			double m = (double) sw / pw;
			double n = (double) sh / ph;
			int wth = (int) (destWidth * m);
			int hth = (int) (destHeight * n);
			int xx = (int) (x * m);
			int yy = (int) (y * n);
			// 四个参数分别为图像起点坐标和宽高
			// 即: CropImageFilter(int x,int y,int width,int height)
			
//			BufferedImage subImage = bi.getSubimage(xx, yy, wth, hth);
			
			cropFilter = new CropImageFilter(xx, yy, wth, hth);
			img = Toolkit.getDefaultToolkit().createImage(
					new FilteredImageSource(image.getSource(), cropFilter));

			BufferedImage tag = new BufferedImage(150, 219,
					BufferedImage.TYPE_INT_RGB);
//			tag.getGraphics().drawImage(img, 0, 0, 150, 219, null); // 绘制缩小后的图

			Graphics2D g = (Graphics2D) tag.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(img, 0, 0, 150, 219, null); // 绘制剪切后的图
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(tag, "JPEG", baos);
			returnValue = (new BASE64Encoder().encode(baos.toByteArray()));

			baos.close();
		}
		return returnValue;
	}

	
}
