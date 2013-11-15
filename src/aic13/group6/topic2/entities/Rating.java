package aic13.group6.topic2.entities;

public class Rating {
	
	private int rid;
	private float value;
	private Tag tag;
	private Task task;
	
	public Rating(int rid, float value, Tag tag, Task task) {
		this.rid = rid;
		this.value = value;
		this.tag = tag;
		this.task = task;
	}
	
	public int getRid() {
		return rid;
	}
	
	public float getValue() {
		return value;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public Task getTask() {
		return task;
	}
	

}
