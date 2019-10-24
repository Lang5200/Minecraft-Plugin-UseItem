package org.useitem;

import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {
	public static boolean isNumber(String string) {
		if (string == null)
		return false;
		Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
		return pattern.matcher(string).matches();
	}
	
	public static String getPercent(double min,double max) {
        // 创建一个数值格式化对象  
   
        NumberFormat numberFormat = NumberFormat.getInstance();  
   
        // 设置精确到小数点后2位  
   
        numberFormat.setMaximumFractionDigits(0);  
   
        return numberFormat.format(min / max * 100);
	}
	
	public static boolean isProbability(int probability) {
		if(probability > 100) {
			probability = 100;
		}
		
		if(probability < 0) {
			probability = 0;
		}
		
		int random = RandomInt(1,100);
		if(random > probability) {
			return false;
		}
		return true;
	}
	
	public static int RandomInt(int min,int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
}
