package models;

import java.util.List;
import java.util.UUID;

public class Image extends Model {
    public UUID dog;
    public List<String> tags;
    public int likes;
    public int dislikes;
    public String imageLink;

    public Image() {
        this.uuid = UUID.randomUUID();
    }

    public Image(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Image{" +
                "dog=" + dog +
                ", tags=" + tags +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", imageLink='" + imageLink + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}
