package aic13.group6.topic2.entities;
import javax.persistence.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="article")
@Entity
public class Article {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	

	private String url;
	private long date;
	private String title;
	private String text;
	private Boolean usable;
	@ManyToMany(mappedBy = "articles", targetEntity=Tag.class)
	private List<Tag> tags;
	
	public Article () {
		tags = new LinkedList<Tag>();
	}
	
	@XmlElement
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@XmlElement
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
	@XmlElement
	public List<Tag> getTags() throws SQLException {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
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
