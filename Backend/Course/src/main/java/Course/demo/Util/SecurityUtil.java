package Course.demo.Util;

import Course.demo.Dto.Response.ResLoginDTO;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SecurityUtil {

    private static final Logger logger = Logger.getLogger(SecurityUtil.class.getName());
    private final JwtEncoder jwtEncoder;

    @Value("${hct14.jwt.base64-secret}")
    private String jwtKey;

    @Value("${hct14.jwt.access-token-validity-in-seconds}")
    private long JwtExpirationAccsessToken;

    @Value("${hct14.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createAccessToken(String email, ResLoginDTO dto) {
        if (email == null || dto == null || dto.getUser() == null) {
            logger.warning("Invalid input data for creating access token.");
            throw new IllegalArgumentException("Invalid email or user data.");
        }

        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setName(dto.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.JwtExpirationAccsessToken, ChronoUnit.SECONDS);

        List<String> listAuthority = new ArrayList<>();
        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        // Create the JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userToken)
                .claim("permission", listAuthority)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        String accessToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        logger.info("Access token created successfully for email: " + email);
        return accessToken;
    }

    public String createRefreshToken(String email, ResLoginDTO dto) {
        if (email == null || dto == null || dto.getUser() == null) {
            logger.warning("Invalid input data for creating refresh token.");
            throw new IllegalArgumentException("Invalid email or user data.");
        }

        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        ResLoginDTO.UserInsideToken userToken = new ResLoginDTO.UserInsideToken();
        userToken.setId(dto.getUser().getId());
        userToken.setEmail(dto.getUser().getEmail());
        userToken.setName(dto.getUser().getName());

        // Create the JWT claims for refresh token
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userToken)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        String refreshToken = this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

        logger.info("Refresh token created successfully for email: " + email);
        return refreshToken;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            logger.warning("Provided refresh token is null or empty.");
            throw new IllegalArgumentException("Refresh token cannot be null or empty.");
        }

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JWT_ALGORITHM)
                .build();

        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
            logger.info("Refresh token decoded successfully.");
            return decodedJwt;
        } catch (JwtException e) {
            logger.log(Level.SEVERE, "Invalid refresh token: " + e.getMessage(), e);
            throw new IllegalArgumentException("Invalid refresh token.", e);
        }
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    // Additional helper methods could be added here for more checks, logging, etc.

}
