package aic13.group6.topic2.mock.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import aic13.group6.topic2.mock.entities.Answer;

public class DAOAnswer implements DAO<Answer> {

	@Override
	public Answer create(Answer obj) throws SQLException {
		synchronized(DAO.SYNC) {
	    	EntityManager em = emf.createEntityManager();
	    	
			em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			
			em.close();
		}
		
		return obj;
	}

	@Override
	public Answer findByID(Answer obj) throws SQLException {
    	EntityManager em = emf.createEntityManager();
		
		obj = em.find(Answer.class, obj.getId());
		
		em.close();
		
		return obj;
	}

}
