package me.protox.archetype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.protox.archetype.jersey.ext.config_property.ConfigPropertyFeature;
import me.protox.archetype.jersey.ext.jooq.JooqFeature;
import me.protox.archetype.jersey.ext.json_param.JsonParamFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;

import javax.ws.rs.ext.ContextResolver;

public class ServerConfig extends ResourceConfig {

    public ServerConfig() {
        packages(true, getClass().getPackage().getName());
        register(FreemarkerMvcFeature.class);
        register(ConfigPropertyFeature.class);
        register(JooqFeature.class);
        register(JsonParamFeature.class);

        register(CustomBinder.class);

    }

}
