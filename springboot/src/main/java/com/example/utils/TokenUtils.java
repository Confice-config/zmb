package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.enetity.User;
import com.example.exception.CustomerEeception;
import com.example.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;


@Component
public class TokenUtils {

    @Resource
    UserService userService;

    static UserService staticUserService;

    @PostConstruct
    public void init() {
        staticUserService = userService;
    }

//    生成token
    public static String createToken(String data,String sign) {
        return JWT.create().withAudience(data)
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1))
                .sign(Algorithm.HMAC256(sign));
    }

//    获取当前用户信息
    public static User getCurrentUser(){
        User user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)){
            token = request.getParameter("token");
        }
        String audience =  JWT.decode(token).getAudience().get(0);
        String[] split = audience.split("-");
        String userId = split[0];
        String roleId = split[1];

        if("1".equals(roleId)){
            return staticUserService.selectById(userId);
        }else if("2".equals(roleId)){
            return staticUserService.selectById(userId);
        }else if ("3".equals(roleId)){
            return staticUserService.selectById(userId);
        }
        return null;
    }
}
