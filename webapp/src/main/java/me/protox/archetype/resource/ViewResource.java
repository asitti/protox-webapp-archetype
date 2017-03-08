package me.protox.archetype.resource;

import me.protox.archetype.jersey.ext.config_property.ConfigProperty;
import me.protox.archetype.jersey.ext.datasource.Transaction;
import org.glassfish.jersey.server.mvc.Viewable;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeMap;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class ViewResource {

    static final Logger LOGGER = LoggerFactory.getLogger(ViewResource.class);

    @ConfigProperty(name = "project.name")
    String name;

    @Inject
    DataSource dataSource;

    @Inject
    Connection connection;

    @Inject
    DSLContext dslContext;

    @Inject
    Transaction transaction;

    @Context
    ServletContext servletContext;

    @GET
    public Viewable index() throws SQLException {
        transaction.markRollback();
        LOGGER.info("{}", transaction);
        ResultSet rs = connection.createStatement().executeQuery("select 1;");
        while (rs.next()) {
            System.out.println(rs.getInt(1));
        }
        return new Viewable("/index", new TreeMap<String, Object>() {{
            put("name", name);
        }});
    }

}
