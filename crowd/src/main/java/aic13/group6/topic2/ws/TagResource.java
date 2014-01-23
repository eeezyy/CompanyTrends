package aic13.group6.topic2.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import aic13.group6.topic2.entities.*;
import aic13.group6.topic2.daos.*;

import java.sql.SQLException;

@Path("/tag")
public class TagResource {
	
	private static final Logger logger =
			Logger.getLogger(TagResource.class.getName());
	
	@GET
    @Produces({"application/xml", "application/json"})
    public Tag get(@QueryParam("name") String name,
                   @Context final UriInfo uriInfo) {
        try {
            DAOTagJPA daoTag = new DAOTagJPA();
            Tag temp = new Tag();
            temp.setName(name);
            Tag t = daoTag.findByID(temp);
            return t;
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
	
	@Path("/answer/")
	@GET
	@Produces({"application/xml", "application/json"})
    public Task getCallback(@QueryParam("id") String id,
    		                @QueryParam("answer") String answer,
    		                @QueryParam("user") String user,
                            @Context final UriInfo uriInfo) {
        try {
        	String[] answers = answer.split(",");
        	for (int i=0; i<answers.length; i++){
        		String[] tagValue = answers[i].split(":");
        		String tag = tagValue[0];
        		String value = tagValue[1];
        		Tag t = new Tag();
        		DAOTagJPA dt = new DAOTagJPA();
        		t.setName(tag);
        		t = dt.findByID(t);
        		if (t == null) {
        			t = new Tag();
        			t.setName(tag);
//            		t.setTasks(new HashSet<Task>());
//            		t.setArticles(new HashSet<Article>());
        			t = dt.create(t);
        		}
        		Rating r = new Rating();
        		DAORatingJPA dr = new DAORatingJPA();
        		r.setTag(t);
//        		r.setTask(new Task()); //?
        		r.setValue(Float.parseFloat(value));
        		dr.create(r);
        	}
        	
            DAOTaskJPA daoTask = new DAOTaskJPA();
            Task t = new Task();
            t.setTid(Integer.parseInt(id));

            return t;
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
	

}
