package com.zeffon.danzhu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.dto.TokenGetDTO;
import com.zeffon.danzhu.dto.WxaDTO;
import com.zeffon.danzhu.exception.http.ParameterException;
import com.zeffon.danzhu.model.Link;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.repository.UserRepository;
import com.zeffon.danzhu.util.CodeUtil;
import com.zeffon.danzhu.util.JwtToken;
import com.zeffon.danzhu.util.RestTemplateUtil;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * Create by Zeffon on 2020/10/1
 */
@Service
public class WxAuthenticationService {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LinkService linkService;

    @Value("${wx.code2session}")
    private String code2SessionUrl;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appsecret}")
    private String appsecret;
    @Value("${wx.accessToken}")
    private String accessTokenUrl;
    @Value("${wx.token2wxa}")
    private String wxaUrl;

    @Transactional
    public String code2Session(TokenGetDTO userData) {
        String url = MessageFormat.format(this.code2SessionUrl, this.appid, this.appsecret, userData.getAccount());
        RestTemplate rest = new RestTemplate();
        Map<String, Object> session = new HashMap<>();
        String sessionText = rest.getForObject(url, String.class);
        try {
            session = mapper.readValue(sessionText, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 容错（捕捉微信返回的错误进行处理）
        }
        return this.registerUser(session);
    }

    public String getWxaCode(WxaDTO wxaDTO) {
        String accessToken = getAccessToken();
        String url = MessageFormat.format(this.wxaUrl, accessToken);
        RestTemplate rest = RestTemplateUtil.getInstance();
        byte[] bytes = rest.postForObject(url, wxaDTO, byte[].class);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String getAccessToken() {
        String url = MessageFormat.format(this.accessTokenUrl, this.appid, this.appsecret);
        RestTemplate rest = new RestTemplate();
        Map<String, Object> session = new HashMap<>();
        String sessionText = rest.getForObject(url, String.class);
        try {
            session = mapper.readValue(sessionText, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 容错（捕捉微信返回的错误进行处理）
        }
        String accessToken = session.get("access_token").toString();
        return accessToken;
    }

    private synchronized String registerUser(Map<String, Object> session) {
        String openid = (String)session.get("openid");
        if (openid == null) {
            throw new ParameterException(20004);
        }
        Optional<User> userOptional = this.userRepository.findByOpenid(openid);
        if (userOptional.isPresent()) {
            return JwtToken.makeToken(userOptional.get().getId());
        }
        String code = CodeUtil.markUserCode();
        User user = User.builder()
                .openid(openid)
                .code(code)
                .build();

        userRepository.save(user);
        Integer uid = user.getId();
        return JwtToken.makeToken(uid);
    }


    public static InputStream doWXPost(String url, JSONObject jsonParam) {
        InputStream instreams = null;
        HttpPost httpRequst = new HttpPost(url);// 创建HttpPost对象
        try {
            StringEntity se = new StringEntity(jsonParam.toString(),"utf-8");
            se.setContentType("application/json");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"UTF-8"));
            httpRequst.setEntity(se);
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    instreams = httpEntity.getContent();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instreams;
    }

    public String code2Token(TokenGetDTO userData) {
        //1. 先将code和密码进行数据库验证
        String code = userData.getAccount();
        String password = userData.getPassword();
        Link link = linkService.getByCode(code);
        //2. 提取码检验
        if (StringUtils.isEmpty(password) || !password.equals(link.getPassword())) {
            throw new ParameterException(60003);
        }
        //3. 将link中的userId生成令牌返回
        Integer uid = link.getUserId();
        return JwtToken.makeToken(uid);
    }
}
