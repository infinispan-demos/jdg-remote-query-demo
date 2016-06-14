package org.jboss.infinispan.demo.rest;


import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
@Path("/remove")
public class RemoveAllTasks
{

   @Inject
   TaskService taskService;
	
   @POST
   @Consumes("application/json")
   public Response remove(Task task)
   {
		
			
			Collection<Task> allTasks=taskService.findAll();
			Iterator it=allTasks.iterator();
			
			while (it.hasNext()){
	    	Task tasktoRemove=(org.jboss.infinispan.demo.model.Task)it.next();
	    	taskService.delete(tasktoRemove);
			}
	    	
			return Response.noContent().build();				   
	   
   }


}