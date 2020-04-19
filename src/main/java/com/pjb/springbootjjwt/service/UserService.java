package com.pjb.springbootjjwt.service;

import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.mapper.UserMapper;
import com.pjb.springbootjjwt.result.ResultDTO;
import com.pjb.springbootjjwt.result.ResultError;
import com.pjb.springbootjjwt.result.UserError;
import com.pjb.springbootjjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by le.bai on 2020/4/18 21:48
 */
@Service("UserService")
public class UserService {
    @Autowired
    UserMapper userMapper;

   /* @Autowired
    PasswordEncoder passwordEncoder;*/

    public User findUserById(String userId) {
        return userMapper.findUserById(userId);
    }

    public String login(String name, String password) {
        //密码需要客户端加密后传递
        String token = null;
        try {
            User user = userMapper.findByUsername(name);
            if(user == null){
                 ResultDTO.failure(new ResultError(UserError.EMP_IS_NULL_EXIT));
            }else{
                if(!user.getPassword().equals(password)){
                    ResultDTO.failure(new ResultError(UserError.PASSWORD_OR_NAME_IS_ERROR));
                }else {
                    // 将 user id 、userName保存到 token 里面
                    token = JwtUtil.sign(user.getUsername(),user.getId(),user.getPassword());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

}
