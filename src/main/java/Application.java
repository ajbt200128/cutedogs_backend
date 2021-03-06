import utils.Pac4jConfig;
import utils.TypeFilter;
import controllers.DogController;
import controllers.ImageController;
import controllers.Paths;
import controllers.UserController;
import database.DatabaseController;
import org.pac4j.sparkjava.SecurityFilter;
import database.Tables;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;

import static spark.Spark.*;

class Application {

    public static void main(String[] args) {
        port(9090);
        DogController dogController = new DogController();
        UserController userController = new UserController();
        ImageController imageController = new ImageController();
        DatabaseController database = DatabaseController.getInstance();
        Pac4jConfig pac4jConfig = Pac4jConfig.getInstance();
        Timer dbPing = new Timer();
        byte[] encoded;
        String PASS = "";

        try {
            encoded = Files.readAllBytes(java.nio.file.Paths.get("p.pwd"));
            PASS = new String(encoded,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


        dbPing.schedule(new TimerTask() {
            @Override
            public void run() {
                database.executeQuery("SELECT 1 FROM "+ (new Tables.DogTable().getName()));
		System.out.println("ping");
            }
	    },0,10*60*1000);

        secure("/etc/letsencrypt/live/cutedogs.org/keystore.jks",PASS,"/etc/letsencrypt/live/cutedogs.org/cacerts",PASS);

        before( "/*",(request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Credentials", "true");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");

            if (request.requestMethod().equals("OPTIONS"))
                halt(200);

        });

        before("/api/user/:username/*", new TypeFilter(new SecurityFilter(pac4jConfig.JWTConfig, "HeaderClient"), "PUT", "POST", "DELETE"));
        post(Paths.CREATE_DOG, dogController.createItem);
        post(Paths.CREATE_IMAGE, imageController.createItem);
        post(Paths.CREATE_USER, userController.createItem);


        get(Paths.GET_DOG, dogController.getItem);
        get(Paths.GET_DOGS, dogController.getItems);
        get(Paths.GET_DOGS_BY_OWNER, dogController.getByOwner);
        get(Paths.GET_DOGS_RANDOM,dogController.getRandomDogs);
        get(Paths.GET_IMAGE, imageController.getItem);
        get(Paths.GET_IMAGES, imageController.getItems);
        get(Paths.GET_USER, userController.getItem);
        get(Paths.GET_USERS, userController.getItems);
        get(Paths.GET_BY_USERNAME, userController.getByUsername);

        before(Paths.LOGIN, new SecurityFilter(pac4jConfig.HTTPConfig, "DirectBasicAuthClient"));

        get(Paths.LOGIN, userController.login);

        put(Paths.UPDATE_DOG, dogController.updateItem);
        put(Paths.UPDATE_IMAGE, imageController.updateItem);
        put(Paths.UPDATE_USER, userController.updateItem);

        before(Paths.LIKE_IMAGE,new SecurityFilter(pac4jConfig.JWTConfig, "HeaderClient"));
        post(Paths.LIKE_IMAGE,imageController.likeImage);

        delete(Paths.DELETE_DOG, dogController.deleteItem);
        delete(Paths.DELETE_IMAGE, imageController.deleteItem);
        delete(Paths.DELETE_USER, userController.deleteItem);
        after((request, response) -> {
            response.type("application/json");
        });

    }

}
