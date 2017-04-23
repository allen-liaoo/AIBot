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
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PauseCommand implements Command{
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
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Pause -Help", HELP, true);
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
        else if("pause".equals(type))
        {
            if(Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused())
                e.getTextChannel().sendMessage("Already paused! Type `" + Prefix.getDefaultPrefix() + "resume` to resume.").queue();
            else
            {
                Music.pause(e);
                e.getChannel().sendMessage(Emoji.PAUSE + " Player paused.").queue();
            }
        }
        else if("resume".equals(type))
        {
            if(!Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused())
                e.getTextChannel().sendMessage("Already resumed! Type `" + Prefix.getDefaultPrefix() + "pause` to pause.").queue();
            else
            {
                Music.resume(e);
                e.getChannel().sendMessage(Emoji.RESUME + " Player resumed.").queue();
            }
        }
        
    }

}
