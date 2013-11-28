package aic13.group6.topic2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Task;
import aic13.group6.topic2.entities.Worker;

public class DAOTask implements DAO<Task> {

	@Override
	public Task create(Task obj) throws SQLException {
		
		if (obj.getArticle() == null) {
			throw new SQLException("A task cannot have no article.");
		}
		
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO tasks VALUES (NULL, ?, ?, ?, ?, NULL);");
		ps.setString(1, obj.getDescription());
		ps.setString(2, obj.getCallBackLink());
		ps.setFloat(3, obj.getPrice());
		ps.setString(4, obj.getArticle().getUrl());
		
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
		
		return obj;
	}

	public Task getRelations(Task obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tasks WHERE tid=?;");
		ps.setInt(1, obj.getTid());
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
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

}
