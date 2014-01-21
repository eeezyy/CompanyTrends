package aic13.group6.topic2.workflow;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;

public class AssignTask implements Runnable {
	
	private final Job job;
	private final Article article;
	
	public AssignTask(Job job, Article article) {
		this.job = job;
		this.article = article;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	

}
