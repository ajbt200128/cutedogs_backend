package controllers;

import Utils.ResponseHandler;
import com.google.gson.Gson;
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
        List<M> ms = database.getAll();
        return (new ResponseHandler<M>(ResponseHandler.Status.SUCCESS, ms).json);
    };
    public Route getItem = (request, response) -> {
        System.out.println(request.params(":uuid"));
        M item = database.get(UUID.fromString(request.params(":uuid")));
        //if not null
        if (item == null)
            halt(404, "Item not found");
        return (new ResponseHandler<M>(ResponseHandler.Status.SUCCESS, item).json);
    };
    public Route createItem = (request, response) -> {
        //TODO validation
        M item = new Gson().fromJson(request.body(), mClass);

        boolean result = database.insert(item);
        return (new ResponseHandler<M>(ResponseHandler.Status.fromBool(result))).json;
    };
    public Route updateItem = (request, response) -> {
        M item = new Gson().fromJson(request.body(), mClass);
        boolean result = database.update(item);
        return (new ResponseHandler<M>(ResponseHandler.Status.fromBool(result))).json;
    };

    public Route deleteItem = (request, response) -> {
        boolean result = database.remove(UUID.fromString(request.params(":uuid")));
        return (new ResponseHandler<M>(ResponseHandler.Status.fromBool(result))).json;
    };
}
