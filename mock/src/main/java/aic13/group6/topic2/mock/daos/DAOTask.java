package aic13.group6.topic2.mock.daos;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import aic13.group6.topic2.mock.entities.Task;

public class DAOTask implements DAO<Task> {

	@Override
	public Task create(Task obj) throws SQLException {
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
	public Task findByID(Task obj) throws SQLException {
    	EntityManager em = emf.createEntityManager();
		
		obj = em.find(Task.class, obj.getId());
		
		em.close();
		
		return obj;
	}
	
	public Task update(Task obj) throws SQLException {
		synchronized(DAO.SYNC) {
			EntityManager em = emf.createEntityManager();
			
			em.getTransaction().begin();
			obj = em.merge(obj);
			em.getTransaction().commit();
			
			em.close();
		}
		
		return obj;
	}
	
	public void delete(Task obj) throws SQLException {
		synchronized (DAO.SYNC) {
			EntityManager em = emf.createEntityManager();
			
			em.getTransaction().begin();
			// remove doesn't work see: https://stackoverflow.com/questions/14977031/jpa-entitymanager-not-removing-entities
//			em.remove(obj);
			
			em.createQuery("delete from Task t where t = :task").setParameter("task", obj).executeUpdate();
			em.getTransaction().commit();
			
			em.close();
		}
	}

	/**
	 * Open tasks list for paginator
	 * @param offset
	 * @param max
	 * @return
	 */
	public List<Task> listOpenTasks(final int offset, final int max) {
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
		
		Root<Task> taskRoot = criteria.from(Task.class);
		criteria.select(taskRoot);
		Path<Integer> counter = taskRoot.get("workerCounter");
		criteria.where(builder.gt(counter, 0));
		criteria.orderBy(builder.desc(taskRoot.get("date")));
		List<Task> list = em.createQuery(criteria).setFirstResult(offset).setMaxResults(max).getResultList();
		
		em.close();
		
		return list;
	}

	public List<Task> listOpenTasks() {
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
		
		Root<Task> taskRoot = criteria.from(Task.class);
		criteria.select(taskRoot);
		Path<Integer> counter = taskRoot.get("workerCounter");
		criteria.where(builder.gt(counter, 0));
		criteria.orderBy(builder.desc(taskRoot.get("date")));
		List<Task> list = em.createQuery(criteria).getResultList();
		
		em.close();
		
		return list;
	}
	
	public List<Task> listOpenTaskByUrl(Task task) {
		EntityManager em = emf.createEntityManager();

		CriteriaBuilder builder = emf.getCriteriaBuilder();
		CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
		
		Root<Task> taskRoot = criteria.from(Task.class);
		criteria.select(taskRoot);
		Path<String> url = taskRoot.get("url");
		Path<Integer> counter = taskRoot.get("workerCounter");
		criteria.where(builder.and(builder.equal(url, task.getUrl())), builder.gt(counter, 0));
		criteria.orderBy(builder.desc(taskRoot.get("date")));
		List<Task> list = em.createQuery(criteria).getResultList();
		
		em.close();
		
		return list;
	}

}
