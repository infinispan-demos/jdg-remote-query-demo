package org.jboss.infinispan.demo.rest;

import java.util.Collection;
import java.util.Date;

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
@Path("/loadtasks")
public class LoadTasksEndpoint
{

   @Inject
   TaskService taskService;
	
   @POST
   @Consumes("application/json")
   public Response loadTasks(int quantity)
   {
       Task task; 	
	   // Create quantity tasks
			for (int i = 0; i < quantity; i++) {
				task = new Task();
				task.setTitle("This is the " + i + " test task");
				task.setCreatedOn(new Date());
				taskService.insert(task);
			}
			
			long startTime = System.currentTimeMillis();
		
			return Response.noContent().build();				   
	   
   }


}