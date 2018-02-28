import Utils.Pac4jConfig;
import Utils.TypeFilter;
import com.google.gson.Gson;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import controllers.DogController;
import controllers.ImageController;
import controllers.Paths;
import controllers.UserController;
import database.DatabaseController;
import database.Tables;
import models.Dog;
import models.User;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sql.profile.service.DbProfileService;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import static spark.Spark.*;

class Application {
    public static void main(String[] args) {
        System.out.println(Tables.DogTable.TABLE_NAME + "AAAAAAAAAA");
        port(8080);
        test();
        DogController dogController = new DogController();
        UserController userController = new UserController();
        ImageController imageController = new ImageController();

        get("/hi", (request, response) -> "a");
        before("/hi", (request, response) -> {
            System.out.println("hi");
        });
        before("/*",(request, response) -> {
                    response.header("Access-Control-Allow-Origin", "*");
        });
        DatabaseController database = DatabaseController.getInstance();
        Pac4jConfig pac4jConfig = Pac4jConfig.getInstance();
        before("/api/user/:username/*", new TypeFilter(new SecurityFilter(pac4jConfig.HTTPConfig, "DirectBasicAuthClient"), "PUT", "POST", "DELETE"));
        post(Paths.CREATE_DOG, dogController.createItem);
        post(Paths.CREATE_IMAGE, imageController.createItem);
        post(Paths.CREATE_USER, userController.createItem);


        get(Paths.GET_DOG, dogController.getItem);
        get(Paths.GET_DOGS, dogController.getItems);
        get(Paths.GET_DOGS_BY_OWNER, dogController.getByOwner);
        get(Paths.GET_IMAGE, imageController.getItem);
        get(Paths.GET_IMAGES, imageController.getItems);
        get(Paths.GET_USER, userController.getItem);
        get(Paths.GET_USERS, userController.getItems);
        get(Paths.GET_USER_UUID, userController.getByUsername);
        before(Paths.LOGIN, new SecurityFilter(pac4jConfig.HTTPConfig, "DirectBasicAuthClient"));
        get(Paths.LOGIN, userController.login);

        put(Paths.UPDATE_DOG, dogController.updateItem);
        put(Paths.UPDATE_IMAGE, imageController.updateItem);
        put(Paths.UPDATE_USER, userController.updateItem);

        delete(Paths.DELETE_DOG, dogController.deleteItem);
        delete(Paths.DELETE_IMAGE, imageController.deleteItem);
        delete(Paths.DELETE_USER, userController.deleteItem);
        after((request, response) -> {
            response.type("application/json");
        });

    }

    public static void config() {
        JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
        String KEY2 = "aa";
        jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration(KEY2));
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeyPair rsaKeyPair = keyGen.generateKeyPair();
        jwtAuthenticator.addSignatureConfiguration(new RSASignatureConfiguration(rsaKeyPair));
        HeaderClient client = new HeaderClient("Authorization", "Bearer", jwtAuthenticator);


        DataSource dataSource = new MysqlDataSource();
        DbProfileService dbProfileService = new DbProfileService(dataSource);

        DirectBasicAuthClient directBasicAuthClient = new DirectBasicAuthClient(dbProfileService);
    }

    private static void test() {
        User user = new User(UUID.fromString("62ed05bb-8fce-41c4-9e16-974658a550a6"));
        user.username = "RON";
        user.name = "RONNNN RON";
        //user.password = "password";

        Dog dog = new Dog();
        dog.points = 9;
        dog.biography = "RUFF";
        dog.dogDislikes = Arrays.asList("BB", "B");
        dog.dogLikes = Arrays.asList("ABBBBB", "B");
        dog.breed = "BS";
        dog.birthday = Calendar.getInstance().getTime();
        dog.name = "BBBBf";
        dog.profilePictureLink = "goog";

        user.dogs = Arrays.asList(dog);
        System.out.println((new Gson().toJson(user)));
    }
}
