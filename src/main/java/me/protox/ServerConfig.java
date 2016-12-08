package me.protox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.protox.jersey.ext.config_property.ConfigPropertyFeature;
import me.protox.jersey.ext.jooq.JooqFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import javax.ws.rs.ext.ContextResolver;

public class ServerConfig extends ResourceConfig {

    public ServerConfig() {
        packages(true, getClass().getPackage().getName());
        register(FreemarkerMvcFeature.class);
        register(JacksonFeature.class);
        register(ConfigPropertyFeature.class);
        register(JooqFeature.class);

        register((ContextResolver<ObjectMapper>) type -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
            return objectMapper;
        });
    }

}
