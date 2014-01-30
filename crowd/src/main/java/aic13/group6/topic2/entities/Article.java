package aic13.group6.topic2.entities;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aic13.group6.topic2.daos.DAOArticle;
import aic13.group6.topic2.daos.DAORating;

@Entity
@XmlRootElement(name="article")
@XmlAccessorType(XmlAccessType.FIELD)
public class Article {
	
	@Id
	private String url;
	private long date;
	private String title;
	@XmlTransient
	private String text;
	@XmlTransient
	@ManyToMany(mappedBy = "articles", targetEntity=Job.class, cascade = CascadeType.PERSIST)
	private List<Job> jobs;
	@XmlAnyAttribute
	@OneToMany(targetEntity=Rating.class, mappedBy = "article")
	private List<Rating> ratings;
	// how many tasks are left to finish
	private int workerCounter;
	
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

	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public int getWorkerCounter() {
		return workerCounter;
	}

	public void setWorkerCounter(int workerCounter) {
		this.workerCounter = workerCounter;
	}
	
	@Transient
	@XmlAttribute
	public Double getRatingResult() {
//		DAORating daoRating = new DAORating();
//		return daoRating.calculateRatingResultForArticle(this);
		
		Double ratingResult = null;
		int countRatings = ratings.size();
		double sumOfValues = 0.0;
		
		List<Rating> ratings = getRatings();
		for(Rating rating: ratings) {
			sumOfValues += rating.getValue();
		}
		
		if(countRatings != 0) {
			ratingResult = sumOfValues/countRatings;
		}
		 
		
		return ratingResult;
	}
	
	@Transient
	@XmlAttribute
	/**
	 * Get progress of finished tasks.
	 * @return progress in percent
	 */
	public double getProgress() {
//		DAOArticle daoArticle = new DAOArticle();
//		Double percentage = daoArticle.calculateProgress(this);
		
		// available ratings + workercounter is sum of all tasks
		int allTasks = (ratings.size() + getWorkerCounter());
		double percentage = 1.0;
		if(allTasks != 0) {
			percentage = ratings.size() / (ratings.size() + getWorkerCounter());
		}
		return percentage;
	}

	
}
