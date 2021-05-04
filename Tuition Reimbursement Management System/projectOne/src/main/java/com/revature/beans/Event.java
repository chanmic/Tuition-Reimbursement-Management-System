package com.revature.beans;

public enum Event {
	UNIVERSITYCOURSE(0.8), SEMINAR(0.6), CERTPREP(0.75), CERTIFICATION(1.0), TECHNICALTRAINING(0.9), OTHER(0.3);

	private double percent;

	Event(double percent) {
		this.percent = percent;
	}

	public double getPercent() {
		return percent;
	}
}