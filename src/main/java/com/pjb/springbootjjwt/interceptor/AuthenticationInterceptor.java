package com.pjb.springbootjjwt.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pjb.springbootjjwt.annotation.TokenRequired;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.service.UserService;
import com.pjb.springbootjjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * Created by le.bai on 2020/4/18 20:25
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(TokenRequired.class)) {
            TokenRequired userLoginToken = method.getAnnotation(TokenRequired.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                  //  ResultDTO.failure(new ResultError(UserError.TOKEN_IS_NOT_EXIT));
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getClaim("userId").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                User user = userService.findUserById(userId);
                if (user == null) {
                  //  ResultDTO.failure(new ResultError(UserError.EMP_IS_NULL_EXIT));
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                try {
                    if(!JwtUtil.verity(token,user.getPassword())){
                     //   ResultDTO.failure(new ResultError(UserError.TOKEN_IS_VERITYED));
                        throw new RuntimeException("无效的令牌");
                    }
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
