package Utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaKeys {
    public static class PrivateKeyReader {

        public static PrivateKey get(String filename)
                throws Exception {

            byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }
    public static class PublicKeyReader {

        public static PublicKey get(String filename)
                throws Exception {

            byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }
    }
}
