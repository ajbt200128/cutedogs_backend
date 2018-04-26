package controllers;

import utils.JWTHelper;
import utils.Pac4jConfig;
import utils.ResponseHandler;
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
        User user = database.get(uuid);
        return (new ResponseHandler<User>(ResponseHandler.Status.SUCCESS, user)).json;
    };

    public Route login = (request, response) -> {
        String base64Credentials = request.headers("Authorization").substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                Charset.forName("UTF-8"));
        String token = JWTHelper.getToken(credentials.split(":", 2)[0]);
        System.out.println(request.toString()+"aaaa");
        return new Gson().toJson(token);
    };

    public UserController() {
        database = new UserDatabase();
        mClass = User.class;
        this.createItem = (request, response) -> {
            User item = new Gson().fromJson(request.body(), mClass);
            String base64Credentials = request.headers("Authorization").substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            String password = credentials.split(":", 2)[1];
            if (database.getByUsername(item.username)==null) {
                System.out.println(database.getByUsername(item.username));
                Pac4jConfig.getInstance().addProfile(item, password);
                boolean result = database.insert(item);
                return (new ResponseHandler<User>(ResponseHandler.Status.fromBool(result))).json;
            }else{
                response.status(409);
                return (new ResponseHandler<>(ResponseHandler.Status.FAILURE,"Username taken")).json;
            }
        };
    }
}
