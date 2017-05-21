package command.moderation;

import command.Command;
import constants.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clean a member or bot's message.
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class CleanCommand extends Command {

    public final static String HELP = "This command is for cleaning the chat(Advanced prune).\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"clean`\n"
                                    + "Parameter: `-h | bot | @Member(s) | @Role(s) |  chat | null`\n"
                                    + "null: Delete messages by AIBot.\n"
                                    + "bot: Delete messages sent by any bot.\n"
                                    + "@Member(s): Delete messages sent by any mentioned members.\n"
                                    + "@Role(s): Delete messages sent by any mentioned roles.\n"
                                    + "chat: Clean up the chat and give people more personal space ^ ^\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Moderation Module", null);
        embed.addField("Clean -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        TextChannel tc = e.getTextChannel();
        if (!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            tc.sendMessage(Emoji.ERROR + " I do not have the `Manage Message` and `Message History` Permission!").queue();
            return;
        } else if (!e.getMember().hasPermission(e.getTextChannel(), Permission.MESSAGE_MANAGE)) {
            tc.sendMessage(Emoji.ERROR + " You need to have the `Manage Message` and `Message History` Permission!").queue();
            return;
        }

        try {
            if (args.length == 0) {
                deleteMemberMessage(tc, e.getJDA().getSelfUser());
            } else if ("bot".equals(args[0])) {
                deleteBotMessage(tc);
            } else if ("chat".equals(args[0])) {
                String msg = "";
                for (int i = 0; i < 100; i++) {
                    msg += Emoji.Unicode.zero_width + "\n\n";
                }
                e.getChannel().sendMessage("Hold on..." + msg).queue();
            } else {
                List<User> users = e.getMessage().getMentionedUsers();
                deleteMemberMessage(tc, users.toArray(new User[users.size()]));

                List<Role> roles = e.getMessage().getMentionedRoles();
                deleteRoleMessage(tc, roles.toArray(new Role[roles.size()]));
            }
        } catch (IllegalArgumentException lae) {
            tc.sendMessage(Emoji.ERROR + " Looks like there is no message to delete...").queue();
        }
    }

    private void deleteMemberMessage(TextChannel tc, User... users)
    {
        if(users.length==0) return;
        List<Message> messages = new ArrayList<>();
        for (User user : users) {
            List<Message> newMsg = tc.getIterableHistory()
                    .cache(false)
                    .stream()
                    .limit(1000)
                    .filter(message -> message.getAuthor().getId().equals(user.getId()))
                    .filter(message -> ChronoUnit.WEEKS.between(message.getCreationTime(), ZonedDateTime.now()) < 2)
                    .collect(Collectors.toList());
            messages.addAll(newMsg);
        }
        messages = messages.size() > 100 ? messages.subList(0, 100) : messages;
        int size = messages.size();

        tc.deleteMessages(messages).queue(
                success -> tc.sendMessage(Emoji.SUCCESS + " Cleaned up " + size + " message(s) from " + users.length + " member(s).").queue()
        );
    }

    private void deleteRoleMessage(TextChannel tc, Role... roles)
    {
        if(roles.length==0) return;
        List<Message> messages;
        messages = new ArrayList<>();
        for (Role role : roles) {
            List<Message> newMsg = tc.getIterableHistory()
                    .cache(false)
                    .stream()
                    .limit(1000)
                    .filter(message -> !message.getMember().getRoles().isEmpty() && !message.getMember().getRoles().contains(role))
                    .filter(message -> ChronoUnit.WEEKS.between(message.getCreationTime(), ZonedDateTime.now()) < 2)
                    .collect(Collectors.toList());
            messages.addAll(newMsg);
        }

        messages = messages.size() > 100 ? messages.subList(0, 100) : messages;
        int size = messages.size();
        tc.deleteMessages(messages).queue(
                success -> tc.sendMessage(Emoji.SUCCESS + " Cleaned up " + size + " message(s) from " + roles.length + " role(s).").queue()
        );
    }

    private void deleteBotMessage(TextChannel tc)
    {
        List<Message> messages = tc.getIterableHistory()
                .cache(false)
                .stream()
                .limit(1000)
                .filter(message -> message.getAuthor().isBot())
                .filter(message -> ChronoUnit.WEEKS.between(message.getCreationTime(), ZonedDateTime.now())<2)
                .collect(Collectors.toList());
        messages = messages.size() > 100 ? messages.subList(0, 100) : messages;
        int size = messages.size();

        tc.deleteMessages(messages).queue(
                success -> tc.sendMessage(Emoji.SUCCESS + " Cleaned up "+size+" message(s) from bots.").queue()
        );
    }

}
