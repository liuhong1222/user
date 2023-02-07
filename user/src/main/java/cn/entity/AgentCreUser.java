package cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理商-用户映射表
 *
 */
public class AgentCreUser implements Serializable{

	private static final long serialVersionUID = 3707264437082473807L;

	private Integer id;
	
	private Integer agentId; //代理商id
	
	private Integer creUserId; //用户id
	
	private Date createTime; //创建时间
	
	private Date updateTime; //修改时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Integer getCreUserId() {
		return creUserId;
	}

	public void setCreUserId(Integer creUserId) {
		this.creUserId = creUserId;
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
