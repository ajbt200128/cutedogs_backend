package controllers;

public class Paths {
    public static final String GET_DOGS = "/api/dogs";
    public static final String GET_DOG = "/api/dogs/:uuid";
    public static final String CREATE_DOG = "/api/user/:username/dogs";
    public static final String UPDATE_DOG = "/api/user/:username/dogs/:uuid";
    public static final String DELETE_DOG = "/api/user/:username/dogs/:uuid";
    public static final String GET_DOGS_BY_OWNER = "/api/dogs/owner/:uuid";

    public static final String GET_USERS = "/api/users";
    public static final String GET_USER = "/api/user/:uuid";
    public static final String CREATE_USER = "/api/users";
    public static final String UPDATE_USER = "/api/user/:uuid";
    public static final String DELETE_USER = "/api/user/:uuid";
    public static final String GET_USER_UUID = "/api/users/getUUID/:username";

    public static final String GET_IMAGES = "/api/images";
    public static final String GET_IMAGE = "/api/images/:uuid";
    public static final String CREATE_IMAGE = "/api/user/:username/images";
    public static final String UPDATE_IMAGE = "/api/user/:username/images/:uuid";
    public static final String DELETE_IMAGE = "/api/user/:username/images/:uuid";

    public static final String LOGIN = "/api/login";
}
