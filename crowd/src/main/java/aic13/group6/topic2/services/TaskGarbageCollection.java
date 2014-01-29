package aic13.group6.topic2.services;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import aic13.group6.topic2.daos.DAOArticle;
import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.Task;

public class TaskGarbageCollection {

	/**
	 * Checks which tasks are overdue and request the deletion of it at the mock platform.
	 */
	public static void runGarbageCollection() {
		DAOJob daoJob = new DAOJob();
		List<Job> jobs = daoJob.getExpiredJobs();
		
		DAOArticle daoArticle = new DAOArticle();
		
		for(Job job: jobs) {
			List<Article> articles = job.getArticles();
			for(Article article: articles) {
				// only article with open tasks
				if(article.getWorkerCounter() == 0) continue;
				
				article.getUrl();
				Task task = new Task();
				task.setUrl(article.getUrl());
				TaskAPI.delete(Settings.getBaseUrl() + Settings.getMockBaseAPI() + Settings.getTaskResource(), task);
				// TODO check response
				
				article.setWorkerCounter(0);
				try {
					daoArticle.update(article);
				} catch (SQLException e) {
					throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
				}
			}
		}
		
		
	}
	
}
