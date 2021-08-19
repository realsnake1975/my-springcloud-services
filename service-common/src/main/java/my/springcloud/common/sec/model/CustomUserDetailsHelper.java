package my.springcloud.common.sec.model;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.springcloud.common.constants.CommonConstants;

@Slf4j
@NoArgsConstructor
public class CustomUserDetailsHelper {

	// private static final long JWT_EXP = 3; // 단위: 시간
	private static final String JWT_SECRET = "7w!z%C*F-JaNdRgUjXn2r5u8x/A?D(G+KbPeShVmYp3s6v9y$B&E)H@McQfTjWnZ";

	// JWT Header
	private static final String TOKEN_TYPE = "JWT";
	private static final String TOKEN_ISSUER = "uplus.co.kr";
	private static final String TOKEN_SUBJECT = "uplus";

	private static final String TIME_ZONE = "Asia/Seoul";

	private final CustomUserDetails systemUser = new CustomUserDetails("system", "시스템",
		Collections.singletonList(new SimpleGrantedAuthority("ROLE_SYSTEM")), 0L);

	private long jwtExp = 3;

	public CustomUserDetailsHelper(long jwtExp) {
		this.jwtExp = jwtExp;
	}

	private Key getSecretKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}

	private Jws<Claims> getJwsClaims(String authorization) {
		return Jwts.parserBuilder()
			.setSigningKey(this.getSecretKey())
			.build()
			.parseClaimsJws(authorization.replace(CommonConstants.TOKEN_PREFIX, ""));
	}

	/**
	 * Claims 반환
	 *
	 * @param token - JWT
	 * @return Claims
	 */
	private Claims getClaimsFromAccessToken(String token) {
		return this.getJwsClaims(token).getBody();
	}

	/**
	 * JWT 발행
	 *
	 * @param customUser - 토큰 사용자
	 * @param expDate - 만료일시
	 * @param refreshTokenYn - 리프레쉬 토큰 여부
	 * @return JWT
	 */
	private String generateJwt(CustomUserDetails customUser, Date expDate, boolean refreshTokenYn) {
		SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.DEFAULT_DATE_FORMAT, Locale.KOREA);
		log.debug("> token expired date: {}", sdf.format(expDate));

		List<String> roles = customUser.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toList());
		Long accountId = (customUser.getAccountId() == null) ? 0L : customUser.getAccountId();

		return Jwts.builder()
			.signWith(this.getSecretKey(), SignatureAlgorithm.HS512)
			.setHeaderParam("typ", TOKEN_TYPE)
			.setId(UUID.randomUUID().toString())
			.setIssuer(TOKEN_ISSUER)
			.setSubject(TOKEN_SUBJECT)
			.setExpiration(expDate)
			.setAudience(customUser.getUsername())
			.setIssuedAt(new Date())
			.claim("rol", roles)
			.claim("name", customUser.getName())
			.claim("accountId", accountId)
			.claim("refreshTokenYn", refreshTokenYn)
			.compact();
	}

	/**
	 * Access 토큰 발행
	 *
	 * @param customUser - 토큰 사용자
	 * @return JWT
	 */
	public String generateAccessToken(CustomUserDetails customUser) {
		Date expDate = Date.from(LocalDateTime.now().plusMinutes(this.jwtExp).atZone(ZoneId.of(TIME_ZONE)).toInstant());
		return this.generateJwt(customUser, expDate, false);
	}

	/**
	 * 리프레쉬 토큰 발행
	 *
	 * @param customUser - 토큰 사용자
	 * @return JWT
	 */
	public String generateRefreshToken(CustomUserDetails customUser) {
		Date expDate = Date.from(LocalDateTime.now().plusDays(30L).atZone(ZoneId.of(TIME_ZONE)).toInstant());
		return this.generateJwt(customUser, expDate, true);
	}

	/**
	 * 시스템용 JWT 토큰 반환
	 *
	 * @return JWT
	 */
	public String generateJwt4System() {
		Date expDate = Date.from(LocalDateTime.now().plusHours(12L).atZone(ZoneId.of(TIME_ZONE)).toInstant());
		return this.generateJwt(this.systemUser, expDate, false);
	}

	/**
	 * JWT에서 CustomUserDetails 반환
	 *
	 * @param token - JWT
	 * @return CustomUserDetails
	 */
	public CustomUserDetails getSimpleUser(String token) {
		Claims claims = this.getClaimsFromAccessToken(token);

		String username = (String)claims.get("aud");
		String name = (String)claims.get("name");
		Long accountId = Long.parseLong(String.valueOf(claims.get("accountId")));
		List<GrantedAuthority> authorities = ((List<?>)claims.get("rol")).stream()
			.map(authority -> new SimpleGrantedAuthority((String)authority))
			.collect(Collectors.toList());

		return new CustomUserDetails(username, name, authorities, accountId);
	}

	/**
	 * JWT에서 리프레쉬 토큰 여부
	 *
	 * @param token - JWT
	 * @return boolean
	 */
	public boolean isRefreshToken(String token) {
		Claims claims = this.getClaimsFromAccessToken(token);
		return (Boolean)claims.get("refreshTokenYn");
	}

}
