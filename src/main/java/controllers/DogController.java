package controllers;

import Utils.ResponseHandler;
import database.DogDatabase;
import database.Tables;
import models.Dog;
import spark.Route;

import java.util.List;
import java.util.UUID;

public class DogController extends GenericController<Dog, Tables.DogTable, DogDatabase> {
    public Route getByOwner = (request, response) -> {
        UUID uuid = UUID.fromString(request.params(":uuid"));
        List<UUID> dogs = database.getByOwner(uuid);
        return (new ResponseHandler<UUID>(ResponseHandler.Status.SUCCESS, dogs)).json;
    };

    public DogController() {
        database = new DogDatabase();
        mClass = Dog.class;
    }
}
