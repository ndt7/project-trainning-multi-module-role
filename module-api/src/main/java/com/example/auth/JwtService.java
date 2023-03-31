package com.example.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/// file cấu hình, xác thực jwt
@Service
public class JwtService {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    /// trich xuat ten nguoi dung -- từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);    /// gọi phương thức getSubject
    }

    /// trich xuat claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /// thực hiện
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())    /// SECRET_KEY da duoc ma hoa
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /// UserDetail
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /// generate token dua vao UserDetails
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()   /// builder
                .setClaims(extraClaims)             ////
                .setSubject(userDetails.getUsername())     ///// username
                .setIssuedAt(new Date(System.currentTimeMillis()))   /// thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  ///  thiết lập thời gian hết hạn 1 ngày
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)        /// thuật toán mã hóa SECRET_KEY
                .compact();
    }

    /// kiem tra xem token co hop le khong
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);   //// tim username bang token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);  /// kiem tra username tim duoc voi username trong userDetails
    }

    /// kiểm tra token con han khong
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);  /// tra ve ngay het han
    }

    /// thuat toan su dung ma hoa
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
