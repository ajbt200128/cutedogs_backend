package controllers;

import Utils.JWTHelper;
import Utils.ResponseHandler;
import database.ImageDatabase;
import database.Tables;
import models.Image;
import org.pac4j.core.profile.CommonProfile;
import spark.Route;

import java.util.List;
import java.util.UUID;

public class ImageController extends GenericController<Image, Tables.ImageTable, ImageDatabase> {
    public ImageController() {
        database = new ImageDatabase();
        mClass = Image.class;
    }

    public Route likeImage= (request, response) -> {
        boolean res=false;
        try {
            String token = request.headers("Authorization").substring(7, request.headers("Authorization").length());
            CommonProfile profile = JWTHelper.getAuthenticator().validateToken(token);
            UUID img = UUID.fromString(request.params(":uuid"));
            UUID user =UUID.fromString(profile.getAttribute("uuid").toString());
            System.out.println("aa1");
            if (Integer.parseInt(request.body()) == 1)
                res= database.addLike(img,user);
            else if (Integer.parseInt(request.body()) == 0) {
                System.out.println("dislike");
                res = database.removeLike(img, user);
            }
        }catch (NullPointerException e){
            System.out.println("aaa");
            res = false;
        }
        return new ResponseHandler<Boolean>(res?ResponseHandler.Status.SUCCESS:ResponseHandler.Status.FAILURE).json;
    };
}
