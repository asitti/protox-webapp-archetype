package me.protox.archetype.jersey.ext.jooq;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.container.ContainerRequestContext;
import java.sql.Connection;
import java.sql.SQLException;

import static me.protox.archetype.jersey.ext.jooq.JooqFeature.PROP_SHOULD_COMMIT;
import static me.protox.archetype.jersey.ext.jooq.JooqFeature.PROP_SQL_CONNECTION;

public class ConnectionFactory implements Factory<Connection> {

    static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

    @Inject
    DataSource dataSource;

    @Inject
    ContainerRequestContext containerRequestContext;

    @Override
    public Connection provide() {
        LOGGER.debug("{}", this);
        try {
            Connection connection = dataSource.getConnection();
            containerRequestContext.setProperty(PROP_SQL_CONNECTION, connection);
            containerRequestContext.setProperty(PROP_SHOULD_COMMIT, true);
            LOGGER.debug("provide Connection {}", connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dispose(Connection connection) {
        boolean committed = false;
        try {
            if (connection.isClosed()) {
                return;
            }
            if (containerRequestContext.getProperty(PROP_SHOULD_COMMIT).equals(true)) {
                connection.commit();
                committed = true;
            } else {
                connection.rollback();
                committed = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (!connection.isClosed()) {
                    LOGGER.debug("{} {} and close", connection, committed ? "committed" : "rollback");
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
