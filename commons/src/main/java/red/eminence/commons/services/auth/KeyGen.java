package red.eminence.commons.services.auth;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.SignatureAlgorithm;
import lombok.val;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class KeyGen
{
    // asymmetric key pair algorithm
    // must be same as commons/src/main/java/red/eminence/commons/services/auth/JWTConfig.java
    static SignatureAlgorithm alg = JWTConfig.alg;
    
    public static void main (String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // generate keys (in pair) and get base64 encoded strings
        KeyPair keyPair          = alg.keyPair().build();
        String  privateKeyBase64 = Encoders.BASE64.encode(keyPair.getPrivate().getEncoded());
        String  publicKeyBase64  = Encoders.BASE64.encode(keyPair.getPublic().getEncoded());
        // from base64 encoded strings, decode, create spec and generate keys from that spec
        KeyFactory keyFactory            = KeyFactory.getInstance(alg.toString());
        val        decodedPrivateKeySpec = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(privateKeyBase64));
        val        decodedPublicKeySpec  = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyBase64));
        PrivateKey decodedPrivateKey     = keyFactory.generatePrivate(decodedPrivateKeySpec);
        PublicKey  decodedPublicKey      = keyFactory.generatePublic(decodedPublicKeySpec);
        // ensure the keys created originally and keys created from the original's base64 encoded strings are the same
        assert keyPair.getPrivate().getAlgorithm().equals(alg.toString()) : "Algorithm mismatch between key pair and desired algorithm";
        assert keyPair.getPublic().getAlgorithm().equals(alg.toString()) : "Algorithm mismatch between key pair and desired algorithm";
        assert keyPair.getPrivate().getAlgorithm().equals(decodedPrivateKey.getAlgorithm()) : "Algorithm mismatch after encoding and decoding private key";
        assert keyPair.getPublic().getAlgorithm().equals(decodedPublicKey.getAlgorithm()) : "Algorithm mismatch after encoding and decoding public key";
        assert privateKeyBase64.equals(Encoders.BASE64.encode(decodedPrivateKey.getEncoded())) : "Encoded and decoded private key mismatch";
        assert publicKeyBase64.equals(Encoders.BASE64.encode(decodedPublicKey.getEncoded())) : "Encoded and decoded public key mismatch";
        // print out the results
        System.out.println("All assertions passed for encoding and decoding keys");
        System.out.println("privateKey Algorithm = " + keyPair.getPrivate().getAlgorithm());
        System.out.println("publicKey Algorithm = " + keyPair.getPublic().getAlgorithm());
        System.out.println("privateKeyBase64 = " + privateKeyBase64);
        System.out.println("publicKeyBase64 = " + publicKeyBase64);
    }
}
