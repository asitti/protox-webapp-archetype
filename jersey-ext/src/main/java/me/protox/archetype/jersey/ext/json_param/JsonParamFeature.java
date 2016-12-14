package me.protox.archetype.jersey.ext.json_param;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by fengzh on 12/14/16.
 */
public class JsonParamFeature implements Feature {

    private static final Binder BINDER = new Binder();

    @Override
    public boolean configure(FeatureContext context) {
        if (!context.getConfiguration().isRegistered(JacksonFeature.class)) {
            context.register(JacksonFeature.class);
        }
        if (!context.getConfiguration().isRegistered(ObjectMapperProvider.class)) {
            context.register(ObjectMapperProvider.class);
        }
        if (!context.getConfiguration().isRegistered(BINDER)) {
            context.register(BINDER);
            return true;
        }
        return false;
    }

    static final class Binder extends AbstractBinder {
        @Override
        protected void configure() {

            bind(JsonParamValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(JsonParamValueFactoryProvider.InjectionResolver.class).to(new TypeLiteral<InjectionResolver<JsonParam>>() {
            }).in(Singleton.class);

        }
    }
}
