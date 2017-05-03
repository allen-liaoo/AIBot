/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.UtilityModule;

import Command.Command;
import Constants.Emoji;
import Constants.Global;
import Setting.Prefix;
import Utility.Search;
import Utility.SearchResult;
import Utility.WebScraper;
import AISystem.AILogger;
import Utility.UtilBot;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class IMDbCommand extends Command {

    public final static String HELP = "This command is for a search result from IMDB.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"imdb`\n"
                                    + "Parameter: `-h | [Keywords] | -m [Keywods] |null`\n"
                                    + "[Keywords]: Search IMDB with [Keywords].\n"
                                    + "-m [Keywods]: Get the specific information for a title.\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Utility Module", null);
        embed.addField("IMDB -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        embed.setTimestamp(Instant.now());
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if("-m".equals(args[0]))
        {
            String input = "";
            for(int i = 1; i < args.length; i++){ input += args[i].substring(0,1).toUpperCase() + args[i].substring(1) + " "; }
            input = input.substring(0, input.length()-1);
            
            try {
                SearchResult results = Search.IMDbSearch(input).get(0);
                WebScraper.getIMDbThumbNail(results);
                EmbedBuilder imdbinfo = WebScraper.getIMDbInfo(results);
                imdbinfo.setFooter("Request by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                imdbinfo.setTimestamp(Instant.now());
                
                e.getChannel().sendMessage("Searching........").complete().editMessage(imdbinfo.build()).complete();
                imdbinfo.clearFields();
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "Input is " + input);
            } catch (IndexOutOfBoundsException ioobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " An error occured. Please enter a valid IMDb **title**.").queue();
            }
            
        }
        
        else
        {
            String input = "";
            for(int i = 0; i < args.length; i++){ input += args[i].substring(0,1).toUpperCase() + args[i].substring(1) + " "; }
            input = input.substring(0, input.length()-1);
            
            try {
                List<SearchResult> results = Search.IMDbSearch(input);
                String titles = "";
                String names = "";
                String characters = "";
                
                //Get Titles, Names, Characters and generate the texts
                for(int i = 0; i < results.size(); i ++)
                {
                    SearchResult sr = results.get(i);
                    if(null != sr.getText())
                    switch (sr.getText()) {
                        case "Titles":
                            titles += "**" + (i+1) + ".** [" + sr.getTitle() + "](" + sr.getLink() + ")\n";
                            break;
                        case "Names":
                            names += "**" + (i+1) + ".** [" + sr.getTitle() + "](" + sr.getLink() + ")\n";
                            break;
                        case "Characters":
                            characters += "**" + (i+1) + ".** [" + sr.getTitle() + "](" + sr.getLink() + ")\n";
                            break;
                        default:
                            break;
                    }
                }
                
                //Prevent null Messages
                if(results.isEmpty()) {
                    e.getChannel().sendMessage(Emoji.ERROR + " No results.").queue();
                    return;
                }
                if("".equals(titles))
                    titles = "None";
                if("".equals(names))
                    names = "None";
                if("".equals(characters))
                    characters = "None\n";
                    
                //Get Thumbnail of the first SearchResult
                WebScraper.getIMDbThumbNail(results.get(0));
                
                //Build EMbed Message
                EmbedBuilder embeds = new EmbedBuilder();
                embeds.setColor(UtilBot.randomColor());
                embeds.setAuthor("IMDb Search Results for \"" + input + "\"", 
                        "http://www.imdb.com/find?q=" + input.replaceAll(" ", "+"), null);
                embeds.addField("Titles", titles, false);
                embeds.addField("Names", names, false);
                embeds.addField("Characters", characters + "\n[Click here for more results...](" + 
                        "http://www.imdb.com/find?q=" + input.replaceAll(" ", "+") + ")\n", false);
                embeds.setThumbnail(results.get(0).getThumbnail());
                embeds.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                embeds.setTimestamp(Instant.now());
                
                e.getChannel().sendMessage("Searching........").complete().editMessage(embeds.build()).complete();
                
                //Reset
                embeds.clearFields();
                results.clear();
                titles = "";
                names = "";
                characters = "";
                
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "Input is " + input);
            }
        }
    }

}
