package Utils;


import models.User;
import org.pac4j.sql.profile.DbProfile;

public class CustomProfile extends DbProfile {
    public CustomProfile() {
    }

    CustomProfile(User user) {
        this.addAttribute("username", user.username);
        this.addAttribute("uuid", user.uuid);
        this.addAttribute("name", user.name);
        //this.addAttribute("dogs",user.dogs);
    }

    public String getUUID() {
        return (String) this.getAttribute("uuid");
    }

    public String getName() {
        return (String) this.getAttribute("name");
    }

}
