package aic13.group6.topic2.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import aic13.group6.topic2.entities.Worker;

public class DAOWorkerJPA implements DAO<Worker> {

	@PersistenceContext
	EntityManager em;

	@Override
	public Worker create(Worker obj) throws SQLException {
		em.persist(obj);
		return obj;
	}

	@Override
	public Worker findByID(Worker obj) throws SQLException {
		return em.find(Worker.class, obj.getWid());
	}
}
