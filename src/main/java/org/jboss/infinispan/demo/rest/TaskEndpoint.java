package org.jboss.infinispan.demo.rest;

import java.util.Collection;
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

import org.jboss.infinispan.demo.TaskService;
import org.jboss.infinispan.demo.model.Task;

/**
 * 
 */
@Stateless
@Path("/tasks")
public class TaskEndpoint
{
	Logger log = Logger.getLogger(this.getClass().getName());
	
   @Inject
   TaskService taskService;
	
   @POST
   @Consumes("application/json")
   public Response create(Task task)
   {
      taskService.insert(task);
      System.out.println("Creada Task id="+task.getId());
      return Response.created(UriBuilder.fromResource(TaskEndpoint.class).path(String.valueOf(task.getId())).build()).build();
   }

      
   @GET   
   @Produces("application/json") 
   public Collection<Task> listAll()   
   {      
	    long startTime = System.currentTimeMillis();
	    Collection<Task> ct=taskService.findAll();
	    long stopTime = System.currentTimeMillis();
	    log.info("#### Executeing all reads took " + (stopTime-startTime) + " ms");
	   return ct; 
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes("application/json")
   public Response update(@PathParam("id") Long id,Task task)
   {
	  taskService.update(task);
      return Response.noContent().build();
   }
}