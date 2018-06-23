package com.jadlwork.utils;

import java.io.File;
import java.io.FileFilter;


public class T {

	public static void main(String[] args) {
		T t = new T();
		t.t1();
	}
	
	private void t1() {
		
		File destFile = new File("C:\\Users\\Administrator\\Desktop\\bak\\t2.zip");
		
		String tmpRootPath = null;
		try {
			tmpRootPath = ZipUtils.decompressZip(destFile.getAbsolutePath(), null, true);
			
			File tmpRoot = new File(tmpRootPath);
			File[] listFiles = tmpRoot.listFiles();
			if (listFiles == null || listFiles.length==0) {
				return;
			}
			
			File tmpFile = FileUtils.findFile(tmpRootPath, ".db");
			
			if (tmpFile == null) {
				return;
			}
			try {
				String sha1 = FileSHA1Utils.getFileSha1(tmpFile.getAbsolutePath());
//				FileUtils.delFolder(tmpRootPath);
			} catch (Exception e) {
				return;
			}
		} catch (Exception e) {
			return;
		}
		
		
		File parentFile = destFile.getParentFile();
		
		File[] files = parentFile.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				return false;
			}
		});
		
		for (File file : files) {
			if (file.isDirectory()) {
				FileUtils.delFolder(file.getAbsolutePath());
			}
		}
		
		
	}
	
	private void delDir(File dir) {
		if (dir == null || !dir.isDirectory()) {
			return;
		}
		
		File[] listFiles = dir.listFiles();
		if (listFiles == null || listFiles.length==0) {
			dir.delete();
			return;
		}
		
		for (File fileItem : listFiles) {
			if (fileItem.isFile()) {
				boolean delete = fileItem.delete();
				if (!delete) {
					delete = fileItem.delete();
				}
				if (!delete) {
					System.out.println("删除文件【"+fileItem.getAbsolutePath()+"】失败，导致删除目录【"+dir.getAbsolutePath()+"】失败！");
					return;
				}
			}else if (fileItem.isDirectory()) {
				delDir(fileItem);
			}
		}
		
		dir.delete();
	}
	
	
}
