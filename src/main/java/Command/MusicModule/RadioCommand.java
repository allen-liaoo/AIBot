/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Audio.Radio;
import Command.Command;
import static Command.Command.embed;
import Resource.Constants;
import Setting.Prefix;
import Utility.UtilNum;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class RadioCommand implements Command{

    public final static String HELP = "This command is for loading an automatic playlist.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"radio`\n"
                                    + "Parameter: `-h | [Station Name] | null`\n"
                                    + "[Station Name]: Load the radio station and start playing.\n"
                                    + "null: Get a list of available radio stations.\n";
    
    private EmbedBuilder embedpl = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("FM -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            String stations = Radio.getStations();
            
            embedpl.setAuthor("AIBot Radio", null, Constants.B_AVATAR);
            embedpl.setDescription("Usage: `" + Prefix.DIF_PREFIX + "radio [Station Name]`\n");
            embedpl.addField("Available Radio Stations", stations, true);
            embedpl.setThumbnail(Constants.B_AVATAR);
            embedpl.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
            embedpl.setColor(UtilNum.randomColor());
            embedpl.setTimestamp(Instant.now());
            
            e.getChannel().sendMessage(embedpl.build()).queue();
            embedpl.clearFields();
        }
        
        else if(args.length > 0)
        {
            String input = "";
            for(int i = 0; i < args.length; i++)
            {
                if(i == 0)
                    input += args[i];
                else
                    input += " " + args[i];
            }
            
            Radio.loadRadio(input, e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
    
    }
}
