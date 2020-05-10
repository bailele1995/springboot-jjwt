package com.pjb.springbootjjwt.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pjb.springbootjjwt.annotation.TokenRequired;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.mapper.UserMapper;
import com.pjb.springbootjjwt.result.ResultDTO;
import com.pjb.springbootjjwt.result.ResultError;
import com.pjb.springbootjjwt.result.UserError;
import com.pjb.springbootjjwt.util.JwtUtil;
import com.pjb.springbootjjwt.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;


/**
 * Created by le.bai on 2020/4/18 20:25
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    UserMapper userMapper;

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

        PrintWriter out = null ;
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(TokenRequired.class)) {
            TokenRequired userLoginToken = method.getAnnotation(TokenRequired.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    out = httpServletResponse.getWriter();
                    out.append(JSON.toJSONString(ResultDTO.failure(new ResultError(UserError.NONE_TOKEN))));
                    return false;
                }
                // 获取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getClaim("userId").asString();
                } catch (JWTDecodeException j) {
                    out = httpServletResponse.getWriter();
                    out.append(JSON.toJSONString(ResultDTO.failure(new ResultError(UserError.TOKEN_CHECK_ERROR))));
                    return false;
                }
                //查询token中登录用户数据库中是否存在
                User user = userMapper.findUserById(userId);
                if (user == null) {
                    out = httpServletResponse.getWriter();
                    out.append(JSON.toJSONString(ResultDTO.failure(new ResultError(UserError.EMP_IS_NULL_EXIT))));
                    return false;
                }
                // 验证 token
                try {
                    if (!JwtUtil.verity(token, user.getPassword())) {
                        //如果token检验失败,查询redis中是否存在，redis为空，用户重新登录
                        if (redisUtils.get("userToken-" + user.getId()) == null) {
                            out = httpServletResponse.getWriter();
                            out.append(JSON.toJSONString(ResultDTO.failure(new ResultError(UserError.TOKEN_IS_VERITYED))));
                            return false;
                        }
                        //如果redis不为空，刷新token
                        String reToken = JwtUtil.sign(user.getUsername(),user.getId(),user.getPassword());
                        //将刷新后的token保存在redis中
                        redisUtils.set("userToken-" + user.getId(), reToken, 2L * 60);
                        out = httpServletResponse.getWriter();
                        out.append(JSON.toJSONString(ResultDTO.failure(reToken,new ResultError(UserError.TOKEN_IS_EXPIRED))));
                        return false;
                    }
                } catch (Exception e) {
                    out = httpServletResponse.getWriter();
                    out.append(JSON.toJSONString(ResultDTO.failure(new ResultError(UserError.TOKEN_CHECK_ERROR))));
                    return false;
                }
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
