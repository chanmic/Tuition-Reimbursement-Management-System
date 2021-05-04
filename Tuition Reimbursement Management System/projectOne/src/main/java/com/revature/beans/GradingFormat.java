package com.revature.beans;

public class GradingFormat {
	private double passingGrade;
	private boolean presentationRequired;
	
	public GradingFormat(double passingGrade, boolean presentationRequired) {
		super();
		this.passingGrade = passingGrade;
		this.presentationRequired = presentationRequired;
	}

	public double getPassingGrade() {
		return passingGrade;
	}

	public void setPassingGrade(double passingGrade) {
		this.passingGrade = passingGrade;
	}

	public boolean isPresentationRequired() {
		return presentationRequired;
	}

	public void setPresentationRequired(boolean presentationRequired) {
		this.presentationRequired = presentationRequired;
	}
}
