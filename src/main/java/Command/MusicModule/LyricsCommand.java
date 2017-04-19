/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Utility.SearchResult;
import Utility.Search;
import Utility.UtilTool;
import Utility.WebScraper;
import Utility.SmartLogger;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jsoup.HttpStatusException;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class LyricsCommand implements Command{

    public final static  String HELP = "This command is showing the lyrics of a song.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"lyrics`\n"
                                     + "Parameter: `-h | [Artist Name] [Song Name] |null`\n"
                                     + "[Artist Name] [Song Name]: The exact name of the artist(s) and the song.";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Lyrics -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else
        {
            List<SearchResult> results = new ArrayList<SearchResult>();
            //Get input
            String input = "";
            for(String i : args) {input+=i + " ";}
            
            //Search Lyrics
            try {
                results = Search.lyricsSearch(input);
            } catch (IOException ex) {
                SmartLogger.errorLog(ex, e, this.getClass().getName(), "IO Exception");
            }
            
            //Get Lyrics
            try {
                String[] lyrics = WebScraper.getSongLyrics(results.get(0).getLink());
                
                //Exract lyrics from String[] array to a single string
                String lyricsText = "";
                for(String s : lyrics)
                {
                    lyricsText += s;
                }
                
                //Split strings if the length is more than 1500
                List<String> strings = new ArrayList<String>();
                int index = 0;
                while (index < lyricsText.length()) {
                    strings.add(lyricsText.substring(index, Math.min(index + 1500,lyricsText.length())));
                    index += 1500;
                }
                
                EmbedBuilder embedly = new EmbedBuilder();
                embedly.setColor(UtilTool.randomColor());
                embedly.setTitle(results.get(0).getTitle() + " by " + results.get(0).getAuthor(), results.get(0).getLink());
                embedly.setFooter("From Genius.com", null);
                MessageEmbed mely = embedly.build();
                e.getChannel().sendMessage(mely).queue();
                embedly.clearFields();
                
                for(String out : strings)
                {
                    e.getChannel().sendMessage(out).queue();
                }
                
            } catch (IndexOutOfBoundsException ioobe) {
                e.getChannel().sendMessage(Emoji.error + " No result.").queue();
                SmartLogger.errorLog(ioobe, e, this.getClass().getName(), "Lyrics Search, no result- " + input);
            } catch (IOException ioe) {
                SmartLogger.errorLog(ioe, e, this.getClass().getName(), "Unknown Cause");
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
