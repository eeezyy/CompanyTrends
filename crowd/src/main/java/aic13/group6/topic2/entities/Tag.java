package aic13.group6.topic2.entities;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="tag")
@Entity
public class Tag {
	
	@Id
	private String name;
	@ManyToMany(targetEntity=Article.class)
	private List<Article> articles;
	@OneToMany(targetEntity=Rating.class, mappedBy = "tag")
	private List<Rating> ratings;
	
	public Tag() {
		articles = new LinkedList<Article>();
		ratings = new LinkedList<Rating>();
	}

	/**
	 * @return the name
	 */
	@XmlElement
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
	 * @throws SQLException 
	 */
	@XmlTransient
	public List<Article> getArticles() throws SQLException {
		return articles;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	/**
	 * @return the tasks
	 * @throws SQLException 
	 */
	@XmlElement
	public List<Rating> getRatings() throws SQLException {
		return ratings;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
	
	@Transient
	@XmlElement
	public double getAverage(){
		double sum = 0.0;
		for (Rating r : ratings){
			sum += r.getValue();
		}
		return sum / ratings.size();
	}


}
