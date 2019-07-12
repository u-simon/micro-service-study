package com.simon.microservice.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author Simon
 * @Date 2019-07-11 16:00
 */
public class TokenUtils {

	private final static String priKey = "";

	private final static String pubKey = "";

	public static String getToken(String uid, int exp) {
		long endTime = System.currentTimeMillis() + 1000 * 60 * exp;
		return Jwts.builder().setSubject(uid).setExpiration(new Date(endTime))
				.signWith(SignatureAlgorithm.RS512, priKey).compact();
	}

	public static String checkToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token).getBody();
		return claims.get("sub", String.class);
	}

}
