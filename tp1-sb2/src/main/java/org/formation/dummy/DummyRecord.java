package org.formation.dummy;

import java.util.Date;

public class DummyRecord {

	private double randomNumber;
	private Date instant;
	
	public DummyRecord() {
		instant = new Date();
		randomNumber = Math.random();
	}

	public double getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(double randomNumber) {
		this.randomNumber = randomNumber;
	}


	public Date getInstant() {
		return instant;
	}

	public void setInstant(Date instant) {
		this.instant = instant;
	}

	@Override
	public String toString() {
		return "DummyRecord [randomNumber=" + randomNumber + ", instant=" + instant + "]";
	}
	
}
