package com.hieunt.medlink.app.utils;

import com.hieunt.medlink.app.entities.UserRoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMs;

    @Value("${jwt.private.key.path}")
    private String privateKeyPath;

    @Value("${jwt.public.key.path}")
    private String publicKeyPath;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey();
            this.publicKey = loadPublicKey();
            logger.info("JWT keys loaded successfully from PEM files");
        } catch (Exception e) {
            logger.error("Failed to load JWT keys from PEM files", e);
            throw new RuntimeException("Failed to initialize JWT keys", e);
        }
    }

    private PrivateKey loadPrivateKey() throws Exception {
        ClassPathResource resource = new ClassPathResource(privateKeyPath);
        String key = new String(Files.readAllBytes(resource.getFile().toPath()),
                java.nio.charset.StandardCharsets.UTF_8);

        // Remove header, footer, and whitespace
        String privateKeyPEM = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey() throws Exception {
        ClassPathResource resource = new ClassPathResource(publicKeyPath);
        String key = new String(Files.readAllBytes(resource.getFile().toPath()),
                java.nio.charset.StandardCharsets.UTF_8);

        // Remove header, footer, and whitespace
        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return keyFactory.generatePublic(keySpec);
    }

    private PrivateKey getSigningKey() {
        return privateKey;
    }

    private PublicKey getVerificationKey() {
        return publicKey;
    }

    /**
     * Generate token with a set of roles.
     */
    public String generateToken(String username, UserRoleEntity.Role role) {
        long expMillis = jwtExpirationInMs != null ? jwtExpirationInMs : 3600_000L;
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name()) // chỉ 1 role
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSigningKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getVerificationKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Return roles as a Set<String>. Safe-checks to avoid ClassCastException.
     */
    public UserRoleEntity.Role getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getVerificationKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String roleName = claims.get("role", String.class);
            return roleName != null ? UserRoleEntity.Role.valueOf(roleName) : null;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getVerificationKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        String token = request.getParameter("token");
        if (token != null) {
            return token;
        }
        return null;
    }

    public long getExpirationDurationInSeconds(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();
        long nowMillis = System.currentTimeMillis();
        return (expiration.getTime() - nowMillis) / 1000;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getVerificationKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
