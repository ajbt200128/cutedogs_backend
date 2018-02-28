package controllers;

import database.ImageDatabase;
import database.Tables;
import models.Image;

public class ImageController extends GenericController<Image, Tables.ImageTable, ImageDatabase> {
    public ImageController() {
        database = new ImageDatabase();
        mClass = Image.class;
    }
}
