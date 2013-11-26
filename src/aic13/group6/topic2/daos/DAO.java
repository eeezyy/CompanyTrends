package aic13.group6.topic2.daos;

import java.sql.SQLException;

public interface DAO<T> {
	
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
	
	/**
	 * Gets the relations for the object.
	 * @param obj An object with the ID field set.
	 * @return An object that has the relations set.
	 * @throws SQLException 
	 */
	public T getRelations(T obj) throws SQLException;

}
