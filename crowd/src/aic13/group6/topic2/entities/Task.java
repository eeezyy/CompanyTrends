package aic13.group6.topic2.entities;

import java.sql.SQLException;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

@XmlRootElement(name="task")
@Entity
public class Task {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int tid;
	private String description;
	private String callBackLink;
	private float price;
	@ManyToOne(targetEntity=Article.class, optional=false)
	private Article article;
	@ManyToOne(targetEntity=Worker.class, optional=false)
	private Worker worker;
	
	public Task() {
		
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


}