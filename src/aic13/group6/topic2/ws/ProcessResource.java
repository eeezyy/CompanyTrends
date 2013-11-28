package aic13.group6.topic2.ws;

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
    public Task get(@QueryParam("url") String url,
    		       @Context final UriInfo uriInfo) {
        try {
        	DAOArticle da = new DAOArticle();
        	Article a = new Article();
        	a.setUrl(url);
        	a = da.findByID(a);
        	DAOTask dt = new DAOTask();
        	Task t = new Task();
        	t.setArticle(a);
        	t.setCallBackLink("abc");
        	t.setDescription("123");
        	float price = (float)0.0;
        	t.setPrice(price);
        	t.setTags(new HashSet<Tag>());
        	t.setTid(-1);
        	t = dt.create(t);
        	int r = generator.nextInt();
        	String mockTask = "<task><id>" + r +"</id><user>peter</user><callbackLink>http://localhost:8080/crowd/rest/tag/answer</callbackLink></task>";
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
