package aic13.group6.topic2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import aic13.group6.topic2.entities.Worker;
import aic13.group6.topic2.entities.Task;

public class DAOWorker implements DAO<Worker> {

	@Override
	public Worker create(Worker obj) throws SQLException {
		Worker w = new Worker();
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO workers VALUES(NULL, 0);");
		ps.executeUpdate();
		w.setWid(ps.getGeneratedKeys().getInt("wid"));
		
		ps.close();
		c.close();
		return w;
	}

	@Override
	public Worker findByID(Worker obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM workers WHERE wid=?;");
		ps.setInt(1, obj.getWid());
		ResultSet rs = ps.executeQuery();
		
		if(!rs.next()) {
			return null;
		}
		
		obj.setRating(rs.getFloat("rating"));
		
		ps = c.prepareStatement("SELECT * FROM tasks WHERE wid=?;");
		ps.setInt(1, obj.getWid());
		rs = ps.executeQuery();
		
		DAO<Task> daoTask = new DAOTask();
		Set<Task> tasks = new HashSet<Task>();
		while (rs.next()) {
			Task t = new Task();
			t.setTid(rs.getInt("tid"));
			t = daoTask.findByID(t);
			tasks.add(t);
		}
		
		if (tasks.size() == 0) {
			tasks = null;
		}
		obj.setTasks(tasks);
		
		return obj;
		
		
		
	}

	@Override
	public Set<Worker> findAll(Worker obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Worker obj) {
		// TODO Auto-generated method stub
		
	}

}
