package aic13.group6.topic2.pojos;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="task")
public class Task {
	
	private long id;
	
	// Article attributes
	private String url;
	private String title;
	private String text;
	
	private long date;
	private String description;
	private List<String> answerPossibilities;
	private String callbackUrl;
	private double price;
	// to how many workers, this task should be assigned
	private int workerCounter;
	// results of finished Tasks
	@XmlTransient
	private List<Answer> answers;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<String> getAnswerPossibilities() {
		return answerPossibilities;
	}
	
	public void setAnswerPossibilities(List<String> answerPossibilities) {
		this.answerPossibilities = answerPossibilities;
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}
	
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	public int getWorkerCounter() {
		return workerCounter;
	}

	public void setWorkerCounter(int workerCounter) {
		this.workerCounter = workerCounter;
	}

	@XmlTransient
	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
