package me.protox.archetype.jersey.ext.jooq;

import me.protox.archetype.jersey.ext.datasource.DataSourceFeature;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by fengzh on 12/8/16.
 */
public class JooqFeature implements Feature {

    static final Logger LOGGER = LoggerFactory.getLogger(JooqFeature.class);

    private static final Binder BINDER = new Binder();

    @Override
    public boolean configure(FeatureContext context) {
        if (!context.getConfiguration().isRegistered(BINDER)) {
            LOGGER.debug("Register JooqFeature");
            context.register(BINDER);
            if (!context.getConfiguration().isRegistered(DataSourceFeature.class)) {
                LOGGER.debug("Register DataSourceFeature by JooqFeature");
                context.register(DataSourceFeature.class);
            }
        }
        return true;
    }

    static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bindFactory(DSLContextFactory.class).to(DSLContext.class).proxy(true).proxyForSameScope(true).in(RequestScoped.class);
        }
    }
}
