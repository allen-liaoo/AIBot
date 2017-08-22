/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import command.Command;
import constants.Emoji;
import constants.Global;
import jdk.nashorn.internal.runtime.GlobalConstants;
import setting.Prefix;
import utility.SearchResult;
import utility.Search;
import utility.WebScraper;
import system.AILogger;
import utility.UtilBot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class LyricsCommand extends Command{

    public final static  String HELP = "This command is showing the lyrics of a song.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"lyrics`\n"
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
        if(args.length > 0) {
            List<SearchResult> results = new ArrayList<>();
            /* Get input */
            String input = "";
            for (String i : args) {
                input += i + " ";
            }
            
            /* Search Lyrics */
            try {
                results = Search.lyricsSearch(input);
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "IO Exception");
            }
            
            /* Get Lyrics */
            try {
                SearchResult first = results.get(0);
                String lyrics = WebScraper.getLyrics(first.getLink());

                e.getChannel().sendMessage(buildLyricsEmbed(first.getTitle(), first.getAuthor(), first.getLink(), lyrics).build()).queue();
            } catch (IndexOutOfBoundsException ioobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " No result.").queue();
            } catch (IOException ioe) {
                AILogger.errorLog(ioe, e, this.getClass().getName(), "Unknown Cause");
            }
        }
    }

    private EmbedBuilder buildLyricsEmbed (String title, String author, String link, String lyrics) {
        EmbedBuilder embed = new EmbedBuilder()
            .setColor(UtilBot.randomColor())
            .setFooter("From Genius.com", null)
            .setAuthor(title + " by " + author, link, Global.B_AVATAR);

        /* Only Breakup lyrics that have sections */
        if (Pattern.compile("(\\[|\\{)(.*?)(]|})").matcher(lyrics).find()) {
            Matcher matcher = Pattern.compile("(?<=\\[|\\{)(?s)(.*?)(?=\\[|\\{|$)").matcher(lyrics);
            int stringLength = 0;

            /* Songs with lyrics sections */
            while (matcher.find() && stringLength < 3500) {
                String content = matcher.group();
                stringLength += content.length();

                /* Matches section title after [ or { or anything and before ] or } */
                Matcher titleMatcher = Pattern.compile("(?<=\\[|\\{|)(?s)(.*?)(?=]|})").matcher(content);
                String section = "N/A";
                if (titleMatcher.find())
                    section = titleMatcher.group();

                /* Matches Lyrics after ] or } */
                Matcher lyricMatcher = Pattern.compile("(?<=]|})(?s)(.*?)(?=\\[|\\{|$)").matcher(content);
                String lyric = "N/A";
                if (lyricMatcher.find()) {
                    lyric = lyricMatcher.group();
                    if (lyric.length() > 1024) lyric = lyric.substring(0, 1000) + "...";
                }

                embed.addField(section, lyric, false);
                if (stringLength > 3500) {
                    embed.addField("There are more lyrics...", "Link: **["+title+"]("+link+")**", false);
                }
            }
        } else {
            if (lyrics.length() > 2047) {
                embed.setDescription(lyrics.substring(0, 2047)+"...");
                embed.addField("There are more lyrics...", "Link: **["+title+"]("+link+")**", false);
            } else {
                embed.setDescription(lyrics);
            }
        }

        return embed;
    }
    
}
