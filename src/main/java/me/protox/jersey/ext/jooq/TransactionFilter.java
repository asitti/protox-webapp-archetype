package me.protox.jersey.ext.jooq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * Created by fengzh on 12/8/16.
 */
public class TransactionFilter implements ContainerResponseFilter {
    static final Logger LOGGER = LoggerFactory.getLogger(TransactionFilter.class);
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
//        LOGGER.info("TransactionFilter");
    }
}
