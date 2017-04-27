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
import Constants.Constants;
import Setting.Prefix;
import Utility.UtilNum;
import AISystem.AILogger;
import Utility.UtilBot;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
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
                                    + "[Playlist Name]: Load the playlist and play songs randomly.\n"
                                    + "null: Get a list of available playlists.\n";
    
    private EmbedBuilder embedpl = new EmbedBuilder();
    

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
            try {
                String[] libraries = FM.getLibrary();
                
                String dfm = "";
                
                for(String l : libraries)
                {
                    dfm += l + ", ";
                }
                dfm = dfm.substring(0, dfm.length() - 2);
                
                String localLib = FM.getLocalLibrary();
                
                embedpl.setAuthor("AIBot FM", FM.FM_base_url, Constants.B_AVATAR);
                embedpl.setDescription("Usage: `" + Prefix.DIF_PREFIX + "fm [Playlist Name]`\n");
                embedpl.addField("Discord FM", dfm, true);
                embedpl.addField("Local Playlists", localLib, true);
                embedpl.setThumbnail(Constants.B_AVATAR);
                embedpl.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                embedpl.setColor(UtilBot.randomColor());
                embedpl.setTimestamp(Instant.now());
                
                e.getChannel().sendMessage(embedpl.build()).queue();
                embedpl.clearFields();
            } catch (UnirestException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "UnirestException when getting libraries");
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "IOException when getting libraries");
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
                     input += " " + args[i];
            }
            
            try {
                FM.loadFm(input, e);
            } catch (UnirestException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "UnirestException when loading FM");
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "IoException when loading FM");
            }
        }
    }

    
}
