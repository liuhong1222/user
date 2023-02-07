package cn.utils;

import java.util.Random;


/**
 * 订单流水号生成
 *
 */
public class OrderNo{


	/** 上次生成ID的时间截 */
	private  static long lastTimestamp = -1L;

	/**
	 *  (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
				// 阻塞到下一个毫秒,获得新的时间戳
			timestamp = tilNextMillis(lastTimestamp);
		}
		
		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		// 移位并通过或运算拼到一起组成64位的ID
		return timestamp;
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp
	 *            上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * 
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}
	
	
	/**
	 * 订单
	 * @param userId
	 * @return
	 */
	public static String getOrderNo16(){
		 OrderNo idWorker =new OrderNo();
		 StringBuffer sbf=new StringBuffer();
	     long time= idWorker.nextId();
	     sbf.append(time).append(0);
	     sbf.append(OrderNo.random());
		 return sbf.toString();
	}
	
	/**
	 * 流水号
	 * @param userId
	 * @return
	 */
	public static String getSerial16(){
		 OrderNo idWorker =new OrderNo();
		 StringBuffer sbf=new StringBuffer();
	     long time= idWorker.nextId();
	     sbf.append(0);
	     sbf.append(time); 
	     sbf.append(OrderNo.random());
		 return sbf.toString();
	}
	
	
	/**
     * 随机生成
     * @return
     */
    public static int random(){
    	int max=99;
        int min=10;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }
    
    //test
	public static void main(String[] args) {
			System.out.println("流水号"+getSerial16()+" 长度："+getSerial16().length());
			System.out.println("订单"+getOrderNo16()+" 长度："+getOrderNo16().length());
			
            
    }

}
