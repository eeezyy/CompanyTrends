package aic13.group6.topic2.entities;

import java.sql.SQLException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;

@XmlRootElement(name="worker")
@Entity
public class Worker {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long wid;
	private float rating; // 1 is good, -1 is bad
	
	public Worker() {

	}

	/**
	 * @return the wid
	 */
	@XmlElement
	public Long getWid() {
		return wid;
	}

	/**
	 * @param wid the wid to set
	 */
	public void setWid(long wid) {
		this.wid = wid;
	}

	/**
	 * @return the rating
	 */
	@XmlElement
	public float getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}


}
