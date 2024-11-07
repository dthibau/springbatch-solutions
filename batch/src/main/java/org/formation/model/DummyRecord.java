package org.formation.model;

import java.time.LocalDateTime;

public class DummyRecord {

	private double randomNumber;
	private LocalDateTime instant;
	
	public DummyRecord() {
		instant = LocalDateTime.now();
		randomNumber = Math.random();
	}

	public double getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(double randomNumber) {
		this.randomNumber = randomNumber;
	}

	public LocalDateTime getInstant() {
		return instant;
	}

	public void setInstant(LocalDateTime instant) {
		this.instant = instant;
	}

	@Override
	public String toString() {
		return "DummyRecord [randomNumber=" + randomNumber + ", instant=" + instant + "]";
	}
	
}
