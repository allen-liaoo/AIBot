/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.Lyrics;
import Command.Command;
import Resource.Info;
import Resource.SearchResult;
import Setting.SmartLogger;
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

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Lyrics -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else
        {
            //Get input
            String input = "";
            for(String i : args) {input+=i + " ";}
            
            try {
                SearchResult lyric = Lyrics.getSongLyrics(input);
                List<String> lyrics = lyric.getLyrics();
                
                //Get lyrics
                String lyricsText = "";
                for(String s : lyrics)
                {
                    //Ignore line breaks
                    if(s.equals("<br>"))
                        continue;
                    //Delete " " infront of the line, if any
                    else if(s.startsWith(" "))
                        s = s.replaceFirst(" ", "");
                    
                    lyricsText += s + "\n";
                }
                
                //Delete multiple lines breaks
                lyricsText = lyricsText.replaceAll("[\r\n]+", "\n");
                
                //Split strings if the length os more than 1500
                List<String> strings = new ArrayList<String>();
                int index = 0;
                while (index < lyricsText.length()) {
                    strings.add(lyricsText.substring(index, Math.min(index + 1500,lyricsText.length())));
                    index += 1500;
                }
                
                EmbedBuilder embedly = new EmbedBuilder();
                embedly.setTitle("Lyrics for " + lyric.getTitle(), lyric.getLink());
                embedly.setFooter("Source: Genius.com", null);
                MessageEmbed mely = embedly.build();
                e.getChannel().sendMessage(mely).queue();
                embedly.clearFields();
                
                for(String out : strings)
                {
                    e.getChannel().sendMessage(out).queue();
                }
                
            } catch (HttpStatusException hse) {
                SmartLogger.errorLog(hse, e.getGuild().getName(), this.getClass().getName(), "Invalid Lyrics Name");
            } catch (IOException ioe) {
                SmartLogger.errorLog(ioe, e.getGuild().getName(), this.getClass().getName(), "Unknown Cause");
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
