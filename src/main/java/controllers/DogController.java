package controllers;

import Utils.ResponseHandler;
import database.DogDatabase;
import database.Tables;
import models.Dog;
import spark.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DogController extends GenericController<Dog, Tables.DogTable, DogDatabase> {
    public Route getByOwner = (request, response) -> {
        UUID uuid = UUID.fromString(request.params(":uuid"));
        List<UUID> dogs = database.getByOwner(uuid);
        return (new ResponseHandler<>(ResponseHandler.Status.SUCCESS, dogs)).json;
    };
    public Route getItems = (request, response) -> {
        List<Dog> dogs = new ArrayList<>();
        System.out.println(request.queryParams("count")+" "+request.queryParams("tag"));
        if (request.queryParams("count")!=null&&request.queryParams("tag")!=null)
            dogs = database.getByTag(request.queryParams("tag"),Integer.parseInt(request.queryParams("count")));
        else if (request.queryParams("count")!=null)
            dogs = database.getAll(Integer.parseInt(request.queryParams("count")));
        else
            dogs = database.getAll(20);


        return (new ResponseHandler<Dog>(ResponseHandler.Status.SUCCESS, dogs).json);
    };

    public Route getRandomDogs = (request, response) -> {
        List<Dog> dogs = new ArrayList<>();
        System.out.println(request.queryParams("count"));
        if (request.queryParams("count")!=null)
            dogs = database.getRandom(Integer.parseInt(request.queryParams("count")));
        return (new ResponseHandler<>(ResponseHandler.Status.SUCCESS, dogs)).json;
    };
    public DogController() {
        database = new DogDatabase();
        mClass = Dog.class;
    }
}
