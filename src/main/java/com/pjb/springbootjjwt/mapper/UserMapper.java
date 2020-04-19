package com.pjb.springbootjjwt.mapper;

import com.pjb.springbootjjwt.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by le.bai on 2020-04-17 20:50
 */
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    User findUserById(@Param("Id") String Id);
}
