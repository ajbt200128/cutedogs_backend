import com.google.gson.Gson;
import models.Dog;
import models.Image;
import models.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class GenerateDogs {
    private static String[] names = {"John", "Luke", "George", "Greg", "Henry", "Sam", "Austin", "Ben", "Mike", "Matt"};
    private static String[] usernames = {"fdbbdfb", "wg56", "rgtym5", "56h65", "ver45", "9gj4", "943n3", "ijt5", "5hh565", "h5jkskk"};
    private static String[] passwords = {"ree", "bee", "see", "pee", "lee", "jee"};
    private static String[] tags = {"cool", "fun", "cute", "funny", "nice", "big", "small", "corgi", "lab", "zoom", "hop", "pup", "oldpup", "midpup"};
    private static String[] links = {"http://imgur.com/pupaffff", "http://imgur.com/pupegh", "http://imgur.com/puper", "http://imgur.com/pupieeee"};

    private static String[] profileLinks = {"https://i.imgur.com/JXetxQh.jpg", "https://i.imgur.com/SazaHUq.jpg", "https://i.imgur.com/ydi5jMh.jpg", "https://i.imgur.com/Eq8jVm0.jpg", "https://i.imgur.com/W5dL1ut.jpg", "https://i.imgur.com/vrilVVO.jpg", "https://i.imgur.com/VrbPeHX.jpg", "https://i.imgur.com/PdWVKyt.jpg", "https://i.imgur.com/C9Wz6G8.jpg", "https://i.imgur.com/tnjGTBM.jpg", "https://i.imgur.com/Uycj63R.jpg", "https://i.imgur.com/IGGBxys.jpg", "https://i.imgur.com/mG7rmUW.jpg", "https://i.imgur.com/wC3u4rM.jpg", "https://i.imgur.com/wC3u4rM.jpg", "https://i.imgur.com/C73XTb6.jpg", "https://i.imgur.com/qzfVi4e.jpg", "https://i.imgur.com/oo67PIH.jpg", "https://i.imgur.com/JlUvsxa.jpg", "https://i.imgur.com/IzyN8fW.jpg", "https://i.imgur.com/XsaLqi1.jpg", "https://i.imgur.com/bdh4Qpn.jpg", "https://i.imgur.com/6oB0QRU.png", "https://i.imgur.com/LPsxLQE.png", "https://i.imgur.com/lUbmZlU.jpg", "https://i.imgur.com/2cGhWub.jpg"};
    private static String[] dogNames = {"max", "charlie", "bella", "lucy", "daisy", "buddy", "jack", "rocky", "oliver", "bear", "duke", "tucker", "luna", "lola", "sadie", "molly", "maggie", "bailey", "sophie"};
    private static String[] breeds = {"Lab", "Pug", "Bulldog", "German Shepard", "Beagle"};
    private static String[] dogLikes = {"Running", "Playing", "Fetch", "Scratches", "Naps", "Chasing squirrels", "Steak"};
    private static String[] dogDislikes = {"Mailman", "Vacuum", "Cats", "Storms", "Mud", "Strangers", "Mice"};
    private static String[] biographies = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus eget metus sit amet turpis bibendum convallis. Aliquam vel orci pretium, facilisis leo a, imperdiet sem. Nam blandit, elit et ultricies."};
    private static Date[] dates;
    private static String[] datesStr = {"3/9/2001", "8/24/2001", "4/3/2002", "11/5/2002", "11/4/2004", "1/17/2006", "1/30/2012", "1/10/2014", "10/8/2014", "11/5/2018"};

    public static void main(String[] args) {
        dates = new Date[datesStr.length];
        for (int i = 0; i < datesStr.length; i++) {
            try {
                dates[i] = new SimpleDateFormat("dd/MM/yyyy").parse(datesStr[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = genUser(i);
            user.dogs = new ArrayList<>();
            for (int j = 0; j < (new Random()).nextInt(5) + 1; j++) {
                Dog dog = genDog(user.uuid);
                dog.images = new ArrayList<>();
                for (int k = 0; k < (new Random()).nextInt(5) + 1; k++)
                    dog.images.add(genImage(dog.uuid));
                user.dogs.add(dog);
            }
            users.add(user);
        }
        System.out.println(users.size());
        HttpPost httpPost;

        for (User user : users) {
            HttpClient client = HttpClientBuilder.create().build();

            try {
                System.out.println("POST user " + user.name);
                httpPost = new HttpPost(new URI("http://0.0.0.0:8080/api/users"));
                httpPost.addHeader(HttpHeaders.AUTHORIZATION, genHeader(user));
                httpPost.setEntity(new StringEntity(new Gson().toJson(user), "UTF-8"));
                System.out.println("POST");
                client.execute(httpPost);
                //Thread.sleep(2000);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String genHeader(User user) {
        String auth = user.username + ":" + passwords[new Random().nextInt(passwords.length)];
        System.out.println(auth);
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        return "Basic " + new String(encodedAuth);
    }

    private static Dog genDog(UUID owner) {
        Random random = new Random();
        Dog dog = new Dog();
        dog.name = dogNames[random.nextInt(dogNames.length)];
        dog.biography = biographies[random.nextInt(biographies.length)];
        dog.birthday = dates[random.nextInt(dates.length)];
        dog.breed = breeds[random.nextInt(breeds.length)];
        dog.points = random.nextInt(1000) + 1;
        dog.profilePictureLink = profileLinks[random.nextInt(profileLinks.length)];
        dog.dogLikes = new ArrayList<>();
        dog.dogDislikes = new ArrayList<>();
        for (int i = 0; i < random.nextInt(5) + 1; i++)
            dog.dogLikes.add(dogLikes[random.nextInt(dogLikes.length)]);
        for (int i = 0; i < random.nextInt(5) + 1; i++)
            dog.dogDislikes.add(dogDislikes[random.nextInt(dogDislikes.length)]);
        dog.gender = random.nextInt(1);
        dog.owner = owner;
        return dog;
    }

    private static Image genImage(UUID dog) {
        Random random = new Random();
        Image image = new Image();
        image.dog = dog;
        image.likes = random.nextInt(1000) + 1;
        image.dislikes = random.nextInt(1000) + 1;
        image.tags = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10) + 1; i++)
            image.tags.add(tags[random.nextInt(tags.length)]);
        image.imageLink = links[random.nextInt(links.length)];
        return image;
    }

    private static User genUser(int i) {
        User user = new User();
        user.name = names[i];
        user.username = usernames[i];
        return user;
    }

}
