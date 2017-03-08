package me.protox.archetype;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import me.protox.archetype.jersey.ext.config_property.ConfigPropertyFeature;
import me.protox.archetype.jersey.ext.config_property.ConfigPropertyResolver;
import me.protox.archetype.jersey.ext.jooq.JooqFeature;
import me.protox.archetype.jersey.ext.json_param.JsonParamFeature;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ServerConfig extends ResourceConfig {

    static final Logger LOGGER = LoggerFactory.getLogger(ServerConfig.class);

    private ServiceLocator serviceLocator = null;


    public ServerConfig() {
        packages(true, getClass().getPackage().getName());
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);

        register(FreemarkerMvcFeature.class);
        register(ConfigPropertyFeature.class);
        register(JsonParamFeature.class);
        register(JooqFeature.class);

        register(CustomBinder.class);

        // CORS support
        register((ContainerRequestFilter) requestContext -> {
            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            if (requestContext.getMethod().equals(HttpMethod.OPTIONS)) {
                requestContext.abortWith(Response.ok()
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, Joiner.on(", ").join(headers.get(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)))
                        .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, HEAD, OPTIONS")
                        .build());
            }
        });
        register((ContainerResponseFilter) (requestContext, responseContext) -> {
            responseContext.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        });


        register(new ContainerResponseFilter() {
            @Context
            ServletContext servletContext;

            @Override
            public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
                if (responseContext.getEntity() instanceof Viewable) {
                    final String contextPath = servletContext == null ?
                            "" :
                            getContextPath(servletContext.getServletContextName());
                    LOGGER.debug("{}", responseContext.getEntity());
                    Viewable viewable = (Viewable) responseContext.getEntity();
                    if (viewable.getModel() == null) {
                        responseContext.setEntity(new Viewable(viewable.getTemplateName(), new TreeMap<String, String>()));
                        viewable = (Viewable) responseContext.getEntity();
                    }
                    if (viewable.getModel() instanceof Map) {
                        Map<String, Object> model = (Map) viewable.getModel();
                        model.put("contextPath", contextPath);
                        responseContext.setEntity(viewable);
                    }
                } // end viewable branch
                // enable CORS support
                responseContext.getHeaders().add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            }
        });


        // Uncomment this block to enable Wechat support
//        register(new AbstractContainerLifecycleListener() {
//            @Inject
//            WechatContextService wechatContextService;
//
//            @ConfigProperty(name = "wechat.refreshTokenOnStartup")
//            boolean wechatRefreshOnStart;
//
//            @Override
//            public void onStartup(Container container) {
//                LOGGER.debug("onStartup {}", container);
//                if (wechatRefreshOnStart) {
//                    wechatContextService.startRefreshAccessTokenScheduler();
//                }
//            }
//
//            @Override
//            public void onShutdown(Container container) {
//                LOGGER.debug("onShutdown", container);
//                if (wechatRefreshOnStart) {
//                    wechatContextService.shutdownScheduler();
//                }
//            }
//        });


        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("version");
        beanConfig.setSchemes(new String[] {"http", "https"});
        beanConfig.setHost(ConfigPropertyResolver.config.getString("server.host", "127.0.0.1:8080"));
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage(getClass().getPackage().getName());
        beanConfig.setScan(true);


    }

    public static String getContextPath(String contextPath) {
        if (contextPath == null) {
            contextPath = "";
        }
        // contextPath must be EMPTY or in the format of "/xxxxx"
        // suffix by "/"
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }
        if (!contextPath.equals("") && !contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        return contextPath;
    }


}
