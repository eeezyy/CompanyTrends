package aic13.group6.topic2.pojos;

public enum AnswerOptions {
	
	POSITIVE (1.0, "positive"),
	NEUTRAL (0.0, "neutral"),
	NEGATIVE (-1.0, "negative"),
	IRRELEVANT (-10.0, "irrelevant"),
	;
	
	private final double value;
	private final String text;
	
	AnswerOptions(double value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public String text() {
		return text;
	}
	
	public double value() {
		return value;
	}
	
}
