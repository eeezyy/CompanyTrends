package aic13.group6.topic2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Task;
import aic13.group6.topic2.entities.Worker;

public class DAOTask implements DAO<Task> {

	@Override
	public Task create(Task obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO tasks VALUES (NULL, ?, NULL, ?, ?, NULL);");
		ps.setString(1, obj.getDescription());
		ps.setFloat(2, obj.getPrice());
		if (obj.getArticle() == null) {
			ps.setString(3, null);
		} else {
			ps.setString(3, obj.getArticle().getUrl());
		}
		
		ps.executeUpdate();
		obj.setTid(ps.getGeneratedKeys().getInt("tid"));
		
		ps.close();
		c.close();
		
		return obj;
	}

	@Override
	public Task findByID(Task obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tasks WHERE tid=?;");
		ps.setInt(1, obj.getTid());
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
		obj.setDescription(rs.getString("description"));
		obj.setCallBackLink(rs.getString("callbacklink"));
		obj.setPrice(rs.getFloat("tprice"));
		
		DAO<Article> daoArticle = new DAOArticle();
		Article a = new Article();
		a.setUrl(rs.getString("url"));
		a = daoArticle.findByID(a);
		obj.setArticle(a);
		
		DAO<Worker> daoWorker = new DAOWorker();
		Worker w = new Worker();
		w.setWid(rs.getInt("wid"));
		w = daoWorker.findByID(w);
		obj.setWorker(w);
		
		return obj;
	}

	@Override
	public Set<Task> findAll(Task obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Task obj) {
		// TODO Auto-generated method stub
		
	}

}
