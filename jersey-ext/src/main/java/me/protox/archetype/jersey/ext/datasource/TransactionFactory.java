package me.protox.archetype.jersey.ext.datasource;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import java.sql.Connection;

import static me.protox.archetype.jersey.ext.datasource.ConnectionFactory.PROP_SHOULD_COMMIT;

/**
 * Created by fengzh on 1/12/17.
 */
public class TransactionFactory implements Factory<Transaction> {

    static final Logger LOGGER = LoggerFactory.getLogger(TransactionFactory.class);

    @Inject
    Connection connection;

    @Inject
    ContainerRequestContext containerRequestContext;

    @Override
    public Transaction provide() {
        return new Transaction() {
            @Override
            public void markRollback() {
                containerRequestContext.setProperty(PROP_SHOULD_COMMIT, false);
            }

            @Override
            public void markCommit() {
                containerRequestContext.setProperty(PROP_SHOULD_COMMIT, true);
            }
        };
    }

    @Override
    public void dispose(Transaction transaction) {
    }
}
