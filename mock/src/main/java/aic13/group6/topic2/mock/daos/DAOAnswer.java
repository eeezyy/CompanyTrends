package aic13.group6.topic2.mock.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import aic13.group6.topic2.mock.entities.Answer;
import aic13.group6.topic2.mock.entities.Task;

public class DAOAnswer implements DAO<Answer> {

	@Override
	public Answer create(Answer obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("mock");
    	EntityManager em = emf.createEntityManager();
    	
		em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}

	@Override
	public Answer findByID(Answer obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("mock");
    	EntityManager em = emf.createEntityManager();
		
		obj = em.find(Answer.class, obj.getId());
		
		em.close();
		emf.close();
		
		return obj;
	}

}
