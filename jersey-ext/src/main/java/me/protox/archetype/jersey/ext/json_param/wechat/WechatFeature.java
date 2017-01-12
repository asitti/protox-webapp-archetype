package me.protox.archetype.jersey.ext.json_param.wechat;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by fengzh on 1/10/17.
 */
public class WechatFeature implements Feature {

    public static final Binder BINDER = new Binder();

    @Override
    public boolean configure(FeatureContext context) {
        if (!context.getConfiguration().isRegistered(BINDER)) {
            context.register(BINDER);
        }
        return true;
    }

    static class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bindFactory(WechatContextServiceFactory.class).to(WechatContextService.class).in(Singleton.class);
        }
    }
}
