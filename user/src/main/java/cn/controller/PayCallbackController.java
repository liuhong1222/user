package cn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

import cn.service.TrdOrderService;
import main.java.cn.common.BackResult;

@RestController
@RequestMapping("/payCallback")
public class PayCallbackController {

	private final static Logger logger = LoggerFactory.getLogger(PayCallbackController.class);

	@Autowired
	private TrdOrderService trdOrderService;

	@RequestMapping("/alipayRecharge")
	public BackResult<Boolean> recharge(String outTrdOrder,String orderStatus,String traOrder) {
		
		logger.info("订单号：【" + outTrdOrder +"】充值回调状态：【" + orderStatus + "】");
		BackResult<Boolean> result = new BackResult<Boolean>();
		try {
			trdOrderService.payCallBack(outTrdOrder, orderStatus,traOrder);
			result.setResultObj(Boolean.TRUE);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("订单号：【" + outTrdOrder + "】出现订单处理异常：" + e.getMessage());
		} 
		
		return result;
	}

	public static void main(String[] args) {

		try {
			// 真 2017092808974970 测 2016081500253188

			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
					"2017092808974970",
					"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCM4Bu0uEw1PJrhVTJXImHFFj2LkrRgTOOMLNN9mosKkS7hkEvxaP4eWYGwhATR+oNB4Z4Iaz/Y/7mMs2rPLckxsTKYWUUt9/RdIbzcy/ofp+LFIPN7mmMawXgl0RYwy780cSc6weTGOiG0LSDmQP0oV41FeJiU/4F3y1ov2wF8rpZmjTbmxLv7oeUdd8NEpFqGYZqeBt1q7TyNuUopq6NOj0qIjKqYoc4oC77apRvfGVK2mU8TpDWJI2h+D+Sjbsc2cr31+PrF1owI3JOabo6KUnvBfiDRQUDb/fESonXEsIoXIowS4ty7GnE1HQX3oSmlYu5Zo4hQ3Bp7GhqKA0FLAgMBAAECggEAXX0MZe44hMvnbMqBUdn7UDPmPA3rrkzYZVoKMDyzMRwQJJkCDP9ERRfZPS8fk8nP+ZSkr6mdAYETvhADPeKAtn2FuhzkEBGRFY0iLNUg9IkRsQihDNH0GvlxpocPVuqfxUmiYxfxnqtNWqjoQGW4m2Ee24+gkqDNZpaJD2Ejp32pMN0JAeAA5VrNo9mkf2+s7LskpcSpyLr3RMhmMbola0adFfrqaRKhO6j9RAzrWgIfAqAuLUcofk6IzO/UYbI/tzItYvpLesWEV1ohkH7rnlpRZ/sm1afP6V2uvv6w89dQAWQ0vtN6yJrTbQ9GMRsjIOrRBYfpL971pBj8Qy36AQKBgQDBaQpiFB7aPXrLwKNb7Hv1eF9a323KPlF9qnSMTwq1fNLn/h41DrrzpyjHqcj/Wnt1Y0CAcJdEAaF1BYbzTCC9tvj2175zWlBpM+Sg73BD9VOYZOSKOE3xRrWRvd13X/g/qTN7rAJeYq8due9nHLV9cel4ojmHGGBz9kDhmKBWiwKBgQC6dti8A2ceht8zLOWu7+C/HDqzY9l4ztNzvCm37ouaRloVkfEzJKFoNGaZB7DpIGb/7oKkIWH/4gTFF1bbDxQD1rh/QNdMOQPYfyJSnr9PGFbgJWiBPOAo8IgzdrBSaLiM9lOEIQrdQMViRI/+fa0jDb6fUVHC7Eq9UMyx0djYQQKBgQCjGVNaNiF2ZNYCsKaRxDEcZa/zz+qj3D2YPYAfOiCLv4GThaYvlAZvpg0rorAPNlxaN6KTLV9CSCXBqzGMRixuD7iPYIwIdqbMsKgiVwhTJrSiFKLtNGH2D2zaRERYgKO1+5eguQMeWRXXeY67Y2hhKwh5ZEQi+pmL8KPyGmVL1QKBgHiTX6V8odMIrg5+quvLrE6Ip6DXIYrQW8YMWAHbnKSfd6NZI1rKBiHHOM0ePxURYel/xzTxbVi1RBZa+ExhXLT6xYVsXYyplcYKuBwrEVTJTWxWZFSmE+IFYF2/E1fG5ggpRWo8n7ThLIJWCbMse6gePDTzteZRPCddt3AJXarBAoGAaCRbWO7eDPDHlaKFCsX5j/ymjwC00qrRB+nDYHg5usAfScR9/+QD4yWI0WLYVJgGHgSCp4CF2FfjwGMDO5R53d1mELJ2m2/f/a8TcA7unnvCxM1oFL4J07YKvBQVjkFcsJm6hkKB5w4//DHyK1QxppLEQZZfYScrvXCKSEPBiEc=",
					"json", "utf-8",
					"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoJMlNgknDQZ/YMtQwSz/9WbFX8BAXeNHbmee8/YHsMZLJyxGsjI9Y4mIqmGCghRVmtsmtg5UsW1bRVoZFUtsNKMdgqoH7UDX33AQgHIGzGJZlVjMN2IfcErstBMdKgi0FLwkFZse+aonRiTNNVxdOpmbiFHZVyKfZNM+Udg+eflC17Muy6k7eGmlOFQx6ke14RATkbTLPkUTHh8RszAC2uIH5YqDlwtIfaZjz40z0MKWDX1WpIZqcE+OZN7U06DEmRwKJT/9ObG+GKwWaQgJ0DnuEPd5WVtjmkIfS1J6lzZc3GaG58clvtBVOYWTs7znfinSTPR+BTR3LOeJYC1a7QIDAQAB",
					"RSA2");

			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();// 创建API对应的request类

			// AlipayTradePayModel model = new AlipayTradePayModel();
			// request.setBizModel(model);
			// .setNotifyUrl("http://www.test-notify-url.com")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置

			// model.setOutTradeNo(System.currentTimeMillis()+"");
			// model.setSubject("Iphone6 16G");
			// model.setTotalAmount("0.01");
			// model.setAuthCode("xxxxx");//沙箱钱包中的付款码
			// model.setScene("bar_code");

			request.setBizContent("{" + "    \"out_trade_no\":\"2015032001010101116\"," + "    \"total_amount\":\"0.01\","
					+ "    \"subject\":\"Iphone6 16G\"," + "    \"store_id\":\"NJ_001\","
					+ "    \"timeout_express\":\"90m\"}");// 设置业务参数

			
			request.setNotifyUrl("https://kh_bd.253.com/payCallback/alipayRecharge");
			AlipayTradePrecreateResponse response = alipayClient.execute(request);

			if (response.isSuccess()) {
				System.out.println(response.getBody());
				
				JSONObject json = JSONObject.parseObject(response.getBody());
				
				JSONObject json1 = JSONObject.parseObject(json.get("alipay_trade_precreate_response").toString());
				
				System.out.println(json1.get("code"));
				System.out.println(json1.get("qr_code"));
			} else {
				System.out.println("调用失败");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	

}
