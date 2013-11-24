package aic13.group6.topic2.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Tag;

public class DAOArticle implements DAO<Article> {

	@Override
	public Article create(Article obj) throws SQLException {
		Connection c = DBConnectionManager.getInstance().getConnection();
		
		PreparedStatement ps = c.prepareStatement("INSERT INTO articles VALUES (?, ?, ?, ?, ?);");
		
		if (obj.getUrl() == null) {
			throw new SQLException("URL required! Entry would have no key otherwise.");
		} else {
			ps.setString(1, obj.getUrl());
		}
		
		if (obj.getDate() == null) {
			ps.setNull(2, Types.NUMERIC);
		} else {
			ps.setLong(2, obj.getDate().getTime().getTime());
		}
		
		ps.setString(3, obj.getTitle());
		ps.setString(4, obj.getText());
		ps.setBoolean(5, obj.getUsable());
		
		int s = ps.executeUpdate();
		if (s != 1) {
			throw new SQLException("Could not insert.");
		}
		
		ps.close();
		c.close();
		
		return obj;
	}

	@Override
	public Article findByID(Article obj) throws SQLException {
		if (obj.getUrl() == null) {
			throw new SQLException("No key specified!");
		}
		
		Connection c = DBConnectionManager.getInstance().getConnection();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM articles WHERE url=?;");
		ps.setString(1, obj.getUrl());
		
		ResultSet rs = ps.executeQuery();
		
		if (!rs.next()) {
			return null;
		}
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(rs.getLong("date"));
		obj.setDate(cal);
		
		obj.setTitle(rs.getString("title"));
		obj.setText(rs.getString("text"));
		obj.setUsable(rs.getBoolean("usable"));
		
		rs.close();
		ps.close();
		
		ps = c.prepareStatement("SELECT * FROM has_tags WHERE url=?;");
		rs = ps.executeQuery();
		
		Set<Tag> tags = new HashSet<Tag>();
		DAO<Tag> daoTag = new DAOTag();
		
		while(rs.next()) {
			Tag t = new Tag();
			t.setName(rs.getString("name"));
			t = daoTag.findByID(t);
			tags.add(t);
		}
		
		if (tags.size() == 0) {
			tags = null;
		}
		
		obj.setTags(tags);
		
		return obj;
	}

	@Override
	public Set<Article> findAll(Article obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Article obj) {
		// TODO Auto-generated method stub
		
	}

}
