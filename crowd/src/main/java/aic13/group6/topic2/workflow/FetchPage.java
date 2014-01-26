package aic13.group6.topic2.workflow;

import java.sql.SQLException;

import aic13.group6.topic2.daos.DAOArticle;
import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.scrapper.YFinancePageScrapperJava;

public class FetchPage implements Runnable {
	
	private final Job job;
	private final String url;
	
	public FetchPage(Job job, String url) {
		this.job = job;
		this.url = url;
	}

	@Override
	public void run() {
		YFinancePageScrapperJava pageScrapper = new YFinancePageScrapperJava();
		Article article = new Article();
		article.setUrl(url);
		DAOArticle daoArticle = new DAOArticle();
		DAOJob daoJob = new DAOJob();
		Article tempArticle;
		try {
			if((tempArticle = daoArticle.findByID(article)) == null) {
				article = pageScrapper.getPage(url);
				tempArticle = daoArticle.create(article);
			}
			synchronized(job) {
				job.getArticles().add(tempArticle);
				daoJob.update(job);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getUrl() {
		return url;
	}
	
}
