package cn.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值订单 实体对象
 *
 */
public class TrdOrder implements Serializable{
	
	private static final long serialVersionUID = -5092792682634256373L;

	private Integer id;
	
	private Integer creUserId; // 关联用户ID
	
	private String orderNo; // 订单编号
	
	private String tradeNo; // 第三方支付订单号
	
	private String clOrderNo; // 创蓝充值订单业务号
	
	private Integer productsId; // 产品ID
	
	private Integer number; // 条数
	
	private BigDecimal money; // 总价
	
	private String payType; // 支付类型：1支付宝，2银联 3创蓝充值
	
	private Date payTime; // 支付时间
	
	private String type; // 交易类型 1充值 2退款
	
	private String status; // 状态：0未支付，1支付成功，2支付失败
	
	private String deleteStatus; // 删除状态：0正常，1已删除
	
	private Integer version; // 版本号
	
	private Date createTime; // 创建时间
	
	private Date updateTime; // 修改时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreUserId() {
		return creUserId;
	}

	public void setCreUserId(Integer creUserId) {
		this.creUserId = creUserId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getClOrderNo() {
		return clOrderNo;
	}

	public void setClOrderNo(String clOrderNo) {
		this.clOrderNo = clOrderNo;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getProductsId() {
		return productsId;
	}

	public void setProductsId(Integer productsId) {
		this.productsId = productsId;
	}
	
	

}
