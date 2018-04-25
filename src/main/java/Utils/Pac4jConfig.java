package Utils;

import database.DatabaseController;
import models.User;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder;
import org.pac4j.core.util.JavaSerializationHelper;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sql.profile.DbProfile;
import org.pac4j.sql.profile.service.DbProfileService;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.List;

public class Pac4jConfig {
    private static final String DB_USERNAME = "cutedogs";
    private static final String DB_PASS = "***REMOVED***";

    private static final Pac4jConfig ourInstance = new Pac4jConfig();

    public final Config HTTPConfig;
    public final DbProfileService dbProfileService;
    public final Config JWTConfig;

    private Pac4jConfig() {
        DatabaseController databaseController = DatabaseController.getInstance();
        dbProfileService = new DbProfileService(databaseController.getMysqlDataSource());
        dbProfileService.setUsersTable("profiles");
        dbProfileService.setPasswordEncoder(new SpringSecurityPasswordEncoder(new StandardPasswordEncoder("SALTTTT")));
        JavaSerializationHelper javaSerializationHelper = new JavaSerializationHelper();
        List<String> packages = javaSerializationHelper.getTrustedPackages();
        packages.add("Utils.CustomProfile");
        javaSerializationHelper.setTrustedPackages(packages);

        dbProfileService.setJavaSerializationHelper(javaSerializationHelper);
        DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(dbProfileService);
        HTTPConfig = new Config(directBasicAuthClient);
        HTTPConfig.setHttpActionAdapter(new DefaultHttpActionAdapter());
        HeaderClient headerClient = new HeaderClient("Authorization", "Bearer ", JWTHelper.getAuthenticator());
        JWTConfig = new Config(headerClient);
        JWTConfig.setHttpActionAdapter(new DefaultHttpActionAdapter());


    }

    public static Pac4jConfig getInstance() {
        return ourInstance;
    }

    public void addProfile(User user, String password) {
        DbProfile customProfile = new DbProfile();//new CustomProfile(user);
        customProfile.setId(user.uuid);
        customProfile.addAttribute("username", user.username);
        customProfile.addAttribute("uuid", user.uuid.toString());
        customProfile.addAttribute("name", user.name);
        dbProfileService.create(customProfile, password);


    }

}
