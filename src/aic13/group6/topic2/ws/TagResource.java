package aic13.group6.topic2.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
            DAOTag daoTag = new DAOTag();
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
            DAOTask daoTask = new DAOTask();
            Task t = new Task();
            t.setTid(Integer.parseInt(id));

            return t;
        } catch (Exception ex) {
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
	

}
