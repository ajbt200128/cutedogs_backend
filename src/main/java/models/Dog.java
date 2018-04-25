package models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Dog extends Model {
    public String profilePictureLink;
    //public UUID uuid;
    public UUID owner;
    public String name;
    public int gender;
    public Date birthday;
    public String breed;
    public List<String> dogLikes;
    public List<String> dogDislikes;
    public String biography;
    public List<Image> images;

    public Dog() {
        this.uuid = UUID.randomUUID();
    }

    public Dog(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "profilePictureLink='" + profilePictureLink + '\'' +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", breed='" + breed + '\'' +
                ", dogLikes=" + dogLikes +
                ", dogDislikes=" + dogDislikes +
                ", biography='" + biography + '\'' +
                ", images=" + images +
                ", uuid=" + uuid +
                '}';
    }
}