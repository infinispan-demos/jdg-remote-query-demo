package org.jboss.infinispan.demo.rest;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.jboss.infinispan.demo.TaskService;
import org.jboss.infinispan.demo.model.Task;

/**
 * 
 */
@Stateless
@Path("/search")
public class SearchEndpoint
{
	
   Logger log = Logger.getLogger(this.getClass().getName());	
   
   @Inject
   RemoteCache<Long, Task> cache;
	
   @GET
   @Consumes("application/json")
   public Response search()
   {
	   
	   System.out.println("Searching");

      return Response.noContent().build();
   }
   
   @GET
   @Path("/{filter}/{keyword}")
   @Produces("application/json") 
   public Collection<Task> listAll(@PathParam("filter") Boolean filter,@PathParam("keyword") String keyword)   
   {
	   QueryFactory qf = Search.getQueryFactory(cache);
	   long startTime = System.currentTimeMillis();
	   Query query = qf.from(Task.class)
	           .having("title")
               //.eq(keyword)
               .like("%" + keyword + "%")
                 .toBuilder()
	                 .build();
	      List<Task> results = query.list();	   
	    long stopTime = System.currentTimeMillis();	    
	    log.info("#### Executing last query took " + (stopTime-startTime) + " ms"); 
	  return results; 
   }
}