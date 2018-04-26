package controllers;

import utils.ResponseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.GenericDatabase;
import database.Tables;
import models.Model;
import spark.Route;

import java.util.List;
import java.util.UUID;

import static spark.Spark.halt;

public class GenericController<M extends Model, T extends Tables.Table, D extends GenericDatabase<M, T>> {
    public D database;
    public Class<M> mClass;
    public Route getItems = (request, response) -> {
        List<M> ms;
        if (request.queryParams("count")!=null)
            ms = database.getAll(Integer.parseInt(request.queryParams("count")));
        else
            ms = database.getAll();

        return (new ResponseHandler<M>(ResponseHandler.Status.SUCCESS, ms).json);
    };
    public Route getItem = (request, response) -> {
        UUID uuid;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        try{
            uuid = UUID.fromString(request.params(":uuid"));
        }catch (IllegalArgumentException e){
            response.status(400);
            return (new ResponseHandler(ResponseHandler.Status.FAILURE,"Invalid UUID string: "+request.params(":uuid")).json);
        }
        M item = database.get(uuid);
        //if not null
        if (item == null)
            halt(404, "Item not found");

        return new ResponseHandler<M>(ResponseHandler.Status.SUCCESS, item,gson).json;
    };
    public Route createItem = (request, response) -> {
        //TODO validation
        System.out.println("AAAA");
        Gson gson = new GsonBuilder().setDateFormat("MMMMMMMMMM dd, yyyy").create();
        M item = gson.fromJson(request.body(), mClass);

        boolean result = database.insert(item);
        return (new ResponseHandler<M>(ResponseHandler.Status.fromBool(result))).json;
    };
    public Route updateItem = (request, response) -> {
        Gson gson = new GsonBuilder().setDateFormat("MMMMMMMMMM dd, yyyy").create();
        System.out.println(request.body());
        M item = gson.fromJson(request.body(), mClass);

        boolean result = database.update(item);
        return (new ResponseHandler<M>(ResponseHandler.Status.fromBool(result))).json;
    };

    public Route deleteItem = (request, response) -> {
        boolean result = database.remove(UUID.fromString(request.params(":uuid")));
        return (new ResponseHandler<M>(ResponseHandler.Status.fromBool(result))).json;
    };
}
