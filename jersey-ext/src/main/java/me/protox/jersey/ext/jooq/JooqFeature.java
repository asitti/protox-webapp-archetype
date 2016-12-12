package me.protox.jersey.ext.jooq;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.sql.Connection;

/**
 * Created by fengzh on 12/8/16.
 */
public class JooqFeature implements Feature {

    public static final String PROP_SQL_CONNECTION = "java.sql.Connection";
    public static final String PROP_SHOULD_COMMIT = "java.sql.Connection.shouldCommit";

    static final Logger LOGGER = LoggerFactory.getLogger(JooqFeature.class);

    private static final Binder BINDER = new Binder();

    @Override
    public boolean configure(FeatureContext context) {
        if (!context.getConfiguration().isRegistered(BINDER)) {
            LOGGER.debug("Register JooqFeature");
            context.register(BINDER);
        }
        return true;
    }

    static class Binder extends AbstractBinder {
        Binder() {
            LOGGER.info("111");
        }

        @Override
        protected void configure() {
            bindFactory(DSLContextFactory.class).to(DSLContext.class).proxy(true).proxyForSameScope(true).in(RequestScoped.class);
            bindFactory(ConnectionFactory.class).to(Connection.class).proxy(true).proxyForSameScope(true).in(RequestScoped.class);
            bindFactory(DataSourceFactory.class).to(DataSource.class).proxy(true).proxyForSameScope(true).in(Singleton.class);
        }
    }
}
