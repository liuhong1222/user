package cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体对象 
 *
 */
public class CreUser implements Serializable{
	
	private static final long serialVersionUID = 8749079892633512615L;

private Integer id;
	
	private Integer clAccountId; //创蓝erp 用户ID
	
	private Integer companyId; //公司id，0表示总公司
	
	private String userName; //用户姓名
	
	private String userPhone; //电话
	
	private String userPassword; //密码
	
	private Integer userType;//用户类型
	
	private Integer sourceType;//客户来源
	
	private Integer salesmanId;//业务员id
	
	private String salesman; //业务员
	
	private String userEmail; //用户邮箱
	
	private String userQq; // 企业QQ
	
	private Integer userDepartment; //部门id
	
	private Integer userPosition; //用户职位
	
	private String userThumbs; // 用户头像
	
	private String wxOpenid; // 微信用户OPENID，用于绑定微信账号到ERP
	
	private Date lastLoginTime; // 最后登录时间
	
	private String lastLoginIp; // 最后登录IP
	
	private Integer isAuth;//是否认证 1-等待审核  0-未认证 3-已认证 2-认证失败
	
	private String authMemo;//认证备注
	
	private String deleteStatus; // 删除状态，0正常  1删除
	
	private Integer version; // 版本号
	
	private Integer createUser; //创建人
	
	private Date createTime; // 创建时间
	
	private Date updateTime; //修改时间
	
	private Integer agentId; // 关联代理商ID,0为无关联
	
	private String nickName; // 用户昵称
	
	private Integer bitOnoff; // 二进制开关控制 第1位：专人助理

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClAccountId() {
		return clAccountId;
	}

	public void setClAccountId(Integer clAccountId) {
		this.clAccountId = clAccountId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Integer salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserQq() {
		return userQq;
	}

	public void setUserQq(String userQq) {
		this.userQq = userQq;
	}

	public Integer getUserDepartment() {
		return userDepartment;
	}

	public void setUserDepartment(Integer userDepartment) {
		this.userDepartment = userDepartment;
	}

	public Integer getUserPosition() {
		return userPosition;
	}

	public void setUserPosition(Integer userPosition) {
		this.userPosition = userPosition;
	}

	public String getUserThumbs() {
		return userThumbs;
	}

	public void setUserThumbs(String userThumbs) {
		this.userThumbs = userThumbs;
	}

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	public String getAuthMemo() {
		return authMemo;
	}

	public void setAuthMemo(String authMemo) {
		this.authMemo = authMemo;
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

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
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

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getBitOnoff() {
		return bitOnoff;
	}

	public void setBitOnoff(Integer bitOnoff) {
		this.bitOnoff = bitOnoff;
	}

	
}
