package aic13.group6.topic2.entities;

import java.util.Set;

public class Task {
	
	private int tid;
	private String description;
	private String callBackLink;
	private float price;
	private Article article;
	private Set<Worker> workers;
	
	public Task(int tid, String description, Article article, Set<Worker> workers) {
		this.tid = tid;
		this.description = description;
		this.article = article;
		this.workers = workers;
		
		setPrice(-1);
		callBackLink = "not set";
	}
	
	public int getTid() {
		return tid;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getCallBackLink() {
		return callBackLink;
	}
	
	public Article getArticle() {
		return article;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	
	public Set<Worker> getWorkers() {
		return workers;
	}
	
	

}
