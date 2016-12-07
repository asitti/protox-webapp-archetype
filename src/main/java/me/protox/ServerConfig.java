package me.protox;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

public class ServerConfig extends ResourceConfig {

    public ServerConfig() {
        packages(true, "me.protox");
        register(FreemarkerMvcFeature.class);
        register(JacksonFeature.class);
    }

}
