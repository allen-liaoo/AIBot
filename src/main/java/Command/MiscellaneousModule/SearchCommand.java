/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MiscellaneousModule;

import Command.Command;
import Config.Emoji;
import Config.Info;
import Config.Prefix;
import java.io.IOException;

import Main.*;
import Config.Web;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */

public class SearchCommand implements Command{
    public final static  String HELP = "This command is for searching the ~~dark~~ web.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"search` or `"+ Prefix.getDefaultPrefix() +"google` or `"+ Prefix.getDefaultPrefix() +"wiki`  `"+ Prefix.getDefaultPrefix() +"urban` or  `"+ Prefix.getDefaultPrefix() +"github`\n"
                                     + "Parameter: `-h | (For search) [Custom Search Site] [Keywords] | (For google, wiki, urban, github) [Keywords] | null`\n"
                                     + "(For search) [Custom Search Domain] [Keywords]: Search a custom site with [Keywords], i.e. `"+ Prefix.getDefaultPrefix() +"search dictionary.com artificial intelligence`\n\n"
                                     + "Related/Alter Commads:\n"
                                     + "**google (g)** - Search via Google Search Engine.\n"
                                     + "**wiki** - Search Wikipedia.\n"
                                     + "**urban** - Search Urban Dictionary.\n"
                                     + "**github (git)** - Search Github.";
    private final EmbedBuilder embed = new EmbedBuilder();
    private String num = "&num=1";
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
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Search -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
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
            try {
                if("&as_sitesearch=".equals(site) && args.length >= 2) //Custom Site Search
                {
                    System.out.println("Custom Search");
                    
                    site += args[0];
                    String input = "";
                    for(int i = 0; i < args.length; i++){
                        if(i != 0) 
                            input += args[i] + " ";
                    }
                    
                    Web.searchSite(site, num, input, e);
                }
                
                else if ("&as_sitesearch=".equals(site) && args.length <= 1) //Custom Site Search without site or keyword
                {
                    e.getChannel().sendMessage(Emoji.error + " Please enter a custom site.").queue();
                }
                
                else if(!"&as_sitesearch=".equals(site)) //Google, wiki, urban, github
                {
                    System.out.println("Normal Search");
                    String input = "";
                    for(int i = 0; i < args.length; i++){ input += args[i] + " "; }
                    
                    Web.searchSite(site, num, input, e);
                }
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
