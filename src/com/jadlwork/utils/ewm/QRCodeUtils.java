package com.jadlwork.utils.ewm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jadlsoft.utils.StringUtils;

/**
 * 生成二维码工具 
 */  
public class QRCodeUtils {  
    private static int width = 300;              //二维码宽度  
    private static int height = 300;             //二维码高度  
    private static int onColor = 0xFF000000;     //前景色  
    private static int offColor = 0xFFFFFFFF;    //背景色  
    private static int margin = 1;               //白边大小，取值范围0~4  
    private static ErrorCorrectionLevel level = ErrorCorrectionLevel.L;  //二维码容错率  
      
    public static void main(String[] args) {
		String txt = "http://www.baidu.com";
		String imgPath = "D:\\工作\\工具\\java生成二维码\\a.jpg";
		generateQRImage(txt, imgPath, null);
	}
    
    /** 
     * 生成二维码 --简单
     * @param txt          二维码内容 
     * @param imgFilePath  二维码保存物理路径 
     * @param format       生成图片的格式	--简易跟文件的后缀保持一致，为空即读取文件后缀名
     */  
    public static void generateQRImage(String txt, String imgFilePath, String format){
    	txt = txt == null ? "" : txt;
        File imgFile = new File(imgFilePath);
        
        File parentDir = imgFile.getParentFile();
        if(!parentDir.exists()){  
        	parentDir.mkdirs();  
        }
        
        if (StringUtils.isEmpty(format)) {
        	if (imgFile.getName().contains(".")) {
        		format = imgFile.getName().substring(imgFile.getName().lastIndexOf(".")+1);
			}
        	if (StringUtils.isEmpty(format)) {
				format = "PNG";
			}
		}
          
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();    
        // 指定纠错等级    
        hints.put(EncodeHintType.ERROR_CORRECTION, level);    
        // 指定编码格式    
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");   
        hints.put(EncodeHintType.MARGIN, margin);   //设置白边  
        try {    
            MatrixToImageConfig config = new MatrixToImageConfig(onColor, offColor);  
            BitMatrix bitMatrix = new MultiFormatWriter().encode(txt,BarcodeFormat.QR_CODE, width, height, hints);    
            MatrixToImageWriter.writeToFile(bitMatrix, format, imgFile, config);
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
    }  
    
    /** 
     * 生成二维码 	--适用于复杂要求的，设置QRCodeParams 的bean对象
     * @param params 
     * QRCodeParams属性：txt、fileName、filePath不得为空； 
     * @throws QRParamsException 
     */  
    public static void generateQRImage(QRCodeParams params){  
        if(params == null  
            || params.getFileName() == null  
            || params.getFilePath() == null  
            || params.getTxt() == null){
        	throw new RuntimeException("生成二维码参数中txt、fileName、filePath不得为空！");
        }  
        try{  
            initData(params);  
              
            String imgPath = params.getFilePath();    
            String imgName = params.getFileName();   
            String txt = params.getTxt();  
              
            if(params.getLogoPath() != null && !"".equals(params.getLogoPath().trim())){  
                generateQRImage(txt, params.getLogoPath(), imgPath, imgName, params.getSuffixName());  
            }else{
            	String imgFilePath = imgPath + File.separator + imgName;
                generateQRImage(txt, imgFilePath, params.getSuffixName());  
            }  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
    }  

    /** 
     * 生成带logo的二维码图片  
     * @param txt          //二维码内容 
     * @param logoPath     //logo绝对物理路径 
     * @param imgPath      //二维码保存绝对物理路径 
     * @param imgName      //二维码文件名称 
     * @param suffix       //图片后缀名 
     * @throws Exception 
     */  
    public static void generateQRImage(String txt, String logoPath, String imgPath, String imgName, String suffix) throws Exception{  
     
        File filePath = new File(imgPath);  
        if(!filePath.exists()){  
            filePath.mkdirs();  
        }  
          
        if(imgPath.endsWith("/")){  
            imgPath += imgName;  
        }else{  
            imgPath += "/"+imgName;  
        }  
          
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();     
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");      
        hints.put(EncodeHintType.ERROR_CORRECTION, level);    
        hints.put(EncodeHintType.MARGIN, margin);  //设置白边  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(txt, BarcodeFormat.QR_CODE, width, height, hints);    
        File qrcodeFile = new File(imgPath);      
        writeToFile(bitMatrix, suffix, qrcodeFile, logoPath);      
    }    
        
    /**  
     *   
     * @param matrix 二维码矩阵相关  
     * @param format 二维码图片格式  
     * @param file 二维码图片文件  
     * @param logoPath logo路径  
     * @throws IOException  
     */    
    public static void writeToFile(BitMatrix matrix,String format,File file,String logoPath) throws IOException {    
        BufferedImage image = toBufferedImage(matrix);    
        Graphics2D gs = image.createGraphics();    
           
        int ratioWidth = image.getWidth()*2/10;  
        int ratioHeight = image.getHeight()*2/10;  
        //载入logo    
        Image img = ImageIO.read(new File(logoPath));   
        int logoWidth = img.getWidth(null)>ratioWidth?ratioWidth:img.getWidth(null);  
        int logoHeight = img.getHeight(null)>ratioHeight?ratioHeight:img.getHeight(null);  
          
        int x = (image.getWidth() - logoWidth) / 2;   
        int y = (image.getHeight() - logoHeight) / 2;  
          
        gs.drawImage(img, x, y, logoWidth, logoHeight, null);   
        gs.setColor(Color.black);  
        gs.setBackground(Color.WHITE);  
        gs.dispose();    
        img.flush();    
        if(!ImageIO.write(image, format, file)){    
            throw new IOException("Could not write an image of format " + format + " to " + file);      
        }    
    }    
        
    public static BufferedImage toBufferedImage(BitMatrix matrix){    
        int width = matrix.getWidth();    
        int height = matrix.getHeight();    
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
            
        for(int x=0;x<width;x++){    
            for(int y=0;y<height;y++){    
                image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);    
            }    
        }    
        return image;       
    }   
      
    public static BitMatrix deleteWhite(BitMatrix matrix){    
        int[] rec = matrix.getEnclosingRectangle();    
        int resWidth = rec[2] + 1;    
        int resHeight = rec[3] + 1;    
        
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);    
        resMatrix.clear();    
        for (int i = 0; i < resWidth; i++) {    
            for (int j = 0; j < resHeight; j++) {    
                if (matrix.get(i + rec[0], j + rec[1]))    
                    resMatrix.set(i, j);    
            }    
        }    
        return resMatrix;    
    }   
      
    private static void initData(QRCodeParams params){  
        if(params.getWidth() != null){  
            width = params.getWidth();  
        }  
        if(params.getHeight() != null){  
            height = params.getHeight();  
        }  
        if(params.getOnColor() != null){  
            onColor = params.getOnColor();  
        }  
        if(params.getOffColor() != null){  
            offColor = params.getOffColor();  
        }  
        if(params.getLevel() != null){  
            level = params.getLevel();  
        }  
          
    }  
}  
