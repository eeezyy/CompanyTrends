package aic13.group6.topic2.entities;

import java.sql.SQLException;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import aic13.group6.topic2.daos.DAO;
import aic13.group6.topic2.daos.DAOTag;

@XmlRootElement(name="tag")
@Entity
@Table(name = "tags")
public class Tag {
	
	@Id
	private String name;
	@ManyToMany(cascade=CascadeType.ALL, mappedBy = "tags")
	private Set<Article> articles;
	@ManyToMany(cascade=CascadeType.ALL, mappedBy = "tags")
	private Set<Task> tasks;
	
	public Tag() {
		setName(null);
		setArticles(null);
		setTasks(null);
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
	@XmlElement
	public Set<Article> getArticles() throws SQLException {
		updateRelations();
		return articles;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	/**
	 * @return the tasks
	 * @throws SQLException 
	 */
	@XmlElement
	public Set<Task> getTasks() throws SQLException {
		updateRelations();
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}
	
	private void updateRelations() throws SQLException {
		DAO<Tag> daoTag = new DAOTag();
		daoTag.getRelations(this);		
	}

}
