package com.jadlsoft.utils;


public class CreateID {
	
	/**
	 * Description: 根据当前的ID编号生成下一编号
	 * @param currentID 当前编号
	 * @return 下一编号
	 */
	public static String getNextID (String currentID) {
		if ((currentID == null) || ("".equals(currentID.trim()))){
			return "";
		}
		currentID = currentID.trim();
		int index = getLastCharIndex(currentID);
		if (index == -1){
			//如果ID中只包含数字,将其值加1后返回
			return createNextNumberID(currentID);
		} else {
			//从字母出现的最后位置截取到字符串尾作为当前ID号
			String part1 = currentID.substring(0, index);
			String part2 = currentID.substring(index, currentID.length());
			return (part1 + createNextStringID(part2));
		}
	}
	
	/**
	 * Description: 根据当前的ID编号生成下一编号
	 * @param currentID 当前编号
	 * @param beginIndex 要求自增1的部分
	 * @return 下一编号
	 */
	public static String getNextID (String currentID, int beginIndex) {
		if ((currentID == null) || ("".equals(currentID.trim()))){
			return "";
		}
		currentID = currentID.trim();
		if (currentID.length() < beginIndex) {
			return "";
		}
		String part1 = currentID.substring(0, beginIndex);
		String part2 = currentID.substring(beginIndex, currentID.length());
		return (part1 + (Integer.parseInt(part2) + 1));
	}
	
	/**
	 * Description: 获取当前字符串中字母出现的最后位置
	 * @param id 要查询的字符串
	 * @return 字母出现的最后位置
	 */
	private static int getLastCharIndex (String id) {
		int index = -1;
		for (int i=0;i<id.length()-1;i++) {
			try {
				Integer.parseInt(id.substring(i, i+1));
			} catch (Exception e) {
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Description: 根据原来的ID号生成下一ID号
	 * @param oldID 原来的ID号,是一个字符串,如S005
	 * @return 下一ID号
	 */
	private static String createNextStringID (String oldID) {
		oldID = oldID.trim();
		String prefix = oldID.substring(0,1);//截取前缀,是一个大写的字符
		String numberPart = oldID.substring(1,oldID.length());//截取数字部分
		int totalLength = numberPart.length();//数字部分的长度
		long effectiveNumber = Long.parseLong(numberPart) + 1;//将有效数字的值加1
		int effectiveLength = String.valueOf(effectiveNumber).length();//有效数字的长度
		if (effectiveLength > totalLength) {
			/**
			 * 如果有效数字长度大于该ID规定的数字部分的长度,则将其前缀变成下一字母,
			 * 并将数字部分变成一个指定长度的起始ID号.
			 * 公枪持枪证采用这种方式处理,例如:如果当前ID是A999,则下一ID将变为B001.
			 */
			prefix = String.valueOf((char)(prefix.charAt(0) + 1));
			return (prefix + createInitID(totalLength));
		} else {
			//将前缀和加1后的数字部分合并成下一ID号
			return (prefix + numberPart.substring(0, (totalLength - effectiveLength)) + effectiveNumber);
		}
	}
	
	/**
	 * Description: 根据原来的ID号生成下一ID号
	 * @param oldID 原来的ID号,是一个数字字符串,但可能是这样的数字:000005
	 * @return 下一ID号
	 */
	private static String createNextNumberID (String oldID) {
		if(oldID.trim().length()==String.valueOf(Long.parseLong(oldID.trim())).length()){
			return (Long.parseLong(oldID) + 1)+"";
		}
		int totalLength = oldID.trim().length();
		long effectiveNumber = Long.parseLong(oldID) + 1;//有效数字
		int effectiveLength = String.valueOf(effectiveNumber).length();//有效数字长度
		return (oldID.substring(0,(totalLength - effectiveLength)) + effectiveNumber);
	}
	
	/**
	 * Description: 根据指定的长度生成一个起始ID号
	 * @param length 指定的长度
	 * @return 起始ID号.例:指定3位将生成001
	 */
	private static String createInitID (int length) {
		String id = "";
		for (int i = 0; i < (length - 1); i++) {
			id += "0";
		}
		id += "1";
		return id;
	}

	public static void main (String [] args) {
		System.out.println(CreateID.getNextID("130000091A999"));//持枪证ID
		System.out.println(CreateID.getNextID("410000S006          "));//配售企业ID
		System.out.println(CreateID.getNextID("0000001"));//人员ID
		System.out.println(CreateID.getNextID("4100009",6));//人员ID
	}
}
