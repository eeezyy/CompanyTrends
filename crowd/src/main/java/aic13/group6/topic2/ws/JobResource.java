package aic13.group6.topic2.ws;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.pojos.Answer;
import aic13.group6.topic2.workflow.Workflow;

@Path("job")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class JobResource {

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Job create(final Job job, @Context UriInfo uriInfo) {
		DAOJob daoJob = new DAOJob();
		
		Job savedJob = null;
		try {
			savedJob = daoJob.create(job);
			job.setId(savedJob.getId());
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		String baseUrl = uriInfo.getAbsolutePath().toString().replace("crowd/rest/job", "");
		
		new Workflow(job, baseUrl).start();
		
		return job;
	}
	
	@GET
	@Path("{id}")
	public Job get(@PathParam("id") final Long id) {
		Job job = new Job();
		job.setId(id);
		
		DAOJob daoJob = new DAOJob();
		try {
			job = daoJob.findByID(job);
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return job;
	}
	
	@POST
	@Path("callback")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Answer callback(final Answer answer) {
		
		return null;
	}
	
}
