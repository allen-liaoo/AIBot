/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import command.Command;
import constants.Emoji;
import Setting.Prefix;
import utility.SearchResult;
import utility.Search;
import utility.WebScraper;
import system.AILogger;
import utility.UtilBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class LyricsCommand extends Command{

    public final static  String HELP = "This command is showing the lyrics of a song.\n"
                                     + "command Usage: `"+ Prefix.getDefaultPrefix() +"lyrics`\n"
                                     + "Parameter: `-h | [Artist Name] [Song Name] |null`\n"
                                     + "[Artist Name] [Song Name]: The exact name of the artist(s) and the song.";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Lyrics -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length > 0)
        {
            List<SearchResult> results = new ArrayList<SearchResult>();
            //Get input
            String input = "";
            for(String i : args) {input+=i + " ";}
            
            //Search Lyrics
            try {
                results = Search.lyricsSearch(input);
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "IO Exception");
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
                embedly.setColor(UtilBot.randomColor());
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
                e.getChannel().sendMessage(Emoji.ERROR + " No result.").queue();
                AILogger.errorLog(ioobe, e, this.getClass().getName(), "Lyrics Search, no result- " + input);
            } catch (IOException ioe) {
                AILogger.errorLog(ioe, e, this.getClass().getName(), "Unknown Cause");
            }
        }
    }

    
}
