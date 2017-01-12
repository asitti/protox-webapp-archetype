package me.protox.archetype.jersey.ext.json_param;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

/**
 * Created by fengzh on 12/14/16.
 */
public class JsonParamFeatureTest extends JerseyTest {
    static final Logger LOGGER = LoggerFactory.getLogger(JsonParamFeatureTest.class);
    @Path("hello")
    public static class HelloResource {

        @JsonParam("name") String name;

        @JsonParam("int_val") int intVal;

        @POST
        @Produces(MediaType.TEXT_PLAIN)
        public String getHello(@JsonParam("id") String id) {
            LOGGER.debug("{} {} {}", id, name, intVal);
            return String.format("%s:%s:%d", id, name, intVal);
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(HelloResource.class)
                .register(JsonParamFeature.class);
    }

    @Test
    public void test() {
        String respText = target("hello").request().post(Entity.json("{\"id\": \"id\", \"name\": \"name\", \"int_val\": 1}")).readEntity(String.class);
        assertEquals("id:name:1", respText);
    }

}