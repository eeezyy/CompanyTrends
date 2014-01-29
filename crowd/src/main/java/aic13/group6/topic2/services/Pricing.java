package aic13.group6.topic2.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.Task;

import static java.util.concurrent.TimeUnit.*;

public class Pricing implements Runnable {

	@Override
	public void run() {
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		priceTask t = new priceTask();
		scheduler.scheduleAtFixedRate(t, 0, 300, SECONDS);
		
	}
	
	private class priceTask implements Runnable {

		@Override
		public void run() {
			DAOJob dj = new DAOJob();
			List<Job> lj = dj.list();
			
			for (Job j: lj) {
				long d = new java.util.Date().getTime() -  j.getDate();
				d = d / 1000; // seconds
				d = d / 60; // minutes
				
				if (d%5 == 0) { // every 5 minutes
//					Task t = j.getTask();
//					t.setPrice(Math.log(d / 5) * 4);
//					postToWebService(Settings.getMockBaseAPI() + Settings.getTaskResource() + "/" + j.getTask().getId(), t);
					
				}
			}
			
		}
		
		private Task postToWebService(String url, Task t) {
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			Client client = Client.create(clientConfig);
			
			WebResource webResource = client.resource(url);
			
			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).put(ClientResponse.class, t);
			
			Task responseTask = response.getEntity(Task.class);
			
			return responseTask;
		}
		
	}
	
	
	
	
	
	
	

}
