package com.pjb.springbootjjwt.controller;

import com.pjb.springbootjjwt.annotation.TokenRequired;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.result.ResultDTO;
import com.pjb.springbootjjwt.result.ResultError;
import com.pjb.springbootjjwt.result.UserError;
import com.pjb.springbootjjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by le.bai on 2020-04-17 20:45
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;


    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResultDTO login( User user){

        String token = userService.login(user.getUsername(), user.getPassword());
        if (token == null) {
            return ResultDTO.failure(new ResultError(UserError.PASSWORD_OR_NAME_IS_ERROR));
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        return ResultDTO.success(tokenMap);
    }

    @TokenRequired
    @GetMapping("/hello")
    public String getMessage(){
        return "你好哇，我是小码仔";
    }
}
