/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.InformationModule;

import Command.Command;
import Constants.Constants;
import Constants.Emoji;
import Setting.Prefix;
import Utility.AIPages;
import Utility.UtilBot;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ListCommand implements Command {

    public final static String HELP = "This command is for getting a list of servers, members or other entities.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"list`\n"
                                    + "Parameter: `-h | server | member | role | channel | null`\n"
                                    + "server: Get a list of servers this bot is in. *\n"
                                    + "member: Get a list of members in this server.\n"
                                    + "role: Get a lost of roles in this server.\n"
                                    + "channel: Get a list of text and voice channels in this server.\n";
    private final EmbedBuilder embed = new EmbedBuilder();
            

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.addField("List -Help", HELP, true);
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
        
        else if (args.length > 0) {
            try {
                if("member".equals(args[0]) || "mem".equals(args[0]))
                    listMember(args,e);
                else if ("server".equals(args[0]) || "servers".equals(args[0]) || "guild".equals(args[0]))
                    listServer(args,e);
                else if ("role".equals(args[0]) || "roles".equals(args[0]))
                    listRole(args,e);
                else if ("channel".equals(args[0]) || "channels".equals(args[0]))
                    listChannel(args,e);
            } catch (IllegalArgumentException  | IndexOutOfBoundsException ex) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid page number.").queue();
                return;
            }
        }
    }
    
    public void listServer(String[] args, MessageReceivedEvent e)
    {
        List<Guild> guildsList = UtilBot.getServerList();
        AIPages pages = new AIPages(guildsList, 10);

        int page = 1;
        if(args.length > 1)
            page = Integer.parseInt(args[1]);

        List<Guild> guilds = pages.getPage(page);
        String output = "```md\n\n[Server List](Total: " + guildsList.size() + ")\n\n";

        int index = (page-1) * 10+1;
        for(int i = 0; i < guilds.size(); i++) {
            output += (i+index) + ". " + guilds.get(i).getName() + "  <Members: " + guilds.get(i).getMembers().size() + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "   (Sorted by the amount of members)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list server [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
    public void listMember(String[] args, MessageReceivedEvent e) 
    {
        List<Member> memsList = UtilBot.getMemberList(e);
        AIPages pages = new AIPages(memsList, 10);
        
        int page = 1;
        if(args.length > 1)
            page = Integer.parseInt(args[1]);

        List<Member> members = pages.getPage(page);
        String output = "```md\n\n[Member List](Total: " + memsList.size() + ")\n\n";

        int index = (page-1) * 10+1;
        for(int i = 0; i < members.size(); i++) {
            Member mem = members.get(i);
            String role = mem.getRoles().isEmpty() ? mem.getGuild().getPublicRole().getName() : members.get(i).getRoles().get(0).getName();
            output += (i+index) + ". " + mem.getEffectiveName() + " #" + mem.getUser().getDiscriminator()
                   + " <Role: " + role + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by role position and alphabetical order)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list member [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
    public void listRole(String[] args, MessageReceivedEvent e) 
    {
        List<Role> roleList = UtilBot.getRoleList(e);

        AIPages pages = new AIPages(roleList, 10);
        int page = 1;
        if(args.length > 1)
            page = Integer.parseInt(args[1]);

        List<Role> roles = pages.getPage(page);
        String output = "```md\n\n[Role List](Total: " + roleList.size() + ")\n\n";
        
        int index = (page-1) * pages.getPageSize()+1;
        for(int i = 0; i < roles.size(); i++) {
            if(!roles.get(i).isPublicRole()) {
                output += (i+index) + ". " + roles.get(i).getName();
                output += roles.get(i).getColor() == null ?  " <Color: None>\n\n" : " <Color: " + UtilBot.getHexCode(roles.get(i).getColor()) + ">\n\n";
            }
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by role position)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list role [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
    public void listChannel(String[] args, MessageReceivedEvent e) 
    {
        List<Channel> tcList = UtilBot.getTextChannelList(e);
        List<Channel> vcList = UtilBot.getVoiceChannelList(e);
        List<Channel> chanList = new ArrayList();
        chanList.addAll(tcList);
        chanList.addAll(vcList);

        AIPages pages = new AIPages(chanList, 10);
        int page = 1;
        if(args.length > 1)
            page = Integer.parseInt(args[1]);

        List<Channel> chans = pages.getPage(page);
        String output = "```md\n\n[Text Channel List](Total: " + chanList.size() + ")\n\n";
        
        int index = (page-1) * pages.getPageSize()+1;
        for(int i = 0; i < chans.size(); i++) {
            output += (i+index) + ". " + chans.get(i).getName() + " <Members: " + chans.get(i).getMembers().size() + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by channel position)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list channel [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
}
