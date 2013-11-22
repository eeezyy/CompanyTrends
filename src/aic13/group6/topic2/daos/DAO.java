package aic13.group6.topic2.daos;

import java.sql.SQLException;
import java.util.Set;

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
	 * Returns a set with fitting objects.
	 * @param obj An object which has the interesting fields set.
	 * @return A set of objects that fit the criteria.
	 */
	public Set<T> findAll(T obj);
	
	/**
	 * Updates the database entry for this object.
	 * @param obj An object with the ID field set. Completely overwrites this database entry.
	 */
	public void update(T obj);

}
