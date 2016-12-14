package me.protox.archetype.jersey.ext.json_param;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.ContextResolver;

/**
 * Created by fengzh on 12/14/16.
 */
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperProvider.class);

    static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();

    static {
        DEFAULT_OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public ObjectMapperProvider() {
        LOGGER.debug("ObjectMapperProvider constructed");
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return DEFAULT_OBJECT_MAPPER;
    }
}
