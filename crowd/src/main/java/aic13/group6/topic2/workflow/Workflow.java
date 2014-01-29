package aic13.group6.topic2.workflow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.scrapper.YFinanceSearchUrlScrapperYQL;

public class Workflow extends Thread {
	
	private final Job job;
	private final String baseUrl;
	private final int workerCount;
	
	public Workflow(Job job, String baseUrl, int workerCount) {
		this.job = job;
		this.baseUrl = baseUrl;
		this.workerCount = workerCount;
	}
	
	public void run() {
		DAOJob daoJob = new DAOJob();
		
		List<Thread> articleThreads = new ArrayList<Thread>();
		YFinanceSearchUrlScrapperYQL urlScrapper = new YFinanceSearchUrlScrapperYQL();
		List<String> urls = urlScrapper.getArticleUrlsForCompany(job.getName());
		
		
		for(String url:urls) {
			Thread thread = new Thread(new FetchPage(job, url, workerCount));
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
			job.setState(aic13.group6.topic2.pojos.State.PREPARED);
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
			Thread thread = new Thread(new AssignTask(job, article, baseUrl));
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
			job.setState(aic13.group6.topic2.pojos.State.ASSIGNED);
			try {
				daoJob.update(job);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
	}

	
}
