/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Command.*;
import Audio.Music;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import Main.Main;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PauseCommand extends Command{
    public final static  String HELP = "This command is for pausing/resuming the bot if the bot is playing music.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"pause` or `"+ Prefix.getDefaultPrefix() +"resume` or `"+ Prefix.getDefaultPrefix() +"unpause`\n"
                                     + "Parameter: `-h | null`";
    
    private String type = "";
    
    public PauseCommand(String invoke)
    {
        if("pause".equals(invoke)) type = "pause";
        else if("resume".equals(invoke)) type = "resume";
    }
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Pause -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if("pause".equals(type))
        {
            if(Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused())
                e.getTextChannel().sendMessage("Already paused! Type `" + Prefix.getDefaultPrefix() + "resume` to resume.").queue();
            else
            {
                Music.pause(e);
            }
        }
        else if("resume".equals(type))
        {
            if(!Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused())
                e.getTextChannel().sendMessage("Already resumed! Type `" + Prefix.getDefaultPrefix() + "pause` to pause.").queue();
            else
            {
                Music.resume(e);
            }
        }
        
    }

}
