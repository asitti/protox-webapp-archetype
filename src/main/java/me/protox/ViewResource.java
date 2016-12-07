package me.protox;

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

    @GET
    public Viewable projectWorkSpaceView() {
        return new Viewable("/index", new TreeMap<String, Object>() {{
            put("who", "world");
        }});
    }

}
