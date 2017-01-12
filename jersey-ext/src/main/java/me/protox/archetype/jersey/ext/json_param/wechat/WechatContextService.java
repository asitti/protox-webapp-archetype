package me.protox.archetype.jersey.ext.json_param.wechat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fengzh on 1/10/17.
 */
public class WechatContextService {

    private int PERIOD_IN_MINUTES = 120;
    static final Logger LOGGER = LoggerFactory.getLogger(WechatContextService.class);

    private final String appId;

    private final String appSecret;

    private final String token;

    public static final String accessTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$appId&secret=$appSecret";

    ScheduledExecutorService scheduledExecutorService;

    String accessToken;

    public WechatContextService(String appId, String appSecret, String token) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.token = token;
    }


    public String verify(String signature, String timestamp, String nonce, String echoStr) {

        LOGGER.debug("token     {}", token);
        LOGGER.debug("timestamp {}", timestamp);
        LOGGER.debug("nonce     {}", nonce);
        LOGGER.debug("echostr   {}", echoStr);

        List<String> strList = Lists.newArrayList(token, timestamp, nonce);
        strList.sort(String::compareTo);

        String sha1Hash = new Sha1Hash(Joiner.on("").join(strList)).toString();
        if (sha1Hash.equalsIgnoreCase(signature.toUpperCase())) {
            return echoStr;
        } else {
            throw new WechatVerificationException();
        }
    }

    public void startRefreshAccessTokenScheduler() {

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            LOGGER.info("accessToken refreshing ");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(accessTokenApi).get().build();
            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                accessToken = objectMapper.readTree(jsonString).get("access_token").asText();
                LOGGER.info("accessToken update to {}", accessToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, PERIOD_IN_MINUTES, TimeUnit.MINUTES);
    }

    public void shutdownScheduler() {
        try {
            scheduledExecutorService.shutdown();
            scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
