package com.zehao.data.bean;

import java.util.Date;

/**
 * Views entity. @author MyEclipse Persistence Tools
 */

public class Views implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 3573759633962147312L;
	private Integer viewId;
	private Village village;
	private String viewName;
	private String viewAddress;
	private String viewGuide;
	private String viewInfo;
	private String viewLogo;
	private String openTime;
	private String ticket;
	private String phone;
	private Integer likeNum;
	private String childSign;
	private Integer upView;
	private Date createDate;
	private String remark;
	private String viewPerson;
	private String viewAction;

	// Constructors

	/** default constructor */
	public Views() {
	}

	/** full constructor */
	public Views(Village village, String viewName, String viewAddress,
			String viewGuide, String viewInfo, String viewLogo,
			String openTime, String ticket, String phone, Integer likeNum,
			String childSign, Integer upView, Date createDate, String remark,
			String viewPerson, String viewAction) {
		this.village = village;
		this.viewName = viewName;
		this.viewAddress = viewAddress;
		this.viewGuide = viewGuide;
		this.viewInfo = viewInfo;
		this.viewLogo = viewLogo;
		this.openTime = openTime;
		this.ticket = ticket;
		this.phone = phone;
		this.likeNum = likeNum;
		this.childSign = childSign;
		this.upView = upView;
		this.createDate = createDate;
		this.remark = remark;
		this.viewPerson = viewPerson;
		this.viewAction = viewAction;
	}

	// Property accessors

	public Integer getViewId() {
		return this.viewId;
	}

	public void setViewId(Integer viewId) {
		this.viewId = viewId;
	}

	public Village getVillage() {
		return this.village;
	}

	public void setVillage(Village village) {
		this.village = village;
	}

	public String getViewName() {
		return this.viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewAddress() {
		return this.viewAddress;
	}

	public void setViewAddress(String viewAddress) {
		this.viewAddress = viewAddress;
	}

	public String getViewGuide() {
		return this.viewGuide;
	}

	public void setViewGuide(String viewGuide) {
		this.viewGuide = viewGuide;
	}

	public String getViewInfo() {
		return this.viewInfo;
	}

	public void setViewInfo(String viewInfo) {
		this.viewInfo = viewInfo;
	}

	public String getViewLogo() {
		return this.viewLogo;
	}

	public void setViewLogo(String viewLogo) {
		this.viewLogo = viewLogo;
	}

	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getTicket() {
		return this.ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getLikeNum() {
		return this.likeNum;
	}

	public void setLikeNum(Integer likeNum) {
		this.likeNum = likeNum;
	}

	public String getChildSign() {
		return this.childSign;
	}

	public void setChildSign(String childSign) {
		this.childSign = childSign;
	}

	public Integer getUpView() {
		return this.upView;
	}

	public void setUpView(Integer upView) {
		this.upView = upView;
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

	public String getViewPerson() {
		return this.viewPerson;
	}

	public void setViewPerson(String viewPerson) {
		this.viewPerson = viewPerson;
	}

	public String getViewAction() {
		return this.viewAction;
	}

	public void setViewAction(String viewAction) {
		this.viewAction = viewAction;
	}

}