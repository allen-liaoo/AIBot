/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Audio.FM;
import Command.Command;
import static Command.Command.embed;
import static Command.MusicModule.PlayCommand.HELP;
import Resource.Info;
import Resource.Prefix;
import Resource.UtilTool;
import Setting.SmartLogger;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.Color;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class FMCommand implements Command{

    public final static String HELP = "This command is for loading an automatic playlist.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"fm`\n"
                                    + "Parameter: `-h | [Playlist Name] | null`\n"
                                    + "[Playlist Name]: Load the playlist and play sogns randomly.\n"
                                    + "null: Get a list of available playlists.\n";
    
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
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            try {
                String[] libraries = FM.getLibrary();
                
                String dfm = "";
                
                for(String l : libraries)
                {
                    dfm += l + ", ";
                }
                
                embedpl.setAuthor("AIBot FM", FM.FM_base_url, Info.B_AVATAR);
                embedpl.setDescription("Usage: `" + Prefix.DIF_PREFIX + "fm <Playlist Name>`\n");
                embedpl.addField("Discord FM", dfm, true);
                embedpl.addField("Local Playlists", "Nothing yet", true);
                embedpl.setThumbnail(Info.B_AVATAR);
                embedpl.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                embedpl.setColor(UtilTool.setColor());
                embedpl.setTimestamp(Instant.now());
                
                e.getChannel().sendMessage(embedpl.build()).queue();
                embedpl.clearFields();
            } catch (UnirestException ex) {
                SmartLogger.errorLog(ex, e, this.getClass().getName(), "UnirestException when getting libraries");
            }
        }
        
        else if(args.length > 0)
        {
            String input = "";
            for(int i = 0; i < args.length; i++)
            {
                if(i == 0)
                    input += args[i];
                else
                     input += args[i] + " ";
            }
            
            try {
                FM.loadFm(input, e);
            } catch (UnirestException ex) {
                SmartLogger.errorLog(ex, e, this.getClass().getName(), "UnirestException when loading FM");
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
