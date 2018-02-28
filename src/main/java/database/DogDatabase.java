package database;

import models.Dog;
import models.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DogDatabase extends GenericDatabase<Dog, Tables.DogTable> {
    public static final String LIKE_JOINER = "' ' '";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public boolean original = true;
    DatabaseController dbController;

    public DogDatabase() {
        dbController = DatabaseController.getInstance();
        TABLE = new Tables.DogTable();
    }

    public DogDatabase(boolean original) {
        dbController = DatabaseController.getInstance();
        TABLE = new Tables.DogTable();
        this.original = original;
    }

    public List<UUID> getByOwner(UUID ownerUUID) {
        String sql = "SELECT UUID FROM " + TABLE.getName() + " WHERE " + Tables.DogTable.OWNER_UUID + " = (?)";
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(ownerUUID.toString()));
        List<UUID> dogs = new ArrayList<>();
        try {
            while (rs.next()) {
                dogs.add(UUID.fromString(rs.getString(Tables.Table.UUID)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dogs;
    }

    @Override
    public boolean update(Dog dog) {
        super.update(dog);
        updateImages(dog);
        return true;
    }

    private void updateImages(Dog dog) {
        if (dog.images != null) {
            ImageDatabase imageDatabase = new ImageDatabase();
            List<Image> images = (new DogDatabase()).get(dog.uuid).images;
            List<UUID> uuids = dog.images.stream().map(e -> e.uuid).collect(Collectors.toList());
            for (Image image : images)
                if (!uuids.contains(image.uuid))
                    imageDatabase.remove(image.uuid);

            for (Image image : dog.images) {
                if (imageDatabase.get(image.uuid) == null) {
                    imageDatabase.insert(image);
                } else {
                    imageDatabase.update(image);
                }
            }
        }
    }

    @Override
    public boolean insert(Dog dog) {
        super.insert(dog);
        updateImages(dog);
        return true;
    }

    public Dog FromSQL(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                Dog dog = new Dog(UUID.fromString(resultSet.getString(Tables.DogTable.UUID)));
                dog.profilePictureLink = resultSet.getString(Tables.DogTable.PROFILE_PICTUE_LINK);
                dog.owner = original ? UUID.fromString(resultSet.getString(Tables.DogTable.OWNER_UUID)) : null;
                dog.name = resultSet.getString(Tables.DogTable.NAME);
                dog.gender = resultSet.getInt(Tables.DogTable.GENDER);
                dog.birthday = resultSet.getDate(Tables.DogTable.BIRTHDAY);
                dog.breed = resultSet.getString(Tables.DogTable.BREED);
                dog.dogLikes = Arrays.asList(resultSet.getString(Tables.DogTable.DOG_LIKES).split(LIKE_JOINER));
                dog.dogDislikes = Arrays.asList(resultSet.getString(Tables.DogTable.DOG_DISLIKES).split(LIKE_JOINER));
                dog.biography = resultSet.getString(Tables.DogTable.BIOGRAPHY);
                dog.points = resultSet.getInt(Tables.DogTable.POINTS);
                dog.images = (new ImageDatabase()).getAll(Tables.ImageTable.DOG_UUID, resultSet.getString(Tables.DogTable.UUID));
                return dog;
            } else {
                System.out.println("no next");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] ToSQL(Dog dog) {
        System.out.println(dog.uuid);

        return new String[]{
                dog.profilePictureLink,
                        dog.uuid.toString(),
                        dog.owner.toString() ,
                        dog.name ,
                        String.valueOf(dog.gender),
                        DATE_FORMAT.format(dog.birthday),
                        dog.breed,
                        String.join(LIKE_JOINER, dog.dogLikes),
                        String.join(LIKE_JOINER, dog.dogDislikes),
                        dog.biography,
                        String.valueOf(dog.points)
        };
    }
}
