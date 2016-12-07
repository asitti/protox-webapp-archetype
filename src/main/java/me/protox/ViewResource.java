package me.protox;

import me.protox.jersey.ext.config_property.ConfigProperty;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.TreeMap;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class ViewResource {

    static final Logger LOGGER = LoggerFactory.getLogger(ViewResource.class);


    @ConfigProperty(name = "project.name")
    String name;

    @GET
    public Viewable projectWorkSpaceView() {
        LOGGER.info("{}", "projectWorkSpaceView");
        return new Viewable("/index", new TreeMap<String, Object>() {{
            put("name", name);
        }});
    }

}
