package aic13.group6.topic2.entities;

import java.util.Set;

public class Tag {
	
	private String name;
	private Set<Article> articles;
	private Set<Task> tasks;
	
	public Tag() {
		setName(null);
		setArticles(null);
		setTasks(null);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the articles
	 */
	public Set<Article> getArticles() {
		return articles;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	/**
	 * @return the tasks
	 */
	public Set<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

}
