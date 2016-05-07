package com.zehao.data.bean;

/**
 * TripInfo entity. @author MyEclipse Persistence Tools
 */

public class TripInfo implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 4299930515523297941L;
	private Integer tripId;
	private Integer typeId;
	private String tripTitle;
	private String tripContent;
	private String tripLogo;
	private String remark;

	// Constructors

	/** default constructor */
	public TripInfo() {
	}

	/** full constructor */
	public TripInfo(Integer typeId, String tripTitle, String tripContent,
			String tripLogo, String remark) {
		this.typeId = typeId;
		this.tripTitle = tripTitle;
		this.tripContent = tripContent;
		this.tripLogo = tripLogo;
		this.remark = remark;
	}

	// Property accessors

	public Integer getTripId() {
		return this.tripId;
	}

	public void setTripId(Integer tripId) {
		this.tripId = tripId;
	}

	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTripTitle() {
		return this.tripTitle;
	}

	public void setTripTitle(String tripTitle) {
		this.tripTitle = tripTitle;
	}

	public String getTripContent() {
		return this.tripContent;
	}

	public void setTripContent(String tripContent) {
		this.tripContent = tripContent;
	}

	public String getTripLogo() {
		return this.tripLogo;
	}

	public void setTripLogo(String tripLogo) {
		this.tripLogo = tripLogo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}