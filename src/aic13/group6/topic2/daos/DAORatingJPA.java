package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Rating;
import aic13.group6.topic2.entities.Tag;

public class DAORatingJPA implements DAO<Rating> {
	
	@PersistenceContext
	EntityManager em;

	@Override
	public Rating create(Rating obj) throws SQLException {
		em.persist(obj);
		return obj;
	}

	@Override
	public Rating findByID(Rating obj) throws SQLException {
		return em.find(Rating.class, obj.getRid());
	}
	
	public List<Rating> findAllByTag(Tag t) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Rating> cq = cb.createQuery(Rating.class);
		Root<Rating> rating = cq.from(Rating.class);
		cq.where(cb.equal(rating.get("tag"), t.getName()));
		TypedQuery<Rating> q = em.createQuery(cq);
		
		return q.getResultList();
	}

}
