package command.moderation;

import setting.Prefix;
import command.Command;
import constants.Emoji;
import constants.Global;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import system.AILogger;

import java.util.List;

/**
 * Created by liaoyilin on 5/6/17.
 */
public class SoftBanCommand extends Command {

    public final static String HELP = "Soft ban is ban and immediately unban a member to kick and clean up the member's message.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"softban`\n"
                                    + "Parameter: `-h | @Member(s)`";
    private final int delDays = 7;

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Moderation Module", null);
        embed.addField("SoftBan -Help", HELP, true);
        embed.setFooter("Command Help/Usage", null);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }

        if(args.length == 0) {
            e.getTextChannel().sendMessage(Emoji.ERROR + " You need to mention 1 or more members to soft ban!").queue();
        }

        else
        {
            Guild guild = e.getGuild();
            Member selfMember = guild.getSelfMember();

            //Check if the bot have permission to kick.
            if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " I need to have **Ban Members** Permission to soft ban members.").queue();
                return;
            } else if(!e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " You need to have **Ban Members** Permission to soft ban members.").queue();
                return;
            }

            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            AILogger.commandLog(e, this.getClass().getName(), "Called to soft ban " + mentionedUsers.size() + " users.");

            for (User user : mentionedUsers)
            {
                Member member = guild.getMember(user);
                if(!selfMember.canInteract(member))
                {
                    e.getTextChannel().sendMessage(Emoji.ERROR + " Cannot soft ban member: `" + member.getEffectiveName()
                            + "`, he is in a higher role than I am!").queue();
                    return;
                }

                guild.getController().ban(member, delDays).queue(
                    success -> {
                        guild.getController().unban(user).queue();
                        e.getChannel().sendMessage(Emoji.SUCCESS + " Successfully Soft Banned `"+user.getName()+"`").queue();
                    },
                    error -> {
                        e.getChannel().sendMessage(Emoji.ERROR + " An error occurred!\n```"+error.getMessage()+"```").queue();
                    }
                );
            }
        }
    }
}
