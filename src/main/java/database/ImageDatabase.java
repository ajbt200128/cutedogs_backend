package database;

import models.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public Image get(UUID uuid) {
        Image i = super.get(uuid);
        if (i != null)
            i.likedBy = getLikes(uuid);
        return i;
    }


    //TODO add like support
    @Override
    public List<Image> getAll() {
        List<Image> imgs = super.getAll();
        if (imgs != null)
            for (Image image : imgs)
                image.likedBy = getLikes(image.uuid);
        return imgs;
    }

    @Override
    public List<Image> getAll(String column, String parameter) {
        List<Image> imgs = super.getAll(column, parameter);
        System.out.println("Imgs "+imgs.toString());
        if (imgs != null)
            for (Image image : imgs) {
                    System.out.println(image.uuid);
                    List<UUID> likes = getLikes(image.uuid);
                    image.likedBy = likes;
                    System.out.println(likes);
            }
        return imgs;
    }

    @Override
    public boolean insert(Image image) {
        boolean result = super.insert(image);

        List<UUID> uuids = getLikes(image.uuid);
        if (image.likedBy != null)
            for (UUID uuid : image.likedBy)
                addLike(image.uuid, uuid);
        return result;
    }

    @Override
    public boolean update(Image image) {

        List<UUID> uuids = getLikes(image.uuid);
        if (image.likedBy != null)
            for (UUID uuid : image.likedBy)
                addLike(image.uuid, uuid);
        return super.update(image);
    }

    public boolean addLike(UUID image, UUID user) {
        try {
            String sql = "INSERT INTO " + Tables.LikesTable.TABLE_NAME + "  (" + Tables.LikesTable.IMAGE + ", " + Tables.LikesTable.USER + ") VALUES (?,?);";
            if (!getLikes(image).contains(user)) {
                dbController.executeStatementUpdate(sql, Arrays.asList(image.toString(), user.toString()));
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean removeLike(UUID image, UUID user) {
        try {
            String sql = "DELETE FROM " + Tables.LikesTable.TABLE_NAME + " WHERE (" + Tables.LikesTable.IMAGE + ", " + Tables.LikesTable.USER + ") = (?,?);";
            if (getLikes(image).contains(user)) {
                dbController.executeStatementUpdate(sql, Arrays.asList(image.toString(), user.toString()));
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public List<UUID> getLikes(UUID imageUUID) {
        String sql = "SELECT * FROM " + Tables.LikesTable.TABLE_NAME + " WHERE " + Tables.LikesTable.IMAGE + " = ?";
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(imageUUID.toString()));

        List<UUID> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(UUID.fromString(rs.getString(Tables.LikesTable.USER)));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    @Override
    Image fromSQL(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                Image image = new Image(UUID.fromString(resultSet.getString(Tables.ImageTable.UUID)));
                image.dog = original ? UUID.fromString(resultSet.getString(Tables.ImageTable.DOG_UUID)) : null;
                image.tags = Arrays.asList(resultSet.getString(Tables.ImageTable.TAGS).split(JOINNER));
                image.imageLink = resultSet.getString(Tables.ImageTable.IMAGE_LINK);
                //image.likedBy
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
                image.imageLink
        };
    }
}
