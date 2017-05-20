package main;

import net.dv8tion.jda.core.entities.User;

import java.time.Instant;

/**
 * UserWrapper for afk settings (Global)
 */
public class UserWrapper {

    private final User user;
    private String afk;
    private Instant afkTimeStamp;

    public UserWrapper(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getAfk() {
        return afk;
    }

    public Instant getAfkTimeStamp() {
        return afkTimeStamp;
    }

    public void setAfk(String afk) {
        this.afk = afk;
        this.afkTimeStamp = Instant.now();
    }
}
