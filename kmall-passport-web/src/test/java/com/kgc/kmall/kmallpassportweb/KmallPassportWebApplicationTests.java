package com.kgc.kmall.kmallpassportweb;

import com.alibaba.fastjson.JSON;
import com.kgc.kmall.utils.HttpclientUtil;
import com.kgc.kmall.utils.JwtUtil;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class KmallPassportWebApplicationTests {

    @Reference
    JwtUtil jwtUtil;

    @Test
    void contextLoads() {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", "1");
        map.put("nickname", "zhangsan");
        String ip = "127.0.0.1";
        String encode = JwtUtil.encode("2020kmall075", map, ip);
        System.err.println(encode);
    }

    @Test
    void contextLoads02() {
        Map<String, Object> decode = jwtUtil.decode("eyJhbGciOiJIUzI1NiJ9.eyJuaWNrbmFtZSI6InpoYW5nc2FuIiwibWVtYmVySWQiOiIxIn0.eg_xHfMNMsiXCiAJAAA04sQXPwW8GfB7Q93w68mJkDc", "2020kmall075", "127.0.0.1");
        System.out.println(decode);
    }

    @Test
    void test03() {
        //根据授权码获取access_token
        String s3 = "https://api.weibo.com/oauth2/access_token";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", "4265947699");
        paramMap.put("client_secret", "610a50b59aca0a3a24529e0e2556d31e");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri", "http://passport.kmall.com:8086/vlogin");
        paramMap.put("code", "ef664fa19c33e0efa94302dfdd087556");// 授权有效期内可以使用，没新生成一次授权码，说明用户对第三方数据进行重启授权，之前的access_token和授权码全部过期
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);

        Map<String, String> access_map = JSON.parseObject(access_token_json, Map.class);


        System.out.println(access_map.get("access_token"));
        System.out.println(access_map.get("uid"));

        String access_token = access_map.get("access_token");
        String uid = access_map.get("uid");
        //根据access_token获取用户信息
        // 4 用access_token查询用户信息
        String s4 = "https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String user_json = HttpclientUtil.doGet(s4);
        Map<String,String> user_map = JSON.parseObject(user_json,Map.class);
        System.out.println(user_map);
    }


}
