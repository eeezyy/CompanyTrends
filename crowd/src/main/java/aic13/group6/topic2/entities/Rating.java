package aic13.group6.topic2.entities;

import java.sql.SQLException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="rating")
@Entity
public class Rating {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long rid;
	private double value;
	@ManyToOne(targetEntity=Tag.class, optional=false)
	private Tag tag;
	
	public Rating() {

	}

	/**
	 * @return the rid
	 */
	@XmlElement
	public Long getRid() {
		return rid;
	}

	/**
	 * @param rid the rid to set
	 */
	public void setRid(long rid) {
		this.rid = rid;
	}

	/**
	 * @return the value
	 */
	@XmlElement
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
	 * @return the tag
	 * @throws SQLException 
	 */
	@XmlTransient
	public Tag getTag() throws SQLException {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
}