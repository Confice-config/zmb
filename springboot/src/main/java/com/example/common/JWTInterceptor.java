package com.example.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.enetity.User;
import com.example.exception.CustomerEeception;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.auth0.jwt.exceptions.JWTDecodeException;



@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //       拿到token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            token = request.getParameter("token");

        }
       if (StrUtil.isBlank(token)) {
           throw new CustomerEeception("401", "Token缺失");
       }
        User user = null;
       try {
           String audience =  JWT.decode(token).getAudience().get(0);
           String[] split = audience.split("-");
           if (split.length < 2) {  // 修改点2：避免数组越界
               throw new CustomerEeception("401", "Token格式错误");
           }
           String userId = split[0];
           String roleId = split[1];
           if("1".equals(roleId)){
               user = userService.selectById(userId);
           }else if("2".equals(roleId)){
               user = userService.selectById(userId);
           }else if ("3".equals(roleId)){
               user = userService.selectById(userId);
           }

       } catch (JWTDecodeException e) {
        throw new CustomerEeception("401", "Token解析失败");  // 修改点3：明确异常类型
    }catch (Exception e){
           throw new CustomerEeception("401","您无权访问该内容");
       }
        if(user == null){
            throw new CustomerEeception("401", "用户不存在");
        }
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token);
        }catch (SignatureVerificationException e) {
            throw new CustomerEeception("401", "Token验证失败");
        }catch (Exception e){
            throw new CustomerEeception("401","您无权访问该内容");
        }
        return true;
    }
}
