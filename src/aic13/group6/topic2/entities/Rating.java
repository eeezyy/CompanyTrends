package aic13.group6.topic2.entities;

import java.sql.SQLException;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import aic13.group6.topic2.daos.DAO;
import aic13.group6.topic2.daos.DAORating;

@XmlRootElement(name="rating")
@Entity
@Table(name = "ratings")
public class Rating {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int rid;
	private float value;
	@ManyToOne(cascade=CascadeType.ALL)
	private Task task;
	@ManyToOne(cascade=CascadeType.ALL)
	private Tag tag;
	
	public Rating() {
		setRid(-1);
		setValue(0);
		setTask(null);
		setTag(null);
	}

	/**
	 * @return the rid
	 */
	@XmlElement
	public int getRid() {
		return rid;
	}

	/**
	 * @param rid the rid to set
	 */
	public void setRid(int rid) {
		this.rid = rid;
	}

	/**
	 * @return the value
	 */
	@XmlElement
	public float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * @return the task
	 * @throws SQLException 
	 */
	public Task getTask() throws SQLException {
		updateRelations();
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * @return the tag
	 * @throws SQLException 
	 */
	public Tag getTag() throws SQLException {
		updateRelations();
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	private void updateRelations() throws SQLException {
		DAO<Rating> daoRating = new DAORating();
		daoRating.getRelations(this);
	}
}