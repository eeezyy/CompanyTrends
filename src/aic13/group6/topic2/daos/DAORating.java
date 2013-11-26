package aic13.group6.topic2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import aic13.group6.topic2.entities.Rating;
import aic13.group6.topic2.entities.Tag;
import aic13.group6.topic2.entities.Task;

public class DAORating implements DAO<Rating> {

	@Override
	public Rating create(Rating obj) throws SQLException {
		
		if (obj.getTask() == null) {
			throw new SQLException("A rating cannot have no assigned task.");
		}
		
		if (obj.getTag() == null) {
			throw new SQLException("A rating cannot have no assigned tag.");
		}
		
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO ratings VALUES (NULL, ?, ?, ?);");
		ps.setFloat(1, obj.getValue());
		ps.setInt(2, obj.getTask().getTid());
		ps.setString(3, obj.getTag().getName());
		ps.executeUpdate();
		
		obj.setRid(ps.getGeneratedKeys().getInt("rid"));
		
		ps.close();
		c.close();
		
		return obj;
	}

	@Override
	public Rating findByID(Rating obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM ratings WHERE rid=?;");
		ps.setInt(1, obj.getRid());
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
		obj.setValue(rs.getFloat("value"));
		
		rs.close();
		ps.close();
		c.close();
		
		return obj;
	}

	@Override
	public Rating getRelations(Rating obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM ratings WHERE rid=?;");
		ps.setInt(1, obj.getRid());
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
		DAO<Task> daoTask = new DAOTask();
		Task tsk = new Task();
		tsk.setTid(rs.getInt("tid"));
		tsk = daoTask.findByID(tsk);
		obj.setTask(tsk);
		
		DAO<Tag> daoTag = new DAOTag();
		Tag t = new Tag();
		t.setName(rs.getString("tag"));
		t = daoTag.findByID(t);
		obj.setTag(t);
		
		rs.close();
		ps.close();
		c.close();
		
		return obj;
	}

}
