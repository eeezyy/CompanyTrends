package aic13.group6.topic2.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import aic13.group6.topic2.entities.Tag;

public class DAOTagJPA implements DAO<Tag> {

	@PersistenceContext
	EntityManager em;

	@Override
	public Tag create(Tag obj) throws SQLException {
		em.persist(obj);
		return obj;
	}

	@Override
	public Tag findByID(Tag obj) throws SQLException {
		return em.find(Tag.class, obj.getName());
	}

}
