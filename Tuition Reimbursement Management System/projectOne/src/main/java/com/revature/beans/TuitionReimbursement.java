package com.revature.beans;

import java.util.ArrayList;
import java.util.List;

public class TuitionReimbursement {
	private int id;
	private String employee; // Employee who submitted the form
	private String dirSu; // Direct supervisor of employee
	private String depHe; // Which department head approved?
	private String benCo; // Which benefits coordinator approved?
	private String date;
	private String time;
	private String location;
	private String description;
	private double cost;
	private GradingFormat gradingFormat;
	private Event event;
	private String justification;
	private boolean urgent;
	/*
	 * Approval levels: 
	 * 1 - Form has been submitted and sent to DirSu
	 * 2 - Form has been approved by DirSu and sent to DepHe
	 * 3 - Form has been approved by DepHe and sent to BenCo
	 * 4 - Form has been approved by BenCo and amount has been rewarded
	 */
	private int approvalLevel; 
	private double pendingReimbursement;
	private boolean exceedsFunds;
	private List<String> attachments = new ArrayList<String>();

	public TuitionReimbursement(int id, String employee, String dirSu, String depHe, String benCo, String date, String time, String location, String description, double cost,
			GradingFormat gradingFormat, Event event, String justification, int approvalLevel, boolean urgent, double pendingReimbursement, boolean exceedsFunds, List<String> attachments) {
		super();
		this.id = id;
		this.employee = employee;
		this.dirSu = dirSu;
		this.depHe = depHe;
		this.benCo = benCo;
		this.date = date;
		this.time = time;
		this.location = location;
		this.description = description;
		this.cost = cost;
		this.gradingFormat = gradingFormat;
		this.event = event;
		this.justification = justification;
		this.approvalLevel = approvalLevel;
		this.urgent = urgent;
		this.pendingReimbursement = pendingReimbursement;
		this.exceedsFunds = exceedsFunds;
		this.attachments = attachments;
	}

	public int getId() {
		return id;
	}
	
	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public GradingFormat getGradingFormat() {
		return gradingFormat;
	}

	public void setGradingFormat(GradingFormat gradingFormat) {
		this.gradingFormat = gradingFormat;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public int getApprovalLevel() {
		return approvalLevel;
	}

	public void setApprovalLevel(int approvalLevel) {
		this.approvalLevel = approvalLevel;
	}

	public String getDirSu() {
		return dirSu;
	}

	public void setDirSu(String dirSu) {
		this.dirSu = dirSu;
	}

	public String getDepHe() {
		return depHe;
	}

	public void setDepHe(String depHe) {
		this.depHe = depHe;
	}

	public String getBenCo() {
		return benCo;
	}

	public void setBenCo(String benCo) {
		this.benCo = benCo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	public double getPendingReimbursement() {
		return pendingReimbursement;
	}

	public void setPendingReimbursement(double pendingReimbursement) {
		this.pendingReimbursement = pendingReimbursement;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

	public boolean isExceedsFunds() {
		return exceedsFunds;
	}

	public void setExceedsFunds(boolean exceedsFunds) {
		this.exceedsFunds = exceedsFunds;
	}
	
}
