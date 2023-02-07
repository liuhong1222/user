package cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础日志
 *
 */
public class BaseLog implements Serializable{

	private static final long serialVersionUID = -4971030481207552132L;

	private Integer id;
	
	private Integer userId; // 关联用户id
	
	private String erpId; // 剩余多少条数提醒
  	
	private Date createDate; // 绑定ip
	
	private String operateTable; // 提醒的手机号码串
	
	private String operation; // 身份认证二要素接口条数
	
	private String content; // 银行卡鉴权接口条数
	
	private String ip; // 号码实时在线检测接口条数
	
	private Date createTime; // 运营商三要素接口条数

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getErpId() {
		return erpId;
	}

	public void setErpId(String erpId) {
		this.erpId = erpId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOperateTable() {
		return operateTable;
	}

	public void setOperateTable(String operateTable) {
		this.operateTable = operateTable;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
