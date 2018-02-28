package controllers;

import Utils.JWTHelper;
import Utils.Pac4jConfig;
import Utils.ResponseHandler;
import com.google.gson.Gson;
import database.Tables;
import database.UserDatabase;
import models.User;
import spark.Route;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.UUID;

public class UserController extends GenericController<User, Tables.UserTable, UserDatabase> {
    public Route getByUsername = (request, response) -> {
        String username = request.params(":username");
        UUID uuid = database.getByUsername(username);
        return (new ResponseHandler<UUID>(ResponseHandler.Status.SUCCESS, uuid)).json;
    };
    public Route login = (request, response) -> {
        String base64Credentials = request.headers("Authorization").substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        String token = JWTHelper.getToken(credentials.split(":", 2)[0]);
        return (new ResponseHandler(ResponseHandler.Status.SUCCESS, token)).json;
    };

    public UserController() {
        database = new UserDatabase();
        mClass = User.class;
        this.createItem = (request, response) -> {
            //TODO validation | John | fdbbd
            User item = new Gson().fromJson(request.body(), mClass);
            String base64Credentials = request.headers("Authorization").substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            String password = credentials.split(":", 2)[1];
            Pac4jConfig.getInstance().addProfile(item,password);
            boolean result = database.insert(item);
            return (new ResponseHandler<User>(ResponseHandler.Status.fromBool(result))).json;
        };
    }
}
