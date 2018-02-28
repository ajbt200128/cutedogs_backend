package database;

import models.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class ImageDatabase extends GenericDatabase<Image, Tables.ImageTable> {

    public static final String JOINNER = "' ' '";
    private boolean original = true;

    public ImageDatabase() {
        dbController = DatabaseController.getInstance();
        TABLE = new Tables.ImageTable();
    }

    public ImageDatabase(boolean original) {
        dbController = DatabaseController.getInstance();
        TABLE = new Tables.ImageTable();
        this.original = original;
    }

    @Override
    Image FromSQL(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                Image image = new Image(UUID.fromString(resultSet.getString(Tables.ImageTable.UUID)));
                image.dog = original ? UUID.fromString(resultSet.getString(Tables.ImageTable.DOG_UUID)) : null;
                image.tags = Arrays.asList(resultSet.getString(Tables.ImageTable.TAGS).split(JOINNER));
                image.likes = resultSet.getInt(Tables.ImageTable.LIKES);
                image.dislikes = resultSet.getInt(Tables.ImageTable.DISLIKES);
                image.imageLink = resultSet.getString(Tables.ImageTable.IMAGE_LINK);
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    String[] ToSQL(Image image) {
        return new String[]{
                image.uuid.toString(),
                image.dog.toString(),
                String.join(JOINNER, image.tags),
                String.valueOf(image.likes),
                String.valueOf(image.dislikes),
                image.imageLink
        };
    }
}
