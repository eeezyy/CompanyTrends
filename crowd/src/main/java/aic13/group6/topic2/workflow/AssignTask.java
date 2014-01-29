package aic13.group6.topic2.workflow;

import java.util.ArrayList;
import java.util.List;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.AnswerOptions;
import aic13.group6.topic2.pojos.Task;
import aic13.group6.topic2.services.Settings;
import aic13.group6.topic2.services.TaskAPI;

public class AssignTask implements Runnable {
	
	private final Job job;
	private final Article article;
	// TODO replace with settings
	private final String baseUrl;
	
	
	private final static List<String> ANSWER_LIST = new ArrayList<String>();
	static {
		ANSWER_LIST.add(AnswerOptions.POSITIVE.text());
		ANSWER_LIST.add(AnswerOptions.NEUTRAL.text());
		ANSWER_LIST.add(AnswerOptions.NEGATIVE.text());
		ANSWER_LIST.add(AnswerOptions.IRRELEVANT.text());
	}
	
	public AssignTask(Job job, Article article, String baseUrl) {
		this.job = job;
		this.article = article;
		this.baseUrl = baseUrl;
	}

	@Override
	public void run() {
		Task task = new Task();
		task.setUrl(article.getUrl());
		task.setTitle(article.getTitle());
		task.setText(article.getText());
		task.setWorkerCounter(article.getWorkerCounter());
		task.setDescription(Settings.getDescription() + job.getName());
		task.setAnswerPossibilities(ANSWER_LIST);
		task.setPrice(1);
		task.setCallbackUrl(baseUrl + Settings.getCrowdBaseAPI() + Settings.getCallbackResource());

		Task responseTask = TaskAPI.create(baseUrl + Settings.getMockBaseAPI() + Settings.getTaskResource(), task);
		// TODO on error
		
	}
	
}
