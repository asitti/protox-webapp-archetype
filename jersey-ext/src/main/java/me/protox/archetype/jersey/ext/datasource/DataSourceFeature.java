package me.protox.archetype.jersey.ext.datasource;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.sql.Connection;

/**
 * Created by fengzh on 12/23/16.
 */
public class DataSourceFeature implements Feature {
    static final Logger LOGGER = LoggerFactory.getLogger(DataSourceFeature.class);
    public static final Binder BINDER = new Binder();

    @Override
    public boolean configure(FeatureContext context) {
        if (!context.getConfiguration().isRegistered(BINDER)) {
            LOGGER.debug("Register DataSourceFeature");
            context.register(BINDER);
        }
        return true;
    }

    static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bindFactory(DataSourceFactory.class).to(DataSource.class).proxy(true).proxyForSameScope(true).in(Singleton.class);
            bindFactory(ConnectionFactory.class).to(Connection.class).proxy(true).proxyForSameScope(true).in(RequestScoped.class);
            bindFactory(TransactionFactory.class).to(Transaction.class).proxy(true).proxyForSameScope(true).in(RequestScoped.class);
        }
    }
}
