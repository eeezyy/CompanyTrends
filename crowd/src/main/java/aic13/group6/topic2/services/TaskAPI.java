package aic13.group6.topic2.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import aic13.group6.topic2.pojos.Task;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TaskAPI {
	
	public static void delete(String url, Task task) {
		// TODO remove url argument, when settings page is done
//		String url = Settings.getBaseUrl() + Settings.getMockBaseAPI() + Settings.getTaskResource();
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		// rewrite delete because of error: "HTTP method DELETE doesn't support output"
//		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).delete(ClientResponse.class, task);
		String urlParam = "";
		try {
			urlParam = "?url=" + URLEncoder.encode(task.getUrl(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		url += urlParam;
		
		WebResource webResource = client.resource(url);
		
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		
	}
	
	public static Task create(String url, Task task) {
		// TODO remove url argument, when settings page is done
//		String url = Settings.getBaseUrl() + Settings.getMockBaseAPI() + Settings.getTaskResource();
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		WebResource webResource = client.resource(url);
		
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(ClientResponse.class, task);
		
		Task responseTask = response.getEntity(Task.class);
		
		return responseTask;
	}

}
