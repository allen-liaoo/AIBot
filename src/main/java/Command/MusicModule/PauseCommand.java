/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Command.*;
import Audio.Music;
import static Command.MusicModule.JoinCommand.HELP;
import Config.Emoji;
import Config.Info;
import Config.Prefix;
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
    public final static  String HELP = "This command is for pausing the bot if the bot is playing music.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"pause`\n"
                                     + "Parameter: `-h | null`";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Pause -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
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
            if(Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused())
                e.getTextChannel().sendMessage("Already paused! Type " + Prefix.getDefaultPrefix() + "play to resume.").queue();
            else
            {
                Music.pause(e);
                e.getChannel().sendMessage(Emoji.success + " Track paused.").queue();
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
}
