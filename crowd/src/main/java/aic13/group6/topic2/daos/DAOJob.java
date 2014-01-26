package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.entities.State;

public class DAOJob implements DAO<Job> {
	
	@Override
	public Job create(Job obj) throws SQLException {
		obj.setDate((new Date()).getTime());
		obj.setState(State.CREATED);
		
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	em.getTransaction().begin();
		em.persist(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}
	
	public Job update(Job obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
    	em.getTransaction().begin();
		em.merge(obj);
		em.getTransaction().commit();
		
		em.close();
		emf.close();
		
		return obj;
	}

	@Override
	public Job findByID(Job obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		obj = em.find(Job.class, obj.getId());
		
		em.close();
		emf.close();
		
		return obj;
	}

}

