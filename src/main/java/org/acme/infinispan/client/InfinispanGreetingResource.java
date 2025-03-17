package org.acme.infinispan.client;

import java.util.concurrent.CompletionStage;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.logging.Logger; // Import Logger

import io.quarkus.infinispan.client.Remote;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/greeting")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfinispanGreetingResource {

    private static final Logger LOG = Logger.getLogger(InfinispanGreetingResource.class); // Define logger

    @Inject
    @Remote("mycache") 
    RemoteCache<String, Greeting> cache; 

    // @POST
@Path("/{id}")
public CompletionStage<String> postGreeting(@PathParam("id") String id, Greeting greeting) {
    return cache.putAsync(id, greeting) 
          .thenApply(g -> "Greeting stored successfully!")
          .exceptionally(ex -> ex.getMessage());
}


    // @GET
    // @Path("/{id}")
    // public CompletionStage<Greeting> getGreeting(@PathParam("id") String id) {
    //     return cache.getAsync(id)
    //         .exceptionally(throwable -> {
    //             LOG.error("Error fetching from Infinispan", throwable);
    //             return new Greeting("Error", "Fallback"); // Provide a default second argument
    //         });
    // }

    @GET
@Path("/{id}")
@Blocking
public CompletionStage<Greeting> getGreeting(@PathParam("id") String id) {
    return cache.getAsync(id)
        .exceptionally(throwable -> {
            LOG.error("Error fetching greeting from cache", throwable);
            return null;  // Handle errors gracefully
        });
}

}
