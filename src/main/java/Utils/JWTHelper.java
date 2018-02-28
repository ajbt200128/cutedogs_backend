package Utils;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import database.UserDatabase;
import org.pac4j.jwt.config.encryption.ECEncryptionConfiguration;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.sql.profile.DbProfile;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class JWTHelper {
    private static String SECRET = "zDf3b1EXUNaj5u1Qzg2Ep0ZJCyKtLXXa";

    public static String getToken(String username) {
        String uuid = new UserDatabase().getByUsername(username).toString();
        DbProfile dbProfile = Pac4jConfig.getInstance().dbProfileService.findById(uuid);

        JwtGenerator<DbProfile> generator = new JwtGenerator<>(getAuthenticator().getSignatureConfigurations().get(0), getAuthenticator().getEncryptionConfigurations().get(0));

        return generator.generate(dbProfile);
    }

    public static JwtAuthenticator getAuthenticator() {
        JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair rsaKeyPair = keyGen.generateKeyPair();
        jwtAuthenticator.addSignatureConfiguration(new RSASignatureConfiguration(rsaKeyPair));
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair ecKeyPair = keyPairGenerator.generateKeyPair();
        ECEncryptionConfiguration encConfig = new ECEncryptionConfiguration(ecKeyPair);
        encConfig.setAlgorithm(JWEAlgorithm.ECDH_ES_A128KW);
        encConfig.setMethod(EncryptionMethod.A192CBC_HS384);
        jwtAuthenticator.addEncryptionConfiguration(encConfig);
        return jwtAuthenticator;
    }
}
