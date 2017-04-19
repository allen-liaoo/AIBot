/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Main.Main;
import Audio.Music;
import Command.Command;
import static Command.Command.embed;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class VolumeCommand implements Command {
    
    public final static  String HELP = "This command is for playing an youtube music in the voice channel.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"play`\n"
                                     + "Parameter: `-h | [Youtube Url] | null`";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Play -Help", HELP, true);
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
        else if(args.length == 0)
        {
            int currentVolume = Main.guilds.get(e.getGuild().getId()).getPlayer().getVolume();
            e.getTextChannel().sendMessage("Current volume: " + Integer.toString(currentVolume)).queue();
        }
        else
        {
            int volume = 50;
            try {
                volume = Integer.parseInt(args[0]);
                
                if(volume < 0 || volume > 100)
                {
                    e.getTextChannel().sendMessage(Emoji.error + " Please enter a number between 0 to 100.").queue();
                    return;
                }

                Music.setVolume(e, volume);
                if(volume < 50)
                    e.getTextChannel().sendMessage(Emoji.volume_low + " Volume set to " + volume).queue();
                else
                    e.getTextChannel().sendMessage(Emoji.volume_high + " Volume set to " + volume).queue();
            } catch(NumberFormatException ex){
                e.getTextChannel().sendMessage(Emoji.error + " Please enter a valid number.").queue();
                return;
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
}
