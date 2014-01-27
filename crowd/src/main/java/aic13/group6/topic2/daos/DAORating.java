package aic13.group6.topic2.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import aic13.group6.topic2.entities.Rating;

public class DAORating implements DAO<Rating> {

	@Override
	public Rating create(Rating obj) throws SQLException {
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			
			em.close();
			emf.close();
		}
		
		return obj;
	}

	@Override
	public Rating findByID(Rating obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	Rating r = em.find(Rating.class, obj.getId());
    	
    	em.close();
    	emf.close();
    	
    	return r;
	}
	
}
