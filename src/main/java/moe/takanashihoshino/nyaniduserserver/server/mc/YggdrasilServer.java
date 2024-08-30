package moe.takanashihoshino.nyaniduserserver.server.mc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moe.takanashihoshino.nyaniduserserver.ErrUtils.ErrRes;
import moe.takanashihoshino.nyaniduserserver.server.mc.YggdrasilServerJson.YggdrasilServerJsonLinks;
import moe.takanashihoshino.nyaniduserserver.server.mc.YggdrasilServerJson.YggdrasilServerJsonMeta;
import moe.takanashihoshino.nyaniduserserver.server.mc.YggdrasilServerJson.YggdrasilServerJsonRoot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/yggdrasil")
public class YggdrasilServer {

    @Value("${yggdrasil.serverName}")
    private String serverName;

    @Value("${yggdrasil.implementationName}")
    private String implementationName;

    @Value("${yggdrasil.implementationVersion}")
    private String implementationVersion;

    @Value("${yggdrasil.signaturePublickey}")
    private String signaturePublickey;

    @Value("${yggdrasil.links-homepage}")
    private String links_homepage;

    @Value("${yggdrasil.links-register}")
    private String links_register;

    @Value("${yggdrasil.feature-non_email_login}")
    private boolean feature_non_email_login;

    @GetMapping
    public Object GetMethod(HttpServletResponse response, HttpServletRequest request){
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String fullURL = requestURL + (queryString != null ? "?" + queryString : "");
        response.setHeader("X-Authlib-Injector-API-Location", fullURL);
        //
        YggdrasilServerJsonLinks links = new YggdrasilServerJsonLinks();
        links.setHomepage(links_homepage);
        links.setRegister(links_register);
        //
        YggdrasilServerJsonMeta meta = new YggdrasilServerJsonMeta();
        meta.setImplementationName(implementationName);
        meta.setImplementationVersion(implementationVersion);
        meta.setServerName(serverName);
        meta.setLinks(links);
        meta.setFeature_non_email_login(feature_non_email_login);
        //
        YggdrasilServerJsonRoot root = new YggdrasilServerJsonRoot();
        root.setMeta(meta);
        root.setSkinDomains(new String[]{"nyanid.cn", ".nyanid.cn"});
        root.setSignaturePublickey(signaturePublickey);
        return root;
    }

    @PostMapping
    public Object PostMethod(HttpServletResponse response){
        return ErrRes.MethodNotAllowedException("杂鱼你的请求方式不对喵~",response);
    }
}
