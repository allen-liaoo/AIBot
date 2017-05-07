/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package command.music;

import audio.Radio;
import command.Command;
import constants.Global;
import setting.Prefix;
import utility.UtilBot;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class RadioCommand extends Command{

    public final static String HELP = "This command is for loading an automatic playlist.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"radio`\n"
                                    + "Parameter: `-h | [Station Name] | null`\n"
                                    + "[Station Name]: Load the radio station and start playing.\n"
                                    + "null: Get a list of available radio stations.\n";
    
    private EmbedBuilder embedpl = new EmbedBuilder();
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("FM -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            String stations = Radio.getStations();
            
            embedpl.setAuthor("AIBot Radio", null, Global.B_AVATAR);
            embedpl.setDescription("Usage: `" + Prefix.DIF_PREFIX + "radio [Station Name]`\n");
            embedpl.addField("Available Radio Stations", stations, true);
            embedpl.setThumbnail(Global.B_AVATAR);
            embedpl.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
            embedpl.setColor(UtilBot.randomColor());
            embedpl.setTimestamp(Instant.now());
            
            e.getChannel().sendMessage(embedpl.build()).queue();
            embedpl.clearFields();
        }
        
        else if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
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

}
