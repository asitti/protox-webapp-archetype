package me.protox.jersey.ext.jooq;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.protox.jersey.ext.config_property.ConfigProperty;
import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by fengzh on 12/8/16.
 */
public class DataSourceFactory implements Factory<DataSource> {

    static final Logger LOGGER = LoggerFactory.getLogger(DataSourceFactory.class);

    @ConfigProperty(name = "ds.jdbcUrl")
    String jdbcUrl;
    @ConfigProperty(name = "ds.username")
    String username;
    @ConfigProperty(name = "ds.password")
    String password;
    @ConfigProperty(name = "ds.autoCommit", defaultValue = "false")
    boolean autoCommit;
    @ConfigProperty(name = "ds.maximumPoolSize")
    int maximumPoolSize;

    @Override
    public DataSource provide() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setAutoCommit(autoCommit);
        config.setMaximumPoolSize(maximumPoolSize);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource dataSource = new HikariDataSource(config);
        LOGGER.debug("provide DataSource {}", dataSource);

        return dataSource;
    }

    @Override
    public void dispose(DataSource dataSource) {
        if (!((HikariDataSource)dataSource).isClosed()) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
