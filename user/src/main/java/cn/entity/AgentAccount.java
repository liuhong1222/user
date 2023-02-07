package cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理商账户
 *
 */
public class AgentAccount implements Serializable{

	private static final long serialVersionUID = -5446684420132861270L;

	private Integer id;
	
	private String agentId; //代理商id
	
	private Integer balance; //空号剩余条数
	
	private Integer warnNumber; //空号预警条数
	
	private Integer creditNumber; //空号透支条数
	
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

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getWarnNumber() {
		return warnNumber;
	}

	public void setWarnNumber(Integer warnNumber) {
		this.warnNumber = warnNumber;
	}

	public Integer getCreditNumber() {
		return creditNumber;
	}

	public void setCreditNumber(Integer creditNumber) {
		this.creditNumber = creditNumber;
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
