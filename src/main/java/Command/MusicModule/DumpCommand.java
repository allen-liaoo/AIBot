/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Command.Command;
import static Command.Command.embed;
import Main.Main;
import Constants.Constants;
import Constants.Emoji;
import Setting.Prefix;
import Utility.UtilBot;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class DumpCommand implements Command {

    public final static String HELP = "This command is for clearing the queue and skip votes.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"stop`\n"
                                    + "Parameter: `-h | null`";
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Dump -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) 
        {
            help(e);
        }
        else
        {   
            if(!e.getMember().getVoiceState().inVoiceChannel()) {
                e.getChannel().sendMessage(Emoji.ERROR + " You are not in a voice channel.").queue();
                return;
            } else if (Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue().isEmpty()) {
                e.getChannel().sendMessage(Emoji.ERROR + " There is no song in the queue.").queue();
                return;
            }
            
            if(UtilBot.isMajority(e.getMember()) ||
                e.getMember().isOwner() || 
                e.getMember().hasPermission(Constants.PERM_MOD) ||
                Constants.D_ID.equals(e.getAuthor().getId()))
            {
                //Prevent user that is not in the same voice channel from stopping the PLAYER
                if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel() ||
                        !e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
                    e.getChannel().sendMessage(Emoji.ERROR + " You and I are not in the same voice channel.").queue();
                    return;
                }
                Main.guilds.get(e.getGuild().getId()).getScheduler().clearQueue().clearVote();
                
                e.getChannel().sendMessage(Emoji.STOP + " Cleared queue and dumped Trump.").queue();
            }
            else
            {
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                        + "members with `Administrator` or `Manage Server` permissions only.\n"
                        + "You can also dump the queue if there is less than 3 members in the voice channel.").queue();
            }
        }
    }

    
}
