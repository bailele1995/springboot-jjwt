package com.pjb.springbootjjwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by le.bai on 2020/4/18 21:39
 */
public class JwtUtil {
    /**
     * 过期时间15分钟
     */
    private static final long EXPIRE_TIME = 15*60*1000;

    /**
     * 生成签名,15分钟后过期
     * @param username
     * @param userId
     * @return
     */
    public static String sign(String username,String userId,String password){
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(password);
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create().withHeader(header).withClaim("userId",userId)
                .withClaim("username",username).withExpiresAt(date).sign(algorithm);
    }


    public static boolean verity(String token,String password){
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }

    }

    public static void main(String[] args){
        String token = sign("baile","3","111");
        System.out.println(token);

        String userId = JWT.decode(token).getClaim("userId").asString();
        System.out.println(userId);

     //   JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("111")).build();
     //   System.out.println(verity("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dpbk5hbWUiOiJiYWlsZSIsImV4cCI6MTU4NzIxOTUwOSwidXNlcklkIjoiMyJ9.GKfFpNA_PtiR1op0mGp2LL8L26Ig7hiVX1o3KEO8SHY","111"));
    }
}
