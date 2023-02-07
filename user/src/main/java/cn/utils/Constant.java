package cn.utils;

/**
 * 简码常量类
 * 
 *
 */
public class Constant {
	
	/**
	 * 充值
	 */
	public final static String TRD_ORDER_TYPE_RECHARGE = "1";

	/**中
	 * 退款
	 */
	public final static String TRD_ORDER_TYPE_REFUNDS = "2";
	
	/**
	 * 处理中
	 */
	public final static String TRD_ORDER_STATUS_PROCESSING = "0";
	
	/**
	 * 成功
	 */
	public final static String TRD_ORDER_STATUS_SUCCEED = "1";
	
	/**
	 * 失败
	 */
	public final static String TRD_ORDER_STATUS_FAILED = "1";
	
	/**
	 * redis key
	 */
	public final static String REDIS_KEY = "alipay:qrcode:";
	
}
