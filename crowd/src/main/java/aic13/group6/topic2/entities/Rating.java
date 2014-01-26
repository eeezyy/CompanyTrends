package aic13.group6.topic2.entities;

import java.sql.SQLException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="rating")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Rating {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private double value;
	@XmlTransient
	@ManyToOne(targetEntity=Article.class, optional=false)
	private Article article;

	/**
	 * @return the rid
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param rid the rid to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the article
	 * @throws SQLException 
	 */
	@XmlTransient
	public Article getArticle() throws SQLException {
		return article;
	}

	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}
	
}