package aic13.group6.topic2.ws;

import java.sql.SQLException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import aic13.group6.topic2.daos.DAORating;

@Path("quality")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class QualityResource {

	@GET
	@Path("tendency")
	public Map<Long, Double> calculateTendency() {
		DAORating daoRating = new DAORating();
		
		Map<Long, Double> map = null;
		try {
			map = daoRating.calculateTendency();
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return map;
	}
	
	@GET
	@Path("distance")
	public Map<Long, Double> calculateDistance() {
		DAORating daoRating = new DAORating();
		
		Map<Long, Double> map = null;
		try {
			map = daoRating.calculateDistance();
		} catch (SQLException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return map;
	}
}
