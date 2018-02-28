package Utils;

import org.pac4j.sparkjava.SecurityFilter;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class TypeFilter implements Filter {
    private List<String> types;
    private SecurityFilter securityFilter;

    public TypeFilter(SecurityFilter securityFilter, String... types) {
        this.types = Arrays.asList(types);
        this.securityFilter = securityFilter;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        /*String SECRET = "zDf3b1EXUNaj5u1Qzg2Ep0ZJCyKtLXXa";
        String token = request.headers("Authorization");
        token = token.substring(6,token.length()-1);
        System.out.println(JWTHelper.getAuthenticator().getEncryptionConfigurations().get(0).toString());
        CommonProfile profile = JWTHelper.getAuthenticator().validateToken(token);
        String username = profile.getAttribute(":username").toString();*/
        String base64Credentials = request.headers("Authorization").substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        System.out.println(request.params() + "AAE");
        if (types.contains(request.requestMethod()))
            if (request.params(":username").equals(credentials.split(":")[0])) {
                System.out.println(request.params(":username") + credentials.split(":")[0]);
                securityFilter.handle(request, response);
            } else
                throw new Exception("Invalid permissions");
        else
            System.out.println("REE");
    }
}
