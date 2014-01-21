package aic13.group6.topic2.workflow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aic13.group6.topic2.daos.DAOJobJPA;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.scrapper.YFinanceSearchUrlScrapperYQL;

public class Workflow extends Thread {
	
	private final Job job;
	
	public Workflow(Job job) {
		this.job = job;
	}
	
	public void run() {
		DAOJobJPA daoJob = new DAOJobJPA();
		
		List<Thread> articleThreads = new ArrayList<Thread>();
		YFinanceSearchUrlScrapperYQL urlScrapper = new YFinanceSearchUrlScrapperYQL();
		List<String> urls = urlScrapper.getArticleUrlsForCompany(job.getName());
		
		
		for(String url:urls) {
			Thread thread = new Thread(new FetchPage(job, url));
			thread.start();
			articleThreads.add(thread);
		}
		
		for(Thread thread:articleThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		synchronized(job) {
			job.setState(aic13.group6.topic2.entities.State.PREPARED);
			try {
				daoJob.update(job);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		List<Thread> assignThreads = new ArrayList<Thread>();
		
		List<Article> articles = job.getArticles();
		for(Article article:articles) {
			Thread thread = new Thread(new AssignTask(job, article));
			thread.start();
			assignThreads.add(thread);
		}
		
		for(Thread thread:assignThreads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		synchronized(job) {
			job.setState(aic13.group6.topic2.entities.State.ASSIGNED);
			try {
				daoJob.update(job);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}

	
}
