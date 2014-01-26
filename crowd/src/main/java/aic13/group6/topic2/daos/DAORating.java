package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Rating;
import aic13.group6.topic2.entities.Tag;

public class DAORating implements DAO<Rating> {

	@Override
	public Rating create(Rating obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}

	@Override
	public Rating findByID(Rating obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	Rating r = em.find(Rating.class, obj.getRid());
    	
    	em.close();
    	emf.close();
    	
    	return r;
	}
	
	public List<Rating> findAllByTag(Tag t) {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Rating> cq = cb.createQuery(Rating.class);
		Root<Rating> rating = cq.from(Rating.class);
		cq.where(cb.equal(rating.get("tag"), t.getName()));
		TypedQuery<Rating> q = em.createQuery(cq);
		
		List<Rating> ret = q.getResultList();
		
		em.close();
		emf.close();
		
		return ret;
	}

}
