package cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * api 账户信息表
 *
 */
public class ApiAccountInfo implements Serializable{

	private static final long serialVersionUID = -6901184879355066563L;
	
	private Integer id;
	
	private Integer creUserId; // 关联用户id
	
	private String name; // api账户
	
	private String password; // 密码
	
	private Integer numReminding; // 剩余多少条数提醒
  	
	private String bdIp; // 绑定ip
	
	private String pushMobiles; // 提醒的手机号码串
	
	private String resultPwd;//检测结果压缩包密码
	
	private Integer deleteStatus; // 删除状态
	
	private Integer version; // 版本号
	
	private Date createTime; // 创建时间
	
	private Date updateTime; //修改时间

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getNumReminding() {
		return numReminding;
	}

	public void setNumReminding(Integer numReminding) {
		this.numReminding = numReminding;
	}

	public String getBdIp() {
		return bdIp;
	}

	public void setBdIp(String bdIp) {
		this.bdIp = bdIp;
	}

	public String getPushMobiles() {
		return pushMobiles;
	}

	public void setPushMobiles(String pushMobiles) {
		this.pushMobiles = pushMobiles;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
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

	public String getResultPwd() {
		return resultPwd;
	}

	public void setResultPwd(String resultPwd) {
		this.resultPwd = resultPwd;
	}
	
	
}
