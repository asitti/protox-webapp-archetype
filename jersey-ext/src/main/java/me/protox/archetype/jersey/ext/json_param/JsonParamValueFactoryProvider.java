package me.protox.archetype.jersey.ext.json_param;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Providers;
import java.io.IOException;

/**
 * Created by fengzh on 12/14/16.
 */
@Singleton
public class JsonParamValueFactoryProvider extends AbstractValueFactoryProvider {

    static final Logger LOGGER = LoggerFactory.getLogger(JsonParamValueFactoryProvider.class);

    @Singleton
    static final class InjectionResolver extends ParamInjectionResolver<JsonParam> {
        static final Logger LOGGER = LoggerFactory.getLogger(InjectionResolver.class);

        @Inject
        public InjectionResolver() {
            super(JsonParamValueFactoryProvider.class);
            LOGGER.debug("JsonParamValueFactoryProvider.InjectionResolver constructed");
        }
    }

    private static final class JsonParamValueFactory extends AbstractContainerRequestValueFactory<Object> {
        static final Logger LOGGER = LoggerFactory.getLogger(JsonParamValueFactory.class);

        private final Parameter parameter;
        public static final String PROP_REQUEST_BODY = "request.body";
        public static final String PROP_REQUEST_BODY_JDON_NODE = "request.body.jsonNode";

        public static final int BODY_LIMIT_IN_BYTES = 10 * 1024 * 1024;

        public JsonParamValueFactory(Parameter parameter) {
            this.parameter = parameter;
            LOGGER.debug("JsonParamValueFactoryProvider.JsonParamValueFactory constructed");
        }

        @Context
        private Providers providers;

        @Override
        public Object provide() {
            String bodyText;
            JsonNode fieldNode;
            if (getContainerRequest().getProperty(PROP_REQUEST_BODY) == null) {
                try {
                    bodyText = IOUtils.toString(getContainerRequest().getEntityStream());
                    ObjectMapper objectMapper = providers.getContextResolver(ObjectMapper.class, MediaType.APPLICATION_JSON_TYPE).getContext(getClass());
                    fieldNode = objectMapper.readTree(bodyText);
                    LOGGER.debug("read body from EntityStream and save");
                    getContainerRequest().setProperty(PROP_REQUEST_BODY, bodyText);
                    getContainerRequest().setProperty(PROP_REQUEST_BODY_JDON_NODE, fieldNode);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.debug("load body from request properties");
                fieldNode = (JsonNode) getContainerRequest().getProperty(PROP_REQUEST_BODY_JDON_NODE);
            }

            fieldNode = fieldNode.get(parameter.getSourceName());

            if (fieldNode == null) {
                throw new RuntimeException(String.format("json field %s not found", parameter.getSourceName()));
            }

            if (parameter.getRawType().equals(String.class)) {
                return fieldNode.asText(parameter.getDefaultValue());
            } else if (parameter.getType().equals(Integer.TYPE)) {
                return fieldNode.asInt(parameter.hasDefaultValue() ? Integer.parseInt(parameter.getDefaultValue()) : 0);
            } else if (parameter.getType().equals(Long.TYPE)) {
                return fieldNode.asLong(parameter.hasDefaultValue() ? Long.parseLong(parameter.getDefaultValue()) : 0L);
            } else if (parameter.getType().equals(Boolean.TYPE)) {
                return fieldNode.asBoolean(parameter.hasDefaultValue() ? Boolean.valueOf(parameter.getDefaultValue()) : false);
            }

            throw new RuntimeException(String.format("not supported @JsonParam value type %s", parameter.getRawType()));
        }
    }

    @Inject
    protected JsonParamValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator locator) {
        super(mpep, locator, Parameter.Source.UNKNOWN);
        LOGGER.info("JsonParamValueFactoryProvider constructed");
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {

        LOGGER.debug("{}", parameter);
//        parameter.getSourceName()
        JsonParamValueFactory factory = new JsonParamValueFactory(parameter);
        getLocator().inject(factory);
        return factory;
    }
}
