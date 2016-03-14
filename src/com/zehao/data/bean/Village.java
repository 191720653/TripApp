package com.zehao.data.bean;

import java.util.Date;

/**
 * Village entity. @author MyEclipse Persistence Tools
 */

public class Village implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 2335642660173160786L;
	private Integer villageId;
	private String villageName;
	private String villageAddress;
	private String villageInfo;
	private String villageLogo;
	private String historyRecord;
	private String villageStory;
	private Integer likeNum;
	private Date createDate;
	private String remark;

	// Constructors

	/** default constructor */
	public Village() {
	}

	/** full constructor */
	public Village(String villageName, String villageAddress,
			String villageInfo, String villageLogo, String historyRecord,
			String villageStory, Integer likeNum, Date createDate,
			String remark) {
		this.villageName = villageName;
		this.villageAddress = villageAddress;
		this.villageInfo = villageInfo;
		this.villageLogo = villageLogo;
		this.historyRecord = historyRecord;
		this.villageStory = villageStory;
		this.likeNum = likeNum;
		this.createDate = createDate;
		this.remark = remark;
	}

	// Property accessors

	public Integer getVillageId() {
		return this.villageId;
	}

	public void setVillageId(Integer villageId) {
		this.villageId = villageId;
	}

	public String getVillageName() {
		return this.villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getVillageAddress() {
		return this.villageAddress;
	}

	public void setVillageAddress(String villageAddress) {
		this.villageAddress = villageAddress;
	}

	public String getVillageInfo() {
		return this.villageInfo;
	}

	public void setVillageInfo(String villageInfo) {
		this.villageInfo = villageInfo;
	}

	public String getVillageLogo() {
		return this.villageLogo;
	}

	public void setVillageLogo(String villageLogo) {
		this.villageLogo = villageLogo;
	}

	public String getHistoryRecord() {
		return this.historyRecord;
	}

	public void setHistoryRecord(String historyRecord) {
		this.historyRecord = historyRecord;
	}

	public String getVillageStory() {
		return this.villageStory;
	}

	public void setVillageStory(String villageStory) {
		this.villageStory = villageStory;
	}

	public Integer getLikeNum() {
		return this.likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}