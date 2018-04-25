package database;

import models.Dog;
import models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserDatabase extends GenericDatabase<User, Tables.UserTable> {
    DatabaseController dbController;
    private boolean original = true;

    public UserDatabase() {
        dbController = DatabaseController.getInstance();
        TABLE = new Tables.UserTable();
    }

    public UserDatabase(boolean original) {
        dbController = DatabaseController.getInstance();
        TABLE = new Tables.UserTable();
        this.original = original;
    }

    public UUID getByUsername(String username) {
        String sql = "SELECT UUID FROM " + TABLE.getName() + " WHERE " + Tables.UserTable.USERNAME + " = (?) ";
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(username));
        UUID uuid = null;
        try {
            if (rs.next()) {
                uuid = UUID.fromString(rs.getString(Tables.UserTable.UUID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    private void updateDogs(User user) {
        if (user.dogs != null) {
            DogDatabase dogDatabase = new DogDatabase();
            List<Dog> dogs = (new UserDatabase()).get(user.uuid).dogs;
            List<UUID> uuids = user.dogs.stream().map(e -> e.uuid).collect(Collectors.toList());
            for (Dog dog : dogs)
                if (!uuids.contains(dog.uuid))
                    dogDatabase.remove(dog.uuid);

            for (Dog dog : user.dogs) {
                dog.owner = user.uuid;
                if (dogDatabase.get(dog.uuid) == null) {
                    dogDatabase.insert(dog);
                } else {
                    dogDatabase.update(dog);
                }
            }
        }
    }

    @Override
    public boolean insert(User user) {
        super.insert(user);
        updateDogs(user);
        return true;
    }

    @Override
    public boolean update(User user) {
        super.update(user);
        updateDogs(user);
        return true;
    }

    @Override
    User fromSQL(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                User user = new User(UUID.fromString(resultSet.getString(Tables.UserTable.UUID)));
                user.name = resultSet.getString(Tables.UserTable.NAME);
                user.username = resultSet.getString(Tables.UserTable.USERNAME);
                user.dogs = original ? (new DogDatabase(false)).getAll(Tables.DogTable.OWNER_UUID, resultSet.getString(Tables.UserTable.UUID)) : null;
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    String[] ToSQL(User user) {
        return new String[]{
                user.uuid.toString(),
                user.name,
                user.username
        };
    }
}
