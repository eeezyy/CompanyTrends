package aic13.group6.topic2.workflow;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.Task;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class AssignTask implements Runnable {
	
	private final Job job;
	private final Article article;
	private final String baseUrl;
	
	private final static String CALLBACK_RESOURCE = "callback";
	private final static String MOCK_BASE_API = "mock/rest/";
	private final static String TASK_RESOURCE = "task";
	private final static int WORKER_COUNTER = 1;
	private final static String DESCRIPTION = "Please read the following article and rate the following topic with the options below: ";
	private final static List<String> ANSWER_LIST = new ArrayList<String>();
	static {
		ANSWER_LIST.add("positiv");
		ANSWER_LIST.add("neutral");
		ANSWER_LIST.add("negativ");
		ANSWER_LIST.add("irrelevant");
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
		task.setWorkerCounter(WORKER_COUNTER);
		task.setDescription(DESCRIPTION + job.getName());
		task.setAnswerPossibilities(ANSWER_LIST);
		task.setPrice(1);
		task.setCallbackUrl(baseUrl + CALLBACK_RESOURCE + job.getId());

		Task responseTask = postToWebService(baseUrl + MOCK_BASE_API + TASK_RESOURCE, task);
		// TODO on error
		
	}
	
	private Task postToWebService(String url, Task task) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		WebResource webResource = client.resource(url);
		
		Task answerResponse = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(Task.class, task);
		return answerResponse;
	}
	
	

}
