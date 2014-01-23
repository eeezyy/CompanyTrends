package aic13.group6.topic2.mock.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="TASK")
@XmlRootElement(name="task")
public class Task {
	
	@Id
	private int id;
	
	private String description;
	private String answerPossibilities;
	private String callbackLink;
	private double price;
	private String answer;
	private String user;
	
	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement
	public String getAnswerPossibilities() {
		return answerPossibilities;
	}
	
	public void setAnswerPossibilities(String answerPossibilities) {
		this.answerPossibilities = answerPossibilities;
	}
	
	@XmlElement
	public String getCallbackLink() {
		return callbackLink;
	}
	
	public void setCallbackLink(String callbackLink) {
		this.callbackLink = callbackLink;
	}
	
	@XmlElement
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	@XmlElement
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@XmlElement
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	

}
