package command.utility;

import command.Command;
import constants.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;
import utility.UtilBot;

/**
 * @author AlienIdeology
 */
public class AvatarCommand extends Command {

    public final static String HELP = "This command is for getting the avatar of a user.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"avatar`\n"
                                    + "Parameter: `-h | @Mention | [ID] | null`\n"
                                    + "@Mention: Mention a user.\n"
                                    + "ID: get avatar by ID.\n"
                                    + "null: Get self avatar.\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Utility Module", null);
        embed.addField("Avatar -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        User avatar = null;

        if(args.length == 0) {      // Self
            avatar = e.getAuthor();
        } else if(args[0].length() == 18) {  // ID (18 characters)
            avatar = e.getJDA().getUserById(args[0]);
        } else if(!e.getMessage().getMentionedUsers().isEmpty()) {  // Mention
            avatar = e.getMessage().getMentionedUsers().get(0);
        }

        if(avatar != null) {
            EmbedBuilder embed = new EmbedBuilder().setColor(UtilBot.randomColor())
                .setDescription("**["+avatar.getName()+"#"+avatar.getDiscriminator()+"'s avatar](" + avatar.getEffectiveAvatarUrl() + ")**")
                .setImage(avatar.getEffectiveAvatarUrl());

            e.getChannel().sendMessage(embed.build()).queue();
        } else {
            e.getChannel().sendMessage(Emoji.ERROR + " Please mention or enter an ID of a valid user.").queue();
        }
    }
}
