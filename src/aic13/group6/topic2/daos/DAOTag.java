package aic13.group6.topic2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Tag;
import aic13.group6.topic2.entities.Task;

public class DAOTag implements DAO<Tag>{

	@Override
	public Tag create(Tag obj) throws SQLException {
		
		if (obj.getName() == null) {
			throw new SQLException("Object has no key.");
		}
		
		if (obj.getArticles() == null || obj.getArticles().size() != 1) {
			throw new SQLException("Tag has no or more than one article - cannot create table entry.");
		}
		
		if (obj.getTasks() == null || obj.getTasks().size() != 1) {
			throw new SQLException("Tag has no task or more than one - cannot create table entry.");
		}
		
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("INSERT INTO tags VALUES (?);");
		
		ps.setString(1, obj.getName());
		
		ps.executeUpdate();
		
		ps = c.prepareStatement("INSERT INTO has_tags VALUES (?, ?);");
		
		Article a = null;
		for (Article e : obj.getArticles()) {
			a = e;
		}
		
		ps.setString(1, a.getUrl());
		ps.setString(2, obj.getName());
		
		ps.executeUpdate();
		
		ps = c.prepareStatement("INSERT INTO belong_to VALUES (?, ?);");
		
		
		Task t = null;
		for (Task e: obj.getTasks()) {
			t = e;
		}
		
		ps.setInt(1, t.getTid());
		ps.setString(2, obj.getName());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
		
		return obj;
	}

	@Override
	public Tag findByID(Tag obj) throws SQLException {
		
		if (obj.getName() == null) {
			throw new SQLException("No key specified!");
		}
		
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM tags WHERE name=?;");
		ps.setString(1, obj.getName());
		
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
		// actually this doesn't make any sense since a tag has only one attribute.
		
		return obj;
	}

	@Override
	public Set<Tag> findAll(Tag obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Tag obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Tag getRelations(Tag obj) throws SQLException {
		
		if (obj.getName() == null) {
			throw new SQLException("Tag has no key!");
		}
		
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM has_tags WHERE tag=?;");
		ps.setString(1, obj.getName());
		ResultSet rs = ps.executeQuery();
		
		Set<Article> articles = new HashSet<Article>();
		DAO<Article> daoArticle = new DAOArticle();
		while(rs.next()) {
			Article a = new Article();
			a.setUrl(rs.getString("url"));
			a = daoArticle.findByID(a);
			articles.add(a);
		}
		
		if (articles.size() == 0) {
			articles = null;
		}
		obj.setArticles(articles);
		
		ps = c.prepareStatement("SELECT * FROM belong_to WHERE tag=?;");
		ps.setString(1, obj.getName());
		rs = ps.executeQuery();
		
		Set<Task> tasks = new HashSet<Task>();
		DAO<Task> daoTask = new DAOTask();
		while (rs.next()) {
			Task t = new Task();
			t.setTid(rs.getInt("tid"));
			t = daoTask.findByID(t);
			tasks.add(t);
		}
		obj.setTasks(tasks);
		
		rs.close();
		ps.close();
		c.close();
		
		return obj;
	}

}
