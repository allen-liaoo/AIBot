/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.InformationModule;

import Command.Command;
import Resource.Constants;
import Resource.Emoji;
import Setting.Prefix;
import Utility.AIPages;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ServerCommand implements Command {

    public final static  String HELP = "This command is for getting a list of servers this bot is in.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"server`\n"
                                     + "Parameter: `-h | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
            

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.addField("Server -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0])) {
            help(e);
        }
        
        else {
            List<Guild> guildsList = new ArrayList(e.getJDA().getGuilds());
            //Sort List
            Collections.sort(guildsList, (Guild g1, Guild g2) -> 
                    g1.getMembers().size() > g2.getMembers().size() ? -1 : (g1.getMembers().size() < g2.getMembers().size() ) ? 1 : 0 );
            
            AIPages pages = new AIPages(guildsList, 10);
            
            try {
                int page = 1;
                if(args.length != 0)
                    page = Integer.parseInt(args[0]);
                
                List<Guild> guilds = pages.getPage(page);
                String output = "```md\n\n[Server List](Total: " + guildsList.size() + ")\n\n";
                
                int index = (page-1) * 10+1;
                for(int i = 0; i < guilds.size(); i++) {
                    output += (i+index) + ". " + guilds.get(i).getName() + "  <Members: " + guilds.get(i).getMembers().size() + ">\n\n";
                }
                
                output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "\n\n"
                        + "# Use " + Prefix.getDefaultPrefix() + "server [Page Number] to show more pages.```";
                e.getChannel().sendMessage(output).queue();
            } catch (IllegalArgumentException  | IndexOutOfBoundsException ex) {
                e.getChannel().sendMessage(Emoji.error + " Please enter a valid page number between 1 and " + pages.getPages() +".").queue();
                return;
            }
        }
    }

    
}
