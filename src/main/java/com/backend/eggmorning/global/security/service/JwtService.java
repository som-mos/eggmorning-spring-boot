package com.backend.eggmorning.global.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.backend.eggmorning.domain.user.entity.User;
import com.backend.eggmorning.global.constant.JwtConstant;
import com.backend.eggmorning.global.exceptions.InvalidAccessTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

	private String secret;
	private long tokenDuration;
	private long refreshTokenDuration;

	public JwtService(
		@Value("${security.jwt.secret}") String secret,
		@Value("#{${security.jwt.duration}}") long tokenDuration,
		@Value("#{${security.jwt.refresh.duration}}") long refreshTokenDuration
	) {
		this.secret = secret;
		this.tokenDuration = tokenDuration;
		this.refreshTokenDuration = refreshTokenDuration;
	}

	public String createToken(User user) {
		return Jwts.builder()
			.setHeaderParam("typ", "JWT")
			.setSubject(JwtConstant.SUBJECT_USER_TOKEN)
			.setExpiration(new Date(System.currentTimeMillis() + tokenDuration))
			.claim(JwtConstant.CLAIM_USER, generateUserClaim(user))
			.signWith(SignatureAlgorithm.HS256, secret.getBytes())
			.compact();
	}

	private Map<String, Object> generateUserClaim(User user) {
		Map<String, Object> claim = new HashMap<>();
		claim.put(JwtConstant.CLAIM_EMAIL, user.getEmail());
		claim.put(JwtConstant.CLAIM_ROLES, user.getRoles());
		return claim;
	}

	public String createRefreshToken(User user) {
		return Jwts.builder()
			.setHeaderParam("typ", "JWT")
			.setSubject(JwtConstant.SUBJECT_REFRESH_TOKEN)
			.setExpiration(new Date(System.currentTimeMillis() + refreshTokenDuration))
			.claim(JwtConstant.CLAIM_EMAIL, user.getEmail())
			.claim(JwtConstant.CLAIM_IS_REFRESH_TOKEN, true)
			.signWith(SignatureAlgorithm.HS256, secret.getBytes())
			.compact();
	}

	public Map<String, Object> parseTokenClaim(String token) {
		Jws<Claims> claims = null;

		try {
			claims = Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token);
		} catch (Exception e) {
			throw new InvalidAccessTokenException(token);
		}

		return claims.getBody();
	}
}
