package aic13.group6.topic2.entities;

import java.util.Calendar;
import java.util.Set;

public class Article {
	
	private int aid;
	private Calendar date;
	private Set<Tag> tags;
	
	public Article (int aid, Calendar date, Set<Tag> tags) {
		this.aid = aid;
		this.date = date;
		this.tags = tags;
	}
	
	public int getAid() {
		return aid;
	}

	public Calendar getDate() {
		return date;
	}
	
	public Set<Tag> getTags() {
		return tags;
	}


}
