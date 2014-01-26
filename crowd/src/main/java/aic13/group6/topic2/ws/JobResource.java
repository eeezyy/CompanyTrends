package aic13.group6.topic2.ws;

import java.sql.SQLException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.entities.State;
import aic13.group6.topic2.workflow.Workflow;

@Path("job")
public class JobResource {

	@POST
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Job create(final Job job) {
		DAOJob daoJob = new DAOJob();
		
		Job savedJob = null;
		try {
			savedJob = daoJob.create(job);
			job.setId(savedJob.getId());
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		new Workflow(job).start();
		
		return job;
	}
	
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
}
