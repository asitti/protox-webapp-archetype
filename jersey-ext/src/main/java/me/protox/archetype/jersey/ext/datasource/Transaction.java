package me.protox.archetype.jersey.ext.datasource;

/**
 * Created by fengzh on 1/12/17.
 */
public interface Transaction {

    void markRollback();

    void markCommit();

}
