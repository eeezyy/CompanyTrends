package aic13.group6.topic2.entities;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name="article")
@XmlAccessorType(XmlAccessType.FIELD)
public class Article {
	
	@Id
	private String url;
	private long date;
	private String title;
	private String text;
	private Boolean usable;
	@XmlTransient
	@ManyToMany(mappedBy = "articles", targetEntity=Job.class, cascade = CascadeType.PERSIST)
	private List<Job> jobs;
	@OneToMany(targetEntity=Rating.class, mappedBy = "article")
	private List<Rating> ratings;
	
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
	public long getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title of the article to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	
	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
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

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	
}
