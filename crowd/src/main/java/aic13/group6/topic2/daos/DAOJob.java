package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.State;

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
	
	/**
	 * Job list for paginator
	 * @param offset
	 * @param max
	 * @return
	 */
	public List<Job> list(final int offset, final int max) {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
		
		Root<Job> taskRoot = criteria.from(Job.class);
		criteria.select(taskRoot);
		criteria.orderBy(builder.desc(taskRoot.get("date")));
		List<Job> list = em.createQuery(criteria).setFirstResult(offset).setMaxResults(max).getResultList();
		
		em.close();
		emf.close();
		
		return list;
	}

	public List<Job> list() {
		EntityManagerFactory emf =   Persistence.createEntityManagerFactory("aic");
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
		
		Root<Job> taskRoot = criteria.from(Job.class);
		criteria.select(taskRoot);
		criteria.orderBy(builder.desc(taskRoot.get("date")));
		List<Job> list = em.createQuery(criteria).getResultList();
		
		em.close();
		emf.close();
		
		return list;
	}

}

