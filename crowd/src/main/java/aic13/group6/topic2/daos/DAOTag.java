package aic13.group6.topic2.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import aic13.group6.topic2.entities.Tag;

public class DAOTag implements DAO<Tag> {

	@Override
	public Tag create(Tag obj) throws SQLException {
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
	public Tag findByID(Tag obj) throws SQLException {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
    	EntityManager em = emf.createEntityManager();
    	
		Tag ret = em.find(Tag.class, obj.getName());
		
		em.close();
		emf.close();
		
		return ret;
	}

}
