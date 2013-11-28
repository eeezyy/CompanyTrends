package aic13.group6.topic2.ws;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import aic13.group6.topic2.entities.*;

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
        	EntityManagerFactory factory =   Persistence.createEntityManagerFactory("crowd");
        	EntityManager em = factory.createEntityManager();
        	TypedQuery<Tag> q = em.createQuery("select t from Tag t WHERE t.name = :name", Tag.class);
        	q.setParameter("name", name);
        	Tag t = q.getSingleResult();
        	em.close();
        	factory.close();
            return t;
        }  catch (NoResultException ex) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
	
	@Path("/answer/")
	@GET
	@Produces({"application/xml", "application/json"})
    public Response getCallback(@QueryParam("id") String id,
    		                @QueryParam("answer") String answer,
    		                @QueryParam("user") String user,
                            @Context final UriInfo uriInfo) {
		Tag t = null;
        try {
        	String[] answers = answer.split(",");
        	for (int i=0; i<answers.length; i++){
        		String[] tagValue = answers[i].split(":");
        		String tag = tagValue[0];
        		String value = tagValue[1];
        		EntityManagerFactory factory =   Persistence.createEntityManagerFactory("crowd");
        		EntityManager em = factory.createEntityManager();
        		try {
        			TypedQuery<Tag> q = em.createQuery("select t from Tag t WHERE t.name = :name", Tag.class);
                	q.setParameter("name", tag);
                	t = q.getSingleResult();
        		} catch (NoResultException ex) {
        			t = new Tag();
                }
        		t.setName(tag);
        		Rating r = new Rating();
        		r.setTag(t);
        		r.setValue(Double.parseDouble(value));
        		t.getRatings().add(r);
        		try {
        			EntityTransaction trans = em.getTransaction();
        			trans.begin();
        			em.merge(t);
        			trans.commit();
        		} catch (RollbackException ex) {
        			throw new WebApplicationException(Response.Status.CONFLICT);
                }
        	}
        	final ResponseBuilder response = Response.status(Status.OK);
    	    return response.build();
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
	

}
