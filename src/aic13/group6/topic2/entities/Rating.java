package aic13.group6.topic2.entities;

public class Rating {
	
	private int rid;
	private float value;
	private Task task;
	private Tag tag;
	
	public Rating() {
		setRid(-1);
		setValue(-10);
		setTask(null);
		setTag(null);
	}

	/**
	 * @return the rid
	 */
	public int getRid() {
		return rid;
	}

	/**
	 * @param rid the rid to set
	 */
	public void setRid(int rid) {
		this.rid = rid;
	}

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the tag
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}
}