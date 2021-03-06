package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.State;
import aic13.group6.topic2.services.Settings;

public class DAOJob implements DAO<Job> {
	
	@Override
	public Job create(Job obj) throws SQLException {
		synchronized(DAO.SYNC) {
			obj.setDate((new Date()).getTime());
			obj.setState(State.CREATED);
			
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();
			
			em.close();
			emf.close();
		}
		
		return obj;
	}
	
	public Job update(Job obj) throws SQLException {
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	em.getTransaction().begin();
			em.merge(obj);
			em.getTransaction().commit();
			
			em.close();
			emf.close();
		}
		
		return obj;
	}

	@Override
	public Job findByID(Job obj) throws SQLException {
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
			obj = em.find(Job.class, obj.getId());
			
			em.close();
			emf.close();
		}
		
		return obj;
	}
	
	/**
	 * Job list for paginator
	 * @param offset
	 * @param max
	 * @return
	 */
	public List<Job> list(final int offset, final int max) {
		List<Job> list;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
			EntityManager em = emf.createEntityManager();
			
			CriteriaBuilder builder = emf.getCriteriaBuilder();
			CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
			
			Root<Job> taskRoot = criteria.from(Job.class);
			criteria.select(taskRoot);
			criteria.orderBy(builder.desc(taskRoot.get("date")));
			list = em.createQuery(criteria).setFirstResult(offset).setMaxResults(max).getResultList();
			
			em.close();
			emf.close();
		}
		
		return list;
	}

	public List<Job> list() {
		List<Job> list;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
			EntityManager em = emf.createEntityManager();
			
			CriteriaBuilder builder = emf.getCriteriaBuilder();
			CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
			
			Root<Job> taskRoot = criteria.from(Job.class);
			criteria.select(taskRoot);
			criteria.orderBy(builder.desc(taskRoot.get("date")));
			list = em.createQuery(criteria).getResultList();
			
			em.close();
			emf.close();
		}
		
		return list;
	}
	
	public Double calculateProgress(Job job) {
		List<Double> result;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
	    	EntityManager em = emf.createEntityManager();
	    	
	    	Query query = em.createNativeQuery("select count(r.id)*1.0/(sum(a.workercounter)+count(r.id)) from job j join job_article ja on j.id=ja.jobs_id join article a on a.url=ja.articles_url left join rating r on r.article_url=a.url where j.id=:id group by j.id").setParameter("id", job.getId());
	    	
	    	result = query.getResultList(); 
	    	
	    	em.close();
	    	emf.close();
		}
    	
    	// should be only zero or one result
    	Double value = null;
    	if(result.size() > 0) {
    		value = result.get(0);
    	}
    	return value;
	}

	public List<Job> getExpiredJobs() {
		Date now = new Date();
		Date dateBefore = new Date(now.getTime() - Settings.getTaskTimeoutInDays() * 24 * 3600 * 1000 );
		
		List<Job> list;
		
		synchronized(DAO.SYNC) {
			EntityManagerFactory emf =	Persistence.createEntityManagerFactory("aic");
			EntityManager em = emf.createEntityManager();
			
			CriteriaBuilder builder = emf.getCriteriaBuilder();
			CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
			
			Root<Job> taskRoot = criteria.from(Job.class);
			criteria.select(taskRoot);
			Path<Long> date = taskRoot.get("date");
			criteria.where(builder.lt(date, dateBefore.getTime()));
			criteria.orderBy(builder.desc(taskRoot.get("date")));
			list = em.createQuery(criteria).getResultList();
			
			em.close();
			emf.close();
		}
		return list;
	}

}

