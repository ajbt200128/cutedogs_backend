package Utils;

import database.UserDatabase;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.sql.profile.DbProfile;

import java.security.KeyPair;

public class JWTHelper {
    public static String getToken(String username) {
        KeyPair rsaKeyPair = null;
        String uuid = new UserDatabase().getByUsername(username).toString();
        DbProfile dbProfile = Pac4jConfig.getInstance().dbProfileService.findById(uuid);
        try {
            rsaKeyPair = new KeyPair(RsaKeys.PublicKeyReader.get("public_key.der"), RsaKeys.PrivateKeyReader.get("private_key.der"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JwtGenerator<DbProfile> generator = new JwtGenerator<>(new RSASignatureConfiguration(rsaKeyPair));
        return generator.generate(dbProfile);
    }

    public static JwtAuthenticator getAuthenticator() {
        KeyPair rsaKeyPair;
        JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
        try {
            rsaKeyPair = new KeyPair(RsaKeys.PublicKeyReader.get("public_key.der"), RsaKeys.PrivateKeyReader.get("private_key.der"));
            jwtAuthenticator.addSignatureConfiguration(new RSASignatureConfiguration(rsaKeyPair));
            return jwtAuthenticator;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
