/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.Music;
import Command.Command;
import Main.Main;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class StopCommand implements Command{

    public final static String HELP = "This command is for stopping the bot from playing music and unbind to the voice channel.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"stop`\n"
                                    + "Parameter: `-h | null`";
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Stop -Help", HELP, true);
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
                e.getMember().hasPermission(Constants.PERM_MOD) ||
                Constants.D_ID.equals(e.getAuthor().getId()))
            {
                Music.stop(e);
            }
            else 
            {
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                        + "members with `Administrator` or `Manage Server` permissions only.\n"
                        + "You can also stop the player if there is less than 3 members in the voice channel.").queue();
            }
        }
    }

    
}
