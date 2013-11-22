package aic13.group6.topic2.entities;

import java.util.Set;

public class Worker {
	
	private int wid;
	private float rating; // 1 is good, -1 is bad
	private Set<Task> tasks;
	
	public Worker() {
		setWid(-1);
		setRating(-10);
		setTasks(null);		
	}

	/**
	 * @return the wid
	 */
	public int getWid() {
		return wid;
	}

	/**
	 * @param wid the wid to set
	 */
	public void setWid(int wid) {
		this.wid = wid;
	}

	/**
	 * @return the rating
	 */
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
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
