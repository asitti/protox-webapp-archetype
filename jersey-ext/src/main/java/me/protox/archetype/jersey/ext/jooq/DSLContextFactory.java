package me.protox.archetype.jersey.ext.jooq;

import org.glassfish.hk2.api.Factory;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import java.sql.Connection;

/**
 * Created by fengzh on 12/8/16.
 */
public class DSLContextFactory implements Factory<DSLContext> {

    static final Logger LOGGER = LoggerFactory.getLogger(DSLContextFactory.class);

    @Inject
    Connection connection;

    @Context
    ContainerRequestContext requestContext;

    @Override
    public DSLContext provide() {
        DSLContext dslContext = DSL.using(connection);
        LOGGER.debug("provide DSLContext {}", dslContext);
        return dslContext;
    }

    @Override
    public void dispose(DSLContext dslContext) {
        dslContext.close();
    }
}
