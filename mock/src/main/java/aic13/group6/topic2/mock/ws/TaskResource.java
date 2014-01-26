package aic13.group6.topic2.mock.ws;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import aic13.group6.topic2.mock.daos.DAOAnswer;
import aic13.group6.topic2.mock.daos.DAOTask;
import aic13.group6.topic2.mock.entities.Answer;
import aic13.group6.topic2.mock.entities.Task;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

@Path("task")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class TaskResource {
	
	@GET
	@Path("{id}")
    public Task get(@PathParam("id") final long id) {
		DAOTask daoTask = new DAOTask();
    	
    	Task task = new Task();
    	task.setId(id);
    	
		try {
        	daoTask.findByID(task);
        } catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return task;
    }
	
	@POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Task post(final Task task) {
		DAOTask daoTask = new DAOTask();
		
		Task savedTask = null;
		
		try {
			savedTask = daoTask.create(task);
        } catch (SQLException e) {
        	throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return savedTask;
	}
	
	@GET
	@Path("open")
	public List<Task> openTasks() {
		DAOTask daoTask = new DAOTask();
		
		List<Task> openTasksList = daoTask.getOpenTasks();
		
		return openTasksList;
	}
	
	@POST
	@Path("submitTask")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Answer submitTask(final Answer answer) {
		DAOTask daoTask = new DAOTask();
		DAOAnswer daoAnswer = new DAOAnswer();
		
		Answer loadAnswer = null;
		try {
			loadAnswer = daoAnswer.findByID(answer);
		} catch (SQLException e1) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		if(loadAnswer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		Answer savedAnswer = null;

		Answer answerResponse = postToWebService(loadAnswer.getTask().getCallbackUrl(), null);
		
		try {
			savedAnswer = daoAnswer.create(answer);
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return savedAnswer;
	}
	
	private Answer postToWebService(String callbackUrl, Answer answer) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		
		WebResource webResource = client.resource(callbackUrl);
		
		Answer answerResponse = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(Answer.class, answer);
		return answerResponse;
	}
	
}
