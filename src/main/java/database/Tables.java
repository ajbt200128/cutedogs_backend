package database;

public class Tables {

    public interface Table {
        String UUID = "uuid";
        String ID = "ID";

        String getName();

        String getSQL();

        String getRows();
    }

    public static class DogTable implements Table {
        public static final String TABLE_NAME = "dogs";
        public static final String PROFILE_PICTUE_LINK = "profile_picture_link";
        public static final String OWNER_UUID = "owner_uuid";

        public static final String NAME = "name";
        public static final String GENDER = "gender";
        public static final String BIRTHDAY = "birthday";
        public static final String BREED = "breed";
        public static final String DOG_LIKES = "dog_likes";
        public static final String DOG_DISLIKES = "dog_dislikes";
        public static final String BIOGRAPHY = "biography";
        public static final String POINTS = "points";

        @Override
        public String getName() {
            return TABLE_NAME;
        }

        @Override
        public String getSQL() {
            return "(" +
                    "" + PROFILE_PICTUE_LINK + " VARCHAR(255)," +
                    "" + UUID + " VARCHAR(191)," +
                    "" + OWNER_UUID + " VARCHAR(191)," +
                    "" + NAME + " VARCHAR(255)," +
                    "" + GENDER + " TINYINT," +
                    "" + BIRTHDAY + " DATE," +
                    "" + BREED + " VARCHAR(255)," +
                    "" + DOG_LIKES + " VARCHAR(500)," +
                    "" + DOG_DISLIKES + " VARCHAR(500)," +
                    "" + BIOGRAPHY + " VARCHAR(500)," +
                    "" + POINTS + " INT," +
                    "PRIMARY KEY (" + UUID + ")," +
                    "FOREIGN KEY (" + OWNER_UUID + ") REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.UUID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
        }

        @Override
        public String getRows() {
            return "" +
                    "" + PROFILE_PICTUE_LINK + "," +
                    "" + UUID + "," +
                    "" + OWNER_UUID + "," +
                    "" + NAME + "," +
                    "" + GENDER + "," +
                    "" + BIRTHDAY + "," +
                    "" + BREED + "," +
                    "" + DOG_LIKES + "," +
                    "" + DOG_DISLIKES + "," +
                    "" + BIOGRAPHY + "," +
                    "" + POINTS + "";
        }
    }

    public static class UserTable implements Table {
        public static final String TABLE_NAME = "users";
        public static final String UUID = "uuid";
        public static final String NAME = "name";
        public static final String USERNAME = "username";

        @Override
        public String getName() {
            return TABLE_NAME;
        }

        @Override
        public String getSQL() {
            return "(" +
                    "" + UUID + " VARCHAR(191)," +
                    "" + NAME + " VARCHAR(255)," +
                    "" + USERNAME + " VARCHAR(255)," +
                    "PRIMARY KEY (" + UUID + ")" +
                    ")";
        }

        @Override
        public String getRows() {
            return "" +
                    "" + UUID + "," +
                    "" + NAME + "," +
                    "" + USERNAME + "" +
                    "";
        }
    }

    public static class ImageTable implements Table {
        public static final String TABLE_NAME = "images";
        public static final String UUID = "uuid";
        public static final String DOG_UUID = "dog_uuid";
        public static final String TAGS = "tags";
        public static final String LIKES = "likes";
        public static final String DISLIKES = "dislikes";
        public static final String IMAGE_LINK = "image_link";

        @Override
        public String getName() {
            return TABLE_NAME;
        }

        @Override
        public String getSQL() {
            return "(" +
                    "" + UUID + " VARCHAR(191)," +
                    "" + DOG_UUID + " VARCHAR(191)," +
                    "" + TAGS + " VARCHAR(255)," +
                    "" + LIKES + " INT," +
                    "" + DISLIKES + " INT," +
                    "" + IMAGE_LINK + " VARCHAR(255)," +
                    "PRIMARY KEY (" + UUID + ")," +
                    "FOREIGN KEY (" + DOG_UUID + ") REFERENCES " + DogTable.TABLE_NAME + "(" + DogTable.UUID + ") " +
                    "ON DELETE CASCADE " +
                    "ON UPDATE CASCADE" +
                    ")";
        }

        @Override
        public String getRows() {
            return "" +
                    "" + UUID + "," +
                    "" + DOG_UUID + "," +
                    "" + TAGS + "," +
                    "" + LIKES + "," +
                    "" + DISLIKES + "," +
                    "" + IMAGE_LINK +
                    "";
        }
    }

    public static class ProfilesTable implements Table {
        public static final String TABLE_NAME = "profiles";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LINKED_ID = "linkedid";
        public static final String SERIALIZED_PROFILE = "serializedprofile";

        @Override
        public String getName() {
            return TABLE_NAME;
        }

        @Override
        public String getSQL() {
            return "(id varchar(255)," +
                    USERNAME + " varchar(255)," +
                    PASSWORD + " varchar(255)," +
                    LINKED_ID + " varchar(255)," +
                    SERIALIZED_PROFILE + " varchar(767)," +
                    "PRIMARY KEY (id)," +
                    "KEY username (username)," +
                    "KEY linkedid (linkedid)" +
                    ") CHARSET=utf8";
        }

        @Override
        public String getRows() {
            return "id" + "," +
                    USERNAME + "," +
                    PASSWORD + "," +
                    LINKED_ID + "," +
                    SERIALIZED_PROFILE;
        }
    }
}
