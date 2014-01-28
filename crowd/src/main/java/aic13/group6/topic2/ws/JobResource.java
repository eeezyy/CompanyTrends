package aic13.group6.topic2.ws;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import aic13.group6.topic2.daos.DAOArticle;
import aic13.group6.topic2.daos.DAOJob;
import aic13.group6.topic2.daos.DAORating;
import aic13.group6.topic2.entities.Article;
import aic13.group6.topic2.entities.Job;
import aic13.group6.topic2.entities.Rating;
import aic13.group6.topic2.pojos.Answer;
import aic13.group6.topic2.pojos.AnswerOptions;
import aic13.group6.topic2.services.Settings;
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
		
		new Workflow(job, baseUrl, Settings.getWorkerPerTask()).start();
		
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
	
	@GET
	@Path("list")
	public List<Job> getList(@QueryParam("page") int page, @QueryParam("max") int max) {
		DAOJob daoJob = new DAOJob();
		List<Job> list;
		
		if(max > 0) {
			int offset = 0;
			if(page < 1) {
				page= 1;
			}
			offset = (page-1)*max;
			list = daoJob.list(offset, max);
		} else {
			list = daoJob.list();
		}
		
		return list;
	}
	
	@POST
	@Path("callback")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Answer callback(final Answer answer) {
		String answerString = answer.getAnswer();
		double ratingValue = 0.0;
		if(answerString.equals(AnswerOptions.POSITIVE.text())) {
			ratingValue = AnswerOptions.POSITIVE.value();
		} else if (answerString.equals(AnswerOptions.NEUTRAL.text())) {
			ratingValue = AnswerOptions.NEUTRAL.value();
		} else if (answerString.equals(AnswerOptions.NEGATIVE.text())) {
			ratingValue = AnswerOptions.NEGATIVE.value();
		} else if (answerString.equals(AnswerOptions.IRRELEVANT.text())) {
			ratingValue = AnswerOptions.IRRELEVANT.value();
		}
		
		DAOArticle daoArticle = new DAOArticle();
		Article article = new Article();
		article.setUrl(answer.getTask().getUrl());
		
		try {
			article = daoArticle.findByID(article);
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		if(article == null) {
			throw new WebApplicationException(Response.Status.CONFLICT);
		}
		
		article.setWorkerCounter(article.getWorkerCounter() - 1);
		
		try {
			daoArticle.update(article);
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		Rating rating = new Rating();
		rating.setValue(ratingValue);
		rating.setArticle(article);
		rating.setUserId(answer.getUserId());
		
		DAORating daoRating = new DAORating();
		try {
			daoRating.create(rating);
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return answer;
	}
	
}
