/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.utility;

import command.Command;
import constants.Emoji;
import setting.Prefix;
import utility.SearchResult;
import java.io.IOException;

import utility.Search;
import system.AILogger;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */

public class SearchCommand extends Command{
    public final static  String HELP = "This command is for searching the ~~dark~~ web.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"search` or `"+ Prefix.getDefaultPrefix() +"google` or `"+ Prefix.getDefaultPrefix() +"wiki`  `"+ Prefix.getDefaultPrefix() +"urban` or  `"+ Prefix.getDefaultPrefix() +"github`\n"
                                     + "Parameter: `-h | (For search) [Custom Search Site] [Keywords] | (For google, wiki, urban, github) [Keywords] | null`\n"
                                     + "(For search) [Custom Search Domain] [Keywords]: Search a custom site with [Keywords], i.e. `"+ Prefix.getDefaultPrefix() +"search dictionary.com artificial intelligence`\n\n"
                                     + "Related/Alter Commads:\n"
                                     + "**google (g)** - Search via Google Search Engine.\n"
                                     + "**wiki** - Search Wikipedia.\n"
                                     + "**urban** - Search Urban Dictionary.\n"
                                     + "**github (git)** - Search Github.";
    private String num = "1";
    private String site = "&as_sitesearch=";
    
    public SearchCommand(String invoke)
    {
        if("search".equals(invoke)) site = "&as_sitesearch=";
        else if("google".equals(invoke))   site = "";
        else if("wiki".equals(invoke)) site += "wikipedia.org";
        else if("git".equals(invoke)) site += "github.com";
        else if("ub".equals(invoke)) site += "urbandictionary.com";
    }
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("utility Module", null);
        embed.addField("Search -Help", HELP, true);
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
            AILogger.commandLog(e, "SearchCommand", "Called");
            
            try {
                if("&as_sitesearch=".equals(site) && args.length >= 2) //Custom Site Search
                {
                    System.out.println("Custom Search");
                    
                    site = args[0];
                    String input = "";
                    for(int i = 0; i < args.length; i++){
                        if(i != 0) 
                            input += args[i] + " ";
                    }
                    
                    final String tempString = Emoji.SEARCH + " This is the result for `" + input + "` on `" + site + "`:";
                    e.getChannel().sendMessage("Searching........").complete().editMessage(tempString).complete();
                    
                    List<SearchResult> result = Search.search(site, num, input);
                    e.getChannel().sendMessage("**" + result.get(0).getTitle() + "**\n" + result.get(0).getLink()).queue();
                }
                
                else if ("&as_sitesearch=".equals(site) && args.length <= 1) //Custom Site Search without site or keyword
                {
                    e.getChannel().sendMessage(Emoji.ERROR + " Please enter a custom site.").queue();
                }
                
                else if(!"&as_sitesearch=".equals(site)) //Google, wiki, urban, github
                {
                    System.out.println("Normal Search");
                    String input = "";
                    for(int i = 0; i < args.length; i++){ input += args[i] + " "; }
                    
                    
                    final String tempString = Emoji.SEARCH + " This is the result for `" + input + "` via `Google Search Engine" + "`:";
                    e.getChannel().sendMessage("Searching........").complete().editMessage(tempString).complete();
                    
                    List<SearchResult> result = Search.search(site, num, input);
                    e.getChannel().sendMessage("**" + result.get(0).getTitle() + "**\n" + result.get(0).getLink()).queue();
                }
                
            } catch (IOException ex) {
                AILogger.errorLog(ex, e, this.getClass().getName(), "IO Exception");
            } catch (IndexOutOfBoundsException iobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " No result.").queue();
                AILogger.errorLog(iobe, e, this.getClass().getName(), "Web Search \""+ args[0] +"\" No Result.");
            }
        }
        
    }

    
}
