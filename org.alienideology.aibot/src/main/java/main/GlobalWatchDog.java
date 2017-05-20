package main;

import constants.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import utility.UtilBot;

import java.util.ArrayList;
import java.util.List;

/**
 * A global watchdog for user's global settings (afk)
 * @author AlienIdeology
 */
public class GlobalWatchDog {

    private List<UserWrapper> afkUsers = new ArrayList<>();

    public void onAFKMention(List<User> mentioned, TextChannel channel) {
        if(mentioned.isEmpty()) return;

        List<UserWrapper> afkMsgs = new ArrayList<>();

        for (User user : mentioned) {
            for (UserWrapper afk : afkUsers) {
                if(user.getId().equals(afk.getUser().getId())) {
                    afkMsgs.add(afk);
                }
            }
        }

        for (UserWrapper adkUser: afkMsgs) {
            EmbedBuilder blocker = new EmbedBuilder().setColor(UtilBot.randomColor())
                .setFooter("Status set at ", null).setTimestamp(adkUser.getAfkTimeStamp())
                .setAuthor("AFK User: " + adkUser.getUser().getName(), null, null)
                .setDescription(Emoji.GUILD_IDLE+ "`" + adkUser.getAfk() + "`");
            channel.sendMessage(blocker.build()).queue();
        }

    }

    public void addAFK(User user, String message) {

        /* No UserWrapper made yet */
        if(afkUsers.stream().noneMatch(afk -> afk.getUser().getId().equals(user.getId()))) {
            UserWrapper newUser = new UserWrapper(user);
            newUser.setAfk(message);
            afkUsers.add(newUser);
        } else {
            afkUsers.stream().filter(afk -> afk.getUser().getId().equals(user.getId())).findFirst().get().setAfk(message);
        }

    }

    public boolean removeAFK(User user) {
        if(afkUsers.stream().noneMatch(afk -> afk.getUser().getId().equals(user.getId()))) {
            return false;
        }

        afkUsers.remove(afkUsers.stream().filter(afk -> afk.getUser().getId().equals(user.getId())).findFirst().get());
        return true;
    }

}
