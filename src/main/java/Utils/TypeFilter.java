package Utils;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.sparkjava.SecurityFilter;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static spark.Spark.halt;

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

        if (types.contains(request.requestMethod())) {

            boolean username=false;
            try {
                String token = request.headers("Authorization").substring(7,request.headers("Authorization").length());
                System.out.println(token);
                CommonProfile profile = JWTHelper.getAuthenticator().validateToken(token);
                System.out.println(profile.getAttribute("username").toString());
                username = request.params(":username").equals(profile.getAttribute("username").toString());
            } catch (NullPointerException e) {
                halt(400, "No username specified");
            }
            if (username) {
                securityFilter.handle(request, response);
            } else
                halt(401, "Invalid permissions");

        }
    }
}
