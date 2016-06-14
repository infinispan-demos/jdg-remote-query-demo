package org.jboss.infinispan.demo.marshallers;

import org.infinispan.protostream.MessageMarshaller;
import org.jboss.infinispan.demo.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


	public class TaskMarshaller implements MessageMarshaller<Task> {

		   @Override
		   public String getTypeName() {
		      return "Task";
		   }

		   @Override
		   public Class<Task> getJavaClass() {
		      return Task.class;
		   }	
		   @Override
		   public Task readFrom(ProtoStreamReader reader) throws IOException {
		      String title = reader.readString("title");
		      long id = reader.readLong("id");
		      
		      //ToDO :fix these datatypes in protobuf
		      Date completed = reader.readDate("completedOn");
		      Date created = reader.readDate("createdOn");
		      Boolean done = reader.readBoolean("done");

		      
		      Task task = new Task();
		      task.setTitle(title);
		      task.setId(id);
		      
		      task.setCreatedOn(created);
		      task.setCompletedOn(completed);
		      task.setDone(done);
		      
		      return task;
		   }

		   @Override
		   public void writeTo(ProtoStreamWriter writer, Task task) throws IOException {
		      writer.writeString("title", task.getTitle());
		      writer.writeLong("id", task.getId());
		      
		      writer.writeDate("completedOn", task.getCompletedOn());
		      writer.writeDate("createdOn", task.getCreatedOn());
		      writer.writeBoolean("done", task.isDone());
		   }
}
