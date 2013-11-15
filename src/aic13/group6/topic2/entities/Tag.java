package aic13.group6.topic2.entities;

import java.util.Set;

public class Tag {
	
	private String name;
	private Task task;
	private Set<Article> articles;
	
	public Tag(String name, Task task, Set<Article> articles) {
		this.name = name;
		this.task = task;
		this.articles = articles;
	}
	
	public String getName() {
		return name;
	}
	
	public Task getTask() {
		return task;
	}
	
	public Set<Article> getArticles() {
		return articles;
	}

}
