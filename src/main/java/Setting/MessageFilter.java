package Setting;

import Constants.Constants;
import Constants.Emoji;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by liaoyilin on 4/29/17.
 */
public class MessageFilter extends ListenerAdapter {

    private final Pattern invite;
    private final List<String> banWord = new ArrayList();

    public MessageFilter() {
        addBanWord();
        invite = Pattern.compile("(discord\\.gg\\/)([A-Za-z0-9]{5,})(?=\\b|$)");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.getGuild().getMembers().size()>100)
            return;

        if(e.getAuthor().isBot() || e.getAuthor().isFake() || e.getMember().isOwner() || e.getMember().hasPermission(Constants.PERM_MOD))
            return;

        try {
            checkInvite(e);
            checkMention(e);
            checkBanWord(e);
        } catch (PermissionException pe) {
            //Can't do anything :(
        }
    }

    private void checkInvite(GuildMessageReceivedEvent e) {
        if(invite.matcher(e.getMessage().getContent()).find() &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) {
            e.getMessage().addReaction(Emoji.BAN).queue();
        }
    }

    private void checkMention(GuildMessageReceivedEvent e) {
        if(e.getMessage().getMentionedUsers().size() + e.getMessage().getMentionedRoles().size() > 7 &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION))
            e.getMessage().addReaction(Emoji.BAN).queue();
    }

    private void checkBanWord(GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot())
            return;

        for(String bw : banWord) {
            if (e.getMessage().getRawContent().contains(bw) &&
                    e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION))
                e.getMessage().addReaction(Emoji.BAN).queue();
        }
    }

    private void addBanWord() {
        banWord.add("fuck");
        banWord.add("bitch");
        banWord.add("b1tch");
        banWord.add("nigger");
        banWord.add("nigg");
        banWord.add("cunt");
        banWord.add("cnut");
        banWord.add("dick");
        banWord.add("cock");
        banWord.add("c0ck");
    }
}
