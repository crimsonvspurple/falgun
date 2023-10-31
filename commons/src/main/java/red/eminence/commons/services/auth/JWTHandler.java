package red.eminence.commons.services.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.stereotype.Component;
import red.eminence.commons.services.user.User;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@SuppressWarnings ("unused")
@Component
@Log4j2
@RequiredArgsConstructor
public class JWTHandler
{
    static final  String    CLAIM_KEY_USERNAME = "sub";
    static final  String    CLAIM_KEY_AUDIENCE = "aud";
    static final  String    CLAIM_KEY_CREATED  = "iat";
    static final  String    AUDIENCE_UNKNOWN   = "unknown";
    static final  String    AUDIENCE_WEB       = "web";
    static final  String    AUDIENCE_MOBILE    = "mobile";
    static final  String    AUDIENCE_TABLET    = "tablet";
    @Getter
    private final JWTConfig jwtConfig;
    private final Clock     clock              = Clock.systemUTC(); // previously this was using DefaultClock.INSTANCE from private Clock of the jwt-api library
    
    // auth
    public String getUsernameFromToken (String token)
    {
        return getClaimFromToken(token, Claims::getSubject);
    }
    
    // auth and ref
    public User getDecodedUserFromToken (String token)
    {
        return getDecodedUserFromToken(token, false);
    }
    
    // auth and ref
    public User getDecodedUserFromToken (String token, boolean ignoreExpiry)
    {
        return getClaimFromToken(token, ignoreExpiry, claims -> {
            val dataMap = claims.get("user", HashMap.class);
            val user    = User.builder().build();
            user.setId((String) dataMap.get("id"));
            user.setFirstName((String) dataMap.get("firstName"));
            user.setLastName((String) dataMap.get("lastName"));
            user.setEmail((String) dataMap.get("email"));
            user.setUserGroupDisplay((String) dataMap.get("userGroupDisplay"));
            user.setPasswordUpdated(dataMap.get("passwordUpdated") != null ? Long.parseLong(dataMap.get("passwordUpdated").toString()) : null);
            // Role and Authorities are returned as an Arraylist within the map.
            // Role will always be seen as an empty array for ALL services EXCEPT USER (because the others operate solely using authorities)
            @SuppressWarnings ("unchecked")
            val authorities = (ArrayList<String>) dataMap.get("authorities");
            user.setAuthorities(authorities);
            // This is done to "propagate the user" onto another service when doing IPC calls.
            user.setCurrentEncodedToken(token);
            return user;
        });
    }
    
    public <T> T getClaimFromToken (String token, boolean ignoreExpiry, Function<Claims, T> claimsResolver)
    {
        final Claims claims = getAllClaimsFromToken(token, ignoreExpiry);
        return claimsResolver.apply(claims);
    }
    
    private Claims getAllClaimsFromToken (String token, boolean ignoreExpiry)
    {
        try {
            // return Jwts.parser().setSigningKey(jwtConfig.getSymmetricSigningKey()).parseClaimsJws(token).getBody();
            return Jwts.parser().verifyWith(jwtConfig.getAsymmetricPublicKey()).build().parseSignedClaims(token).getPayload();
        }
        catch (ExpiredJwtException eje) {
            // in case of refresh, we parse the expired token; validity of expiry will be checked elsewhere
            if (ignoreExpiry) {
                return eje.getClaims();
            }
            else {
                throw eje;
            }
        }
    }
    
    public Date getIssuedAtDateFromToken (String token)
    {
        return getIssuedAtDateFromToken(token, false);
    }
    
    public Date getIssuedAtDateFromToken (String token, boolean ignoreExpiry)
    {
        return getClaimFromToken(token, ignoreExpiry, Claims::getIssuedAt);
    }
    
    public Date getExpirationDateFromToken (String token)
    {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    private Boolean isTokenExpired (String token)
    {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(Date.from(clock.instant()));
    }
    
    private Boolean ignoreTokenExpiration (String token)
    {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }
    
    public String getAudienceFromToken (String token)
    {
        return getClaimFromToken(token, Claims::getAudience).toString();
    }   // set to string; need to check
    
    public <T> T getClaimFromToken (String token, Function<Claims, T> claimsResolver)
    {
        return getClaimFromToken(token, false, claimsResolver);
    }
    
    public String generateToken (User userDetails, boolean isRefreshToken)
    {
        val claims = new HashMap<String, Object>();
        claims.put("user", userDetails);
        return doGenerateToken(claims, userDetails.getId(), generateAudience(), isRefreshToken);
    }
    
    private String doGenerateToken (Map<String, Object> claims, String subject, String audience, boolean isRefreshToken)
    {
        final Date createdDate    = Date.from(clock.instant());
        final Date expirationDate = calculateExpirationDate(createdDate, isRefreshToken);
        return Jwts.builder()
                   .claims(claims)
                   .subject(subject)
                   .audience()
                   .add(audience)
                   .and()
                   .issuedAt(createdDate)
                   .expiration(expirationDate)
                   .signWith(jwtConfig.getAsymmetricPrivateKey(), JWTConfig.alg)
                   .compact();
    }
    
    private String generateAudience ()
    {
        return AUDIENCE_UNKNOWN;
    }
    
    private Date calculateExpirationDate (Date createdDate, boolean isRefreshToken)
    {
        if (isRefreshToken) {
            return new Date(createdDate.getTime() + jwtConfig.getRefreshExpirationMinutes() * 60 * 1000);
        }
        return new Date(createdDate.getTime() + jwtConfig.getExpirationMinutes() * 60 * 1000);
    }
    
    // TODO: Look into these conditions, perhaps some fine tuning would do us some good here.
    // ref
    public Boolean canTokenBeRefreshed (String token, Long passwordResetTimestamp, Long jwtRefreshExpirationMinutes)
    {
        final Date created           = getIssuedAtDateFromToken(token, false);
        Date       lastPasswordReset = null;
        if (passwordResetTimestamp != null) {
            lastPasswordReset = new Date(passwordResetTimestamp * 1000); // This is seconds by default, but the Date API wants millis.
        }
        if (isCreatedBeforeLastPasswordReset(created, lastPasswordReset)) {
            return false;
        }
        final Date current = Date.from(clock.instant());
        val        expiry  = created.toInstant().plus(jwtRefreshExpirationMinutes, ChronoUnit.MINUTES);
        return expiry.isAfter(current.toInstant());
    }
    
    private Boolean isCreatedBeforeLastPasswordReset (Date created, Date lastPasswordReset)
    {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
    
    // auth
    public Boolean validateToken (String token)
    {
        // We don't really care about the return result here.
        // This method validates the JWT when extracting the claims from it.
        final String username = getUsernameFromToken(token);
        return (!username.isBlank() && !isTokenExpired(token));
    }
}
