package com.pjb.springbootjjwt.service;

import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.mapper.UserMapper;
import com.pjb.springbootjjwt.result.ResultDTO;
import com.pjb.springbootjjwt.result.ResultError;
import com.pjb.springbootjjwt.result.UserError;
import com.pjb.springbootjjwt.util.JwtUtil;
import com.pjb.springbootjjwt.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by le.bai on 2020/4/18 21:48
 */
@Service("UserService")
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisUtils redisUtils;

   /* @Autowired
    PasswordEncoder passwordEncoder;*/

    public User findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    public ResultDTO login(String name, String password) {
        Map<String, Object> tokenMap = new HashMap<>();
        //密码需要客户端加密后传递
        String token = null;
        try {
            User user = userMapper.findByUsername(name);
            if(user == null){
                 return ResultDTO.failure(new ResultError(UserError.EMP_IS_NULL_EXIT));
            }else{
                if(!user.getPassword().equals(password)){
                    return ResultDTO.failure(new ResultError(UserError.PASSWORD_OR_NAME_IS_ERROR));
                }else {
                    // 将 user id 、userName保存到 token 里面
                    token = JwtUtil.sign(user.getUsername(),user.getId(),user.getPassword());
                    redisUtils.set("userToken-" + user.getId(), token, 2L * 60);
                }
            }

            if(StringUtils.isEmpty(token)){
                return ResultDTO.failure(new ResultError(UserError.PASSWORD_OR_NAME_IS_ERROR));
            }
            tokenMap.put("token", token);
            tokenMap.put("user",user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultDTO.success(tokenMap);
    }

}
