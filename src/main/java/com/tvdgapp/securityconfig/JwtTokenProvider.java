package com.tvdgapp.securityconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration}")
    private Long jwtExpirationDate;

    @PostConstruct
    public void init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateToken(String userName) {
        Map<String, Object> claims = Map.of();  // Use immutable empty map
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token, String authenticatedUserName) {
        final String username = extractUsername(token);
        return (username.equals(authenticatedUserName) && !isTokenExpired(token));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
            // Log the exception and return false instead of throwing
            return false;
        }
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}


//@Component
//public class JwtTokenProvider {
//    @Value("${app.jwt-secret}")
//    private String jwtSecret;
//    @Value("${app.jwt-expiration}")
//    private Long jwtExpirationDate;
//
////    @Value("${jwt.token.secret-key:secret-key}")
////    private String secretKey;
////
////    @Value("${jwt.token.expire-length:86400000}")
////    private long validityInMilliseconds;
////
////    @Value("${jwt.authorities.key}")
////    public String AUTHORITIES_KEY;
//
//    @PostConstruct
//    public void init() {
//        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//    }
//
//    public Boolean isTokenExpired(String token) {
//        try {
//            return extractExpiration(token).before(new Date());
//        } catch (ExpiredJwtException e) {
//            return true;
//        }
//    }
//
//    public String generateToken (Authentication authentication) {
//        String username = authentication.getName();
//        Date currentDate = new Date();
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(currentDate)
//                .setExpiration(expireDate)
//                .signWith(Key())
//                .compact();
//    }
//    public String generateToken(String userName) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, userName);
//    }
//
////    public String generateToken(String userName) {
////        Map<String, Object> claims = new HashMap<>();
////        return createToken(claims, userName);
////    }
//
//    private String createToken(Map<String, Object> claims, String subject) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + jwtExpirationDate);
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
//    }
//
//    public boolean validateToken(String token, String authenticatedUserName) {
//        final String username = extractUsername(token);
//        return (username.equals(authenticatedUserName) && !isTokenExpired(token));
//    }
//
//    @SuppressWarnings("NullAway")
//    public String extractUserTypeFromToken(String token) throws UnsupportedEncodingException {
//        Claims claims = this.extractAllClaims(token);
//        return (String) claims.get(USER_TYPE);
//    }
//
//    public @Nullable String extractTokenFromRequest(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//
//        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7, headerAuth.length());
//        }
//
//        return null;
//    }
//
//    public void setValidityInMilliseconds(long validityInMilliseconds) {
//        this.jwtExpirationDate = validityInMilliseconds;
//    }
//
//
//    private Key Key() {
//        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
//        return Keys.hmacShaKeyFor(bytes);
//    }
//
//    public  String getUsername(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(Key())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Key())
//                    .build()
//                    .parse(token);
//            return true;
//        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
