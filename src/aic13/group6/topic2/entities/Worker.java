package aic13.group6.topic2.entities;

import java.util.Set;

public class Worker {
	
	private int wid;
	private float rating; // 1 is good, 0 is bad
	private Set<Task> tasks;
	
	public Worker(int wid, float rating, Set<Task> tasks) {
		this.wid = wid;
		this.rating = rating;
		this.tasks = tasks;
	}
	
	public int getWid() {
		return wid;
	}
	
	public float getRating() {
		return rating;
	}
	
	public void setRating(float newRating) {
		rating = newRating;
	}
	
	public Set<Task> getTasks() {
		return tasks;
	}

}
