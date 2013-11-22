package aic13.group6.topic2.entities;

import java.util.Calendar;
import java.util.Set;

public class Article {
	
	private String url;
	private Calendar date;
	private String text;
	private Boolean usable;
	private Set<Tag> tags;
	
	public Article () {
		setUrl(null);
		setDate(null);
		setText(null);
		setUsable(true);
		setTags(null);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the tags
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @return the usable
	 */
	public Boolean getUsable() {
		return usable;
	}

	/**
	 * @param usable the usable to set
	 */
	public void setUsable(Boolean usable) {
		this.usable = usable;
	}

}
