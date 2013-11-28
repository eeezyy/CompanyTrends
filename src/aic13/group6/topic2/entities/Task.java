package aic13.group6.topic2.entities;

import java.sql.SQLException;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="task")
@Entity
@Table(name = "tasks")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int tid;
	private String description;
	private String callBackLink;
	private float price;
	@ManyToOne(cascade=CascadeType.ALL)
	private Article article;
	@ManyToOne(cascade=CascadeType.ALL)
	private Worker worker;
	@ManyToMany(cascade=CascadeType.ALL)
	private Set<Tag> tags;
	
	public Task() {
		setTid(-1);
		setDescription(null);
		setCallBackLink(null);
		setPrice(-1);
		setArticle(null);
		setWorker(null);
		setTags(null);
	}

	/**
	 * @return the tid
	 */
	@XmlElement
	public int getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(int tid) {
		this.tid = tid;
	}

	/**
	 * @return the description
	 */
	@XmlElement
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the callBackLink
	 */
	@XmlElement
	public String getCallBackLink() {
		return callBackLink;
	}

	/**
	 * @param callBackLink the callBackLink to set
	 */
	public void setCallBackLink(String callBackLink) {
		this.callBackLink = callBackLink;
	}

	/**
	 * @return the price
	 */
	@XmlElement
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the article
	 * @throws SQLException 
	 */
	public Article getArticle() throws SQLException {
		return article;
	}

	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}

	/**
	 * @return the worker
	 * @throws SQLException 
	 */
	@XmlElement
	public Worker getWorker() throws SQLException {
		return worker;
	}

	/**
	 * @param worker the worker to set
	 */
	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	/**
	 * @return the tags
	 * @throws SQLException 
	 */
	public Set<Tag> getTags() throws SQLException {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
}