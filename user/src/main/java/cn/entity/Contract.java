package cn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 合同实体对象
 *
 */
public class Contract implements Serializable{

	private static final long serialVersionUID = 3351886175213479372L;

	private Integer id;
	
	private Integer creUserId; // 关联用户ID
	
	private Date createDate; //创建日期
	
	private String contractName; //合同名称
	
	private String contractNo; //合同编号
	
	private String orderNo; //充值订单号
	
	private String fileUrl; //合同文件地址
	
	private Integer status; //生效标识 1-生效  0-未生效
	
	private Integer flag; //启用标识  1-启用 0-禁用
	
	private Date createTime; //创建时间
	
	private String createBy; //创建人
	
	private String lastRepair; //最后修改人
	
	private Date lastTime; //最后修改时间

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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastRepair() {
		return lastRepair;
	}

	public void setLastRepair(String lastRepair) {
		this.lastRepair = lastRepair;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	
}
