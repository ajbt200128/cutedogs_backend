package models;

import java.util.List;
import java.util.UUID;

public class User extends Model {
    public String name;
    public String username;
    public List<Dog> dogs;

    public User() {
        this.uuid = UUID.randomUUID();
    }

    public User(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", dogs=" + dogs +
                ", uuid=" + uuid +
                '}';
    }
}
