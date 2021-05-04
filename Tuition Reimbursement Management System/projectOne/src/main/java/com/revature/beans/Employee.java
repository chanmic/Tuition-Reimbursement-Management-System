package com.revature.beans;

import java.util.ArrayList;
import java.util.List;

public class Employee {
	protected String username;
	protected String password;
	/*
	 * User Roles:
	 * 1 - Employee
	 * 2 - Direct Supervisor
	 * 3 - Department Head
	 * 4 - Both Direct Supervisor and Department Head
	 * 5 - Benefits Coordinator
	 */
	protected int role;
	private List<Integer> formId = new ArrayList<Integer>();
	double availableReimbursement;
	
	public Employee() {
		super();
	}

	public Employee(String username, String password, int role, List<Integer> formId, double availableReimbursement) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.formId = formId;
		this.availableReimbursement = availableReimbursement;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
	
	public List<Integer> getFormId() {
		return formId;
	}

	public void setFormId(List<Integer> formId) {
		this.formId = formId;
	}

	public double getAvailableReimbursement() {
		return availableReimbursement;
	}

	public void setAvailableReimbursement(double availableReimbursement) {
		this.availableReimbursement = availableReimbursement;
	}
	

	
}
