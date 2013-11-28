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

import aic13.group6.topic2.entities.*;
import aic13.group6.topic2.daos.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;

@Path("/process")
public class ProcessResource {
	
	static Random generator = new Random();

	@GET
    @Produces({"application/xml", "application/json"})
    public Task get(@QueryParam("id") String sid,
    		       @Context final UriInfo uriInfo) {
		
		long id = Long.parseLong(sid);
		EntityManagerFactory factory =   Persistence.createEntityManagerFactory("crowd");
    	EntityManager em = factory.createEntityManager();
    	Article a = null;
		try {
			TypedQuery<Article> q = em.createQuery("select a from Article a WHERE a.id = :id", Article.class);
        	q.setParameter("id", id);
        	a = q.getSingleResult();
		} catch (NoResultException ex) {
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
		
        try {
        	Task t = new Task();
        	t.setArticle(a);
        	t.setCallBackLink("http://localhost:8080/crowd/rest/tag/answer");
        	t.setDescription(a.getTitle());
        	float price = (float)0.0;
        	t.setPrice(price);
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
        	String mockTask = "<task><id>" + 
    	                       t.getTid() +
    	                       "</id><user>peter</user><callbackLink>" +
    	                       "<answerPossibilities>1..5</answerPossibilities>" +
    	                       t.getCallBackLink() + "</callbackLink></task>";
        	sendToServer ("http://localhost:8080/mock/rest/task", mockTask);
            return t;
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
	
	private int sendToServer (String sUrl, String data) {
        BufferedReader URLinput;
        try {
            URL url = new URL(sUrl);
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            httpcon.setUseCaches(false);
            httpcon.setRequestMethod("POST");
            httpcon.setDoInput (true);
            httpcon.setDoOutput (true);
            httpcon.setRequestProperty("Content-Type", "application/xml");
            httpcon.setRequestProperty("Accept-Encoding", "gzip,deflate");
            DataOutputStream printout = new DataOutputStream (httpcon.getOutputStream ());
            printout.writeBytes (data);
            printout.flush ();
            printout.close ();
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
