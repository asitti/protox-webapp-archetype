package me.protox.archetype.resource;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import me.protox.archetype.jersey.ext.json_param.wechat.WechatContextService;
import org.eclipse.persistence.oxm.mappings.XMLMapping;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Created by fengzh on 1/10/17.
 */
@Path("/wechat")
public class WechatResource {

    @Inject
    WechatContextService wechatContextService;

    @GET
    public String verify(@QueryParam("signature") String signature,
                         @QueryParam("timestamp") String timestamp,
                         @QueryParam("nonce") String nonce,
                         @QueryParam("echostr") String echostr) {
        return wechatContextService.verify(signature, timestamp, nonce, echostr);
    }
}
