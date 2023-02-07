package cn.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 佣金折扣计算
 * 
 *
 */
public class MoneyCommission {

	private final static Logger logger = LoggerFactory.getLogger(MoneyCommission.class);

	/**
	 * 获取佣金
	 * @param sumMoney  涉及金额
	 * @param discount   折扣
	 * @return
	 */
	public static Double getCommission(String sumMoney,String discount){
		 try {
			 NumberFormat nbf=NumberFormat.getPercentInstance();
			 Double number=(Double) nbf.parse(discount);
			 Double money=MoneyCommission.multiply(Double.valueOf(sumMoney),number,3);
			 return money;
		} catch (ParseException e) {
			e.printStackTrace();
			logger.info("折扣获取异常");
		}
		 return null;
	}
	
	/**
	 * 乘以
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static double multiply(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue(); 
	}

	

	public static void main(String[] args) {
//		Double big = MoneyCommission.getCommission("2345", "0%");
//		System.out.println(big);
		
		String str="第一";
		System.out.println(str.indexOf("第一级"));
	}

}
