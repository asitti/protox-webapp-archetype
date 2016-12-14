package me.protox.archetype.jersey.ext.config_property;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class ConfigPropertyFeature implements Feature {

    static final Logger LOGGER = LoggerFactory.getLogger(ConfigPropertyFeature.class);

    @Override
    public boolean configure(FeatureContext context) {
        Configuration configuration = context.getConfiguration();
        if (!configuration.isRegistered(ConfigPropertyResolver.class)) {
            LOGGER.debug("Register ConfigPropertyFeature");
            context.register(ConfigPropertyResolver.class);
            context.register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(ConfigPropertyResolver.class)
                            .to(new TypeLiteral<InjectionResolver<ConfigProperty>>() {})
                            .in(Singleton.class);
                }
            });
        }
        return true;
    }
}
