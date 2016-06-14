package org.jboss.infinispan.demo;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import javax.enterprise.inject.Produces;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.jboss.infinispan.demo.marshallers.TaskMarshaller;
import org.jboss.infinispan.demo.model.Task;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.infinispan.client.hotrod.impl.transport.tcp.FailoverRequestBalancingStrategy;
import org.infinispan.client.hotrod.configuration.NearCacheMode;


/**
 * This class produces configured cache objects via CDI
 *  
 * @author tqvarnst
 * @author sgutierr
 *
 */
public class Config {

    private static final String PROTOBUF_DEFINITION_RESOURCE = "/task.proto";

	private RemoteCacheManager cacheManager; 
	
	/**
	 * DONE: Add a default Producer for org.infinispan.client.hotrod.RemoteCache<Long, Task> 
	 * 		  using org.infinispan.client.hotrod.configuration.ConfigurationBuilder
	 * 		  and org.infinispan.client.hotrod.RemoteCacheManager
	 * 
	 * @return org.infinispan.client.hotrod.RemoteCache<Long, Task>
	 */
	@Produces
	public RemoteCache<Long, Task> getRemoteCache() throws Exception {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.addServers("localhost:11222")
		       .marshaller(new ProtoStreamMarshaller());  // The Protobuf based marshaller is required for query capabilities
		cacheManager= new RemoteCacheManager(builder.build(), true);
		registerSchemasAndMarshallers();
		
		return cacheManager.getCache("demoSergio");
		
	}
	
	  /**
	    * Register the Protobuf schemas and marshallers with the client and then register the schemas with the server too.
	    */
	   private void registerSchemasAndMarshallers() throws IOException {
	      // Register entity marshallers on the client side ProtoStreamMarshaller instance associated with the remote cache manager.
	      SerializationContext ctx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
	      ctx.registerProtoFiles(FileDescriptorSource.fromResources(PROTOBUF_DEFINITION_RESOURCE));
	      ctx.registerMarshaller(new TaskMarshaller());

          
	      // register the schemas with the server too
	      RemoteCache<String, String> metadataCache = cacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
	   
	      String errors = metadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);
	      
	      if (errors != null) {
	         throw new IllegalStateException("Some Protobuf schema files contain errors:\n" + errors);
	      }
	       
	   }
	  

}
