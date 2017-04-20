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
import Resource.Constants;
import Resource.Emoji;
import Setting.Prefix;
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
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

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
                e.getChannel().sendMessage(Emoji.error + " You are not in a voice channel.").queue();
                return;
            } else if (Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue().isEmpty()) {
                e.getChannel().sendMessage(Emoji.error + " There is no song in the queue.").queue();
                return;
            }
            
            int mem = 0;
            //Only count non-Bot Users
            List<Member> members = Main.guilds.get(e.getGuild().getId()).getVc().getMembers();
            for(Member m : members) {
                if(!m.getUser().isBot())
                    mem++;
            }
            
            if(mem <= 2 ||
                e.getMember().isOwner() || 
                e.getMember().hasPermission(Permission.ADMINISTRATOR) || 
                e.getMember().hasPermission(Permission.MANAGE_SERVER) || 
                Constants.D_ID.equals(e.getAuthor().getId()))
            {
                //Prevent user that is not in the same voice channel from stopping the player
                if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel() ||
                        !e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
                    e.getChannel().sendMessage(Emoji.error + " You and I are not in the same voice channel.").queue();
                    return;
                }
                Main.guilds.get(e.getGuild().getId()).getScheduler().clearQueue();
                Main.guilds.get(e.getGuild().getId()).getScheduler().clearVote();
                
                e.getChannel().sendMessage(Emoji.stop + " Cleared queue and dumped Trump.").queue();
            }
            else
            {
                e.getChannel().sendMessage(Emoji.error + " This command is for server owner, bot owner, or "
                        + "members with `Administrator` or `Manage Server` permissions only.\n"
                        + "You can also stop the player if there is less than 3 members in the voice channel.").queue();
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
