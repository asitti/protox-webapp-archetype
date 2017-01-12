package me.protox.archetype;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import me.protox.archetype.jersey.ext.config_property.ConfigPropertyFeature;
import me.protox.archetype.jersey.ext.jooq.JooqFeature;
import me.protox.archetype.jersey.ext.json_param.JsonParamFeature;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

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
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage(getClass().getPackage().getName());
        beanConfig.setScan(true);


    }

}
