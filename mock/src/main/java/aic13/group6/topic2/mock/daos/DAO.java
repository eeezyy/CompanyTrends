package aic13.group6.topic2.mock.daos;

import java.sql.SQLException;

public interface DAO<T> {
	
	public static final Integer SYNC = 0;
	/**
	 * Creates an entry in the database.
	 * @param obj An object with the fields set to the required values. If the ID is an integer, it is set automatically and the set ID will be ignored.
	 * @return The created object.
	 * @throws SQLException 
	 */
	public T create(T obj) throws SQLException;
	
	/**
	 * Returns the requested object.
	 * @param obj Object with its ID field set.
	 * @return The object with the specified ID.
	 * @throws SQLException 
	 */
	public T findByID(T obj) throws SQLException;


}
