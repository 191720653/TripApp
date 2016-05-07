package com.zehao.data.bean;

/**
 * TripType entity. @author MyEclipse Persistence Tools
 */

public class TripType implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1843360177798736319L;
	private Integer typeId;
	private String typeTitle;
	private String typeLogo;
	private String remark;

	// Constructors

	/** default constructor */
	public TripType() {
	}

	/** full constructor */
	public TripType(String typeTitle, String typeLogo, String remark) {
		this.typeTitle = typeTitle;
		this.typeLogo = typeLogo;
		this.remark = remark;
	}

	// Property accessors

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeTitle() {
		return this.typeTitle;
	}

	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}

	public String getTypeLogo() {
		return this.typeLogo;
	}

	public void setTypeLogo(String typeLogo) {
		this.typeLogo = typeLogo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}