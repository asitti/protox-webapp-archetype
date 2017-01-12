package me.protox.archetype.jersey.ext.json_param.wechat;

import me.protox.archetype.jersey.ext.config_property.ConfigProperty;
import org.glassfish.hk2.api.Factory;

/**
 * Created by fengzh on 1/10/17.
 */
public class WechatContextServiceFactory implements Factory<WechatContextService> {

    @ConfigProperty(name = "wechat.token")
    String token;

    @ConfigProperty(name = "wechat.appsecret")
    String appSecret;

    @ConfigProperty(name = "wechat.appid")
    String appId;

    static WechatContextService wechatContextService = null;

    @Override
    public WechatContextService provide() {
        if (wechatContextService == null) {
            wechatContextService = new WechatContextService(appId, appSecret, token);
        }
        return wechatContextService;
    }

    @Override
    public void dispose(WechatContextService instance) {
        // do nothing
    }
}
