package aic13.group6.topic2.entities;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import aic13.group6.topic2.daos.DAO;
import aic13.group6.topic2.daos.DAOArticle;

@XmlRootElement(name="article")
@Entity
@Table(name = "articles")
public class Article implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 49266136511814730L;

	@Id
	private String url;
	private Calendar date;
	private String title;
	private String text;
	private Boolean usable;
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Tag> tags;
	
	public Article () {
		setUrl(null);
		setDate(null);
		setTitle(null);
		setText(null);
		setUsable(true);
		setTags(null);
	}

	/**
	 * @return the url
	 */
	@XmlElement
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
	@XmlElement
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
	@XmlElement
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
	 * @throws SQLException 
	 */
	public Set<Tag> getTags() throws SQLException {
		DAO<Article> daoArticle = new DAOArticle();
		daoArticle.getRelations(this);
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
	@XmlElement
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
