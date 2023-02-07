package cn.entity;

import java.io.Serializable;

/**
 * 代理商支付宝信息
 *
 */
public class AgentAlipayInfo implements Serializable{

	private static final long serialVersionUID = -2195215833364972111L;

	private String alipayAppid;
	
	private String alipayPayurl;
	
	private String alipayPrivatekey;
	
	private String alipayPublickey;
	
	private String alipayCallbackurl;

	public String getAlipayAppid() {
		return alipayAppid;
	}

	public void setAlipayAppid(String alipayAppid) {
		this.alipayAppid = alipayAppid;
	}

	public String getAlipayPayurl() {
		return alipayPayurl;
	}

	public void setAlipayPayurl(String alipayPayurl) {
		this.alipayPayurl = alipayPayurl;
	}

	public String getAlipayPrivatekey() {
		return alipayPrivatekey;
	}

	public void setAlipayPrivatekey(String alipayPrivatekey) {
		this.alipayPrivatekey = alipayPrivatekey;
	}

	public String getAlipayPublickey() {
		return alipayPublickey;
	}

	public void setAlipayPublickey(String alipayPublickey) {
		this.alipayPublickey = alipayPublickey;
	}

	public String getAlipayCallbackurl() {
		return alipayCallbackurl;
	}

	public void setAlipayCallbackurl(String alipayCallbackurl) {
		this.alipayCallbackurl = alipayCallbackurl;
	}
	
	
}
