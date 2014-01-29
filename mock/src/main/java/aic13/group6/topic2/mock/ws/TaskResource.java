package aic13.group6.topic2.mock.ws;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
        	task = daoTask.findByID(task);
        } catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return task;
    }
	
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Task update(final Task t) {
		DAOTask daoTask = new DAOTask();
		
		Task ret;
		try {
			ret = daoTask.update(t);
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return ret;
	}
	
	@POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Task create(final Task task) {
		DAOTask daoTask = new DAOTask();
		
		task.setDate((new Date()).getTime());
		
		Task savedTask = null;
		
		try {
			savedTask = daoTask.create(task);
        } catch (SQLException e) {
        	throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return savedTask;
	}
	
	/**
	 * Open tasks list. Uses page and max parameters for paginator.
	 * @param page
	 * @param max
	 * @return open tasks list
	 */
	@GET
	@Path("open")
	public List<Task> openTasks(@QueryParam("page") int page, @QueryParam("max") int max) {
		DAOTask daoTask = new DAOTask();
		List<Task> openTasksList;
		
		if(max > 0) {
			int offset = 0;
			if(page < 1) {
				page= 1;
			}
			offset = (page-1)*max;
			openTasksList = daoTask.listOpenTasks(offset, max);
		} else {
			openTasksList = daoTask.listOpenTasks();
		}
		
		return openTasksList;
	}
	
	@POST
	@Path("submitTask")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Answer submitTask(final Answer answer) {
		DAOAnswer daoAnswer = new DAOAnswer();
		DAOTask daoTask = new DAOTask();
		
		Task loadTask = null;
		try {
			loadTask = daoTask.findByID(answer.getTask());
		} catch (SQLException e1) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		if(loadTask == null) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		
		Answer savedAnswer = null;

		answer.setTask(loadTask);
		
		Answer answerResponse = postToWebService(answer.getTask().getCallbackUrl(), answer);
		// TODO on error 
		
		loadTask.setWorkerCounter(loadTask.getWorkerCounter()-1);
		try {
			daoTask.update(loadTask);
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
