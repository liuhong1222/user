package cn.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商充值退款
 *
 */
public class AgentOrder implements Serializable{

	private static final long serialVersionUID = 1602933349592044798L;

	private Integer id;	
	private String agentId; //代理商id	
	private Integer productId; //产品Id
	private String orderNo; //订单编号
	private String tradeNo; //第三方支付单号	
	private Integer type; //操作类型 1 充值 2 退款 3给用户充值',
	private BigDecimal price;//单价
	private Integer number; //条数
	private BigDecimal money; //金额
	private Integer payType; //交易类型：1支付宝  2银联  3创蓝充值  4管理员充值  5对公转账  6赠送 7 官网扫码充值
	private Date payTime; //支付时间
	private Integer status; //状态：0待处理，1成功，2失败
	private String remark; //备注
	private Integer roleType; //充值操作角色   0-代理商  1-管理员 2-用户
	private Integer version; //版本号
	private Date createTime; //创建时间
	private Date updateTime; //修改时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getRoleType() {
		return roleType;
	}
	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
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
	
	
}
