/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.UtilityModule;

import Command.Command;
import static Command.UtilityModule.SearchCommand.HELP;
import Resource.Emoji;
import Resource.Info;
import Resource.Prefix;
import Resource.Search;
import Resource.SearchResult;
import Resource.WebScraper;
import Setting.SmartLogger;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class IMDbCommand implements Command{

    public final static  String HELP = "This command is for a search result from IMDB.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"imdb`\n"
                                     + "Parameter: `-h | [Keywords] | null`\n"
                                     + "[Keywords]: Search IMDB with [Keywords].\n";

    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.addField("IMDB -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
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
            String input = "";
            for(int i = 0; i < args.length; i++){ input += args[i].substring(0,1).toUpperCase() + args[i].substring(1) + " "; }
            input = input.substring(0, input.length()-1);
            
            try {
                List<SearchResult> results = Search.IMDBSearch(input);
                String titles = "";
                String names = "";
                String characters = "";
                
                //Get Titles, Names, Characters and make the texts
                for(int i = 0; i < results.size(); i ++)
                {
                    SearchResult sr = results.get(i);
                    if("Titles".equals(sr.getText()))
                        titles += "**" + (i+1) + ".** [" + sr.getTitle() + "](" + sr.getLink() + ")\n";
                    else if("Names".equals(sr.getText()))
                        names += "**" + (i+1) + ".** [" + sr.getTitle() + "](" + sr.getLink() + ")\n";
                    else if("Characters".equals(sr.getText()))
                        characters += "**" + (i+1) + ".** [" + sr.getTitle() + "](" + sr.getLink() + ")\n";
                }
                
                //Prevent null Messages
                if(results.isEmpty())
                {
                    e.getChannel().sendMessage(Emoji.error + " No results.").queue();
                    return;
                }
                if("".equals(titles))
                    titles = "None";
                if("".equals(names))
                    names = "None";
                if("".equals(characters))
                    characters = "None\n";
                    
                //Get Thumbnail of the first SearchResult
                WebScraper.getIMDBThumbNail(results.get(0));
                
                //Build EMbed Message
                EmbedBuilder embeds = new EmbedBuilder();
                embeds.setColor(Info.setColor());
                embeds.setTitle("IMDB Search Results for \"" + input + "\"", null);
                embeds.addField("Titles", titles, false);
                embeds.addField("Names", names, false);
                embeds.addField("Characters", characters + "\n[Click here for more results...](" + 
                        "http://www.imdb.com/find?q=" + input.replaceAll(" ", "+") + ")\n", false);
                embeds.setThumbnail(results.get(0).getThumbnail());
                embeds.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                embeds.setTimestamp(Instant.now());
                
                final String tempString = Emoji.search + " This is the result for `" + input + "` on `IMDB.com`:";
                e.getChannel().sendMessage("Searching........").complete().editMessage(embeds.build()).complete();
                
                //Reset
                embeds.clearFields();
                titles = "";
                names = "";
                characters = "";
                
            } catch (IOException ex) {
                SmartLogger.errorLog(ex, e, this.getClass().getName(), "Input is " + input);
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
}
