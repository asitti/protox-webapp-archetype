package me.protox.archetype.jersey.ext.json_param.wechat;

/**
 * Created by fengzh on 1/10/17.
 */
public class WechatVerificationException extends RuntimeException {

    public WechatVerificationException() {
    }

    public WechatVerificationException(String message) {
        super(message);
    }

    public WechatVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WechatVerificationException(Throwable cause) {
        super(cause);
    }

    public WechatVerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
