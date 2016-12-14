package me.protox.archetype;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Created by fengzh on 12/9/16.
 */
public class CustomBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(DefaultPasswordService.class).to(PasswordService.class).in(Singleton.class);
    }
}
