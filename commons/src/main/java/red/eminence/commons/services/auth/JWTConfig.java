package red.eminence.commons.services.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.Data;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


@Data
@Configuration
@ConfigurationProperties (prefix = "core.jwt")
public class JWTConfig
{
    static final SignatureAlgorithm alg = Jwts.SIG.EdDSA;
    KeyFactory keyFactory;
    private String asymmetricPrivateKey;    // this should be null for non-user services
    
    private String headerName;
    private String tokenPrefix;
    private String asymmetricPublicKey;

    {
        try {
            keyFactory = KeyFactory.getInstance(alg.toString());
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    private long   expirationMinutes;
    private long   refreshExpirationMinutes;
    
    // I think we are doing a lot of work to get the Keys from strings. Maybe there is an opportunity to cache the keys?
    public PrivateKey getAsymmetricPrivateKey ()
    {
        val decodedPrivateKeySpec = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(asymmetricPrivateKey));
        try {
            return keyFactory.generatePrivate(decodedPrivateKeySpec);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    
    public PublicKey getAsymmetricPublicKey ()
    {
        val decodedPublicKeySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(asymmetricPublicKey));
        try {
            return keyFactory.generatePublic(decodedPublicKeySpec);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
