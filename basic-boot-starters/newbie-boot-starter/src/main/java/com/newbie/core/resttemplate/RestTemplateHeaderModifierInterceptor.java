package com.newbie.core.resttemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newbie.context.CurrentUserContext;
import com.newbie.context.UserInfoManager;
import com.newbie.core.util.net.NetUtil;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Base64;

public class RestTemplateHeaderModifierInterceptor
  implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        CurrentUserContext userContext = UserInfoManager.getInstance().getUserInfo();
        headers.add("X-BMSAH", userContext.getBmsah());
        headers.add("x-forwarded-for", NetUtil.getLocalHost());
        headers.add("X-IDENTITY", StringUtils.newStringUtf8(Base64.getEncoder().encodeToString(new ObjectMapper().writeValueAsBytes(userContext)).getBytes()));

        ObjectMapper mapper = new ObjectMapper();
        CurrentUserContext userContext1 = mapper.readValue(new ObjectMapper().writeValueAsBytes(userContext), CurrentUserContext.class);
        System.out.println(userContext1);
        return execution.execute(request, body);
    }
}