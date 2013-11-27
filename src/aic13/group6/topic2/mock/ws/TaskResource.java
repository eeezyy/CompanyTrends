package aic13.group6.topic2.mock.ws;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

import org.eclipse.persistence.exceptions.DatabaseException;

import aic13.group6.topic2.mock.entities.Task;

@Path("/task")
public class TaskResource {
	
	@GET
    @Produces({"application/xml", "application/json"})
    public Task get(@QueryParam("id") String sid,
                    @Context final UriInfo uriInfo) {
        try {
        	int id = Integer.parseInt(sid);
        	EntityManagerFactory factory =   Persistence.createEntityManagerFactory("mock");
        	EntityManager em = factory.createEntityManager();
        	TypedQuery<Task> q = em.createQuery("select t from Task t WHERE t.id = :id", Task.class);
        	q.setParameter("id", id);
        	Task t = q.getSingleResult();
        	em.close();
        	factory.close();
        	return t;
        } catch (DatabaseException ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        } catch (NoResultException ex) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
	
	@POST
    @Consumes({"application/xml", "application/json"})
	public Response post(final Task t,
            @Context final UriInfo uriInfo) {
		

		EntityManagerFactory factory =   Persistence.createEntityManagerFactory("mock");
		EntityManager em = factory.createEntityManager();
		try {
			EntityTransaction trans = em.getTransaction();
			trans.begin();
			em.persist(t);
			trans.commit();
		} catch (RollbackException ex) {
        	throw new WebApplicationException(Response.Status.CONFLICT);
        }
		em.close();
	    factory.close();
		final ResponseBuilder response = Response.status(Status.OK);
	    return response.build();
	}
	
	@PUT
    @Consumes({"application/xml", "application/json"})
	public Response put(final Task t,
            @Context final UriInfo uriInfo) {
		

		EntityManagerFactory factory =   Persistence.createEntityManagerFactory("mock");
		EntityManager em = factory.createEntityManager();
		try {
			TypedQuery<Task> q = em.createQuery("select t from Task t WHERE t.id = :id", Task.class);
	    	q.setParameter("id", t.getId());
	    	Task t1 = q.getSingleResult();
		} catch (NoResultException ex) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.merge(t);
		trans.commit();
		em.close();
	    factory.close();
	    if (!(t.getAnswer().equals(""))){
	    	if (!(t.getUser().equals(""))){
	    		if (!(t.getCallbackLink().equals(""))){
	    			String url = t.getCallbackLink() + "?id=" + t.getId() + "&answer=" + t.getAnswer() + "&user=" + t.getUser();
	    			sendToServer(url, "");
	    		}
	    	}
	    }
	    
	    
		final ResponseBuilder response = Response.status(Status.OK);
	    return response.build();
	}
	
	@DELETE
	public Response delete(@QueryParam("id") String sid,
                           @Context final UriInfo uriInfo) {
		
		int id = Integer.parseInt(sid);
		EntityManagerFactory factory =   Persistence.createEntityManagerFactory("mock");
		EntityManager em = factory.createEntityManager();
		Task t = null;
		try {
			TypedQuery<Task> q = em.createQuery("select t from Task t WHERE t.id = :id", Task.class);
	    	q.setParameter("id", id);
	    	t = q.getSingleResult();
		} catch (NoResultException ex) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
		EntityTransaction trans = em.getTransaction();
		trans.begin();
		em.remove(t);
		trans.commit();
		em.close();
	    factory.close();
		final ResponseBuilder response = Response.status(Status.OK);
	    return response.build();
	}
	
	private int sendToServer (String sUrl, String data) {
        BufferedReader URLinput;
        try {
            URL url = new URL(sUrl);
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.setUseCaches(false);
            httpcon.setRequestMethod("GET");
            httpcon.setDoInput (true);
            httpcon.setDoOutput (false);
            httpcon.setRequestProperty("Content-Type", "application/xml");
            httpcon.setRequestProperty("Accept-Encoding", "gzip,deflate");
            //DataOutputStream printout = new DataOutputStream (httpcon.getOutputStream ());
            //printout.writeBytes (data);
            //printout.flush ();
            //printout.close ();
            URLinput = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            if (httpcon.getResponseCode() != HttpURLConnection.HTTP_OK) {
               System.out.println(httpcon.getResponseMessage());
               return httpcon.getResponseCode();
            }
            return (httpcon.getResponseCode());
        } catch (Exception ex) {
          System.out.println("Could not connect to " + sUrl);
          ex.printStackTrace();
          return -1;
        }
    } 
	
	

}
