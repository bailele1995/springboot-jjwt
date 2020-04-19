package com.pjb.springbootjjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by le.bai on 2020-04-17 20:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String Id;
    String username;
    String password;
}
