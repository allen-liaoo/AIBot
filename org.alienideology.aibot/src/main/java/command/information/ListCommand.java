/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package command.information;

import command.Command;
import constants.Global;
import constants.Emoji;
import setting.Prefix;
import system.AIPages;
import system.selector.EmojiSelection;
import listener.SelectorListener;
import utility.UtilBot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ListCommand extends Command {

    public final static String HELP = "This command is for getting a list of servers, members or other entities.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"list` or `"+ Prefix.getDefaultPrefix() +"l`\n"
                                    + "Parameter: `-h | server | member | role | channel | null`\n"
                                    + "server: Get a list of servers this bot is in. *\n"
                                    + "member: Get a list of members in this server.\n"
                                    + "role: Get a list of roles in this server.\n"
                                    + "channel: Get a list of text and voice channels in this server.\n";  
    
    private static final List<String> reactions = Arrays.asList(Emoji.LEFT, Emoji.RIGHT);
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("List -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if (args.length > 0) {
            try {
                int page = 1;
                if(null == args[0])
                    e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid list type. "
                            + "`=list server, member, role, or channel`").queue();
                else {
                    if(args.length > 1)
                        page = Integer.parseInt(args[1]);
                }
                
                switch (args[0]) {
                    case "member":
                    case "mem":
                        listMember(e,page);
                        break;
                    case "server":
                    case "servers":
                    case "guild":
                        listServer(e,page);
                        break;
                    case "role":
                    case "roles":
                        listRole(e,page);
                        break;
                    case "channel":
                    case "channels":
                        listChannel(e,page);
                        break;
                    default:
                        e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid list type. "
                                + "`=list server, member, role, or channel`").queue();
                        break;
                }
            } catch (IllegalArgumentException  | IndexOutOfBoundsException ex) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid page number.").queue();
                return;
            }
        }
    }
    
    public void listServer(MessageReceivedEvent e, int page)
    {
        List<Guild> guildsList = UtilBot.getServerList();
        AIPages pages = new AIPages(guildsList, 10);

        List<Guild> guilds = pages.getPage(page);
        String output = "```md\n\n[Server List](Total: " + guildsList.size() + ")\n\n";

        int index = (page-1) * 10+1;
        for(int i = 0; i < guilds.size(); i++) {
            output += (i+index) + ". " + guilds.get(i).getName() + "  <Members: " + guilds.get(i).getMembers().size() + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "   (Sorted by the amount of members)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list server [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue( (Message msg) -> {
            SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                @Override
                public void action(int chose) {
                    switch(chose) {
                        case 0:
                            ListCommand lc = new ListCommand();
                            lc.action(new String[]{"server",(page-1)+""}, e);
                            break;
                        case 1:
                            ListCommand lc2 = new ListCommand();
                            lc2.action(new String[]{"server",(page+1)+""}, e);
                            break;
                        default:
                            break;
                    }
                    UtilBot.deleteMessage(msg);
                }
            });
        });
    }
    
    public void listMember(MessageReceivedEvent e, int page)
    {
        List<Member> memsList = UtilBot.getMemberList(e);
        AIPages pages = new AIPages(memsList, 10);

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
        e.getChannel().sendMessage(output).queue( (Message msg) -> {
            SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                @Override
                public void action(int chose) {
                    switch(chose) {
                        case 0:
                            ListCommand lc = new ListCommand();
                            lc.action(new String[]{"member",(page-1)+""},e);
                            break;
                        case 1:
                            ListCommand lc2 = new ListCommand();
                            lc2.action(new String[]{"member",(page+1)+""},e);
                            break;
                        default:
                            break;
                    }
                    UtilBot.deleteMessage(msg);
                }
            });
        });
    }
    
    public void listRole(MessageReceivedEvent e, int page)
    {
        List<Role> roleList = UtilBot.getRoleList(e);
        AIPages pages = new AIPages(roleList, 10);

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
        e.getChannel().sendMessage(output).queue( (Message msg) -> {
            SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                @Override
                public void action(int chose) {
                    switch(chose) {
                        case 0:
                            ListCommand lc = new ListCommand();
                            lc.action(new String[]{"role",(page-1)+""},e);
                            break;
                        case 1:
                            ListCommand lc2 = new ListCommand();
                            lc2.action(new String[]{"role",(page+1)+""},e);
                            break;
                        default:
                            break;
                    }
                    UtilBot.deleteMessage(msg);
                }
            });
        });
    }
    
    public void listChannel(MessageReceivedEvent e, int page)
    {
        List<Channel> tcList = UtilBot.getTextChannelList(e);
        List<Channel> vcList = UtilBot.getVoiceChannelList(e);
        List<Channel> chanList = new ArrayList();
        chanList.addAll(tcList);
        chanList.addAll(vcList);

        AIPages pages = new AIPages(chanList, 10);

        List<Channel> chans = pages.getPage(page);
        String output = "```md\n\n[Channel List](Total: " + chanList.size() + ")\n\n";
        
        int index = (page-1) * pages.getPageSize()+1;
        for(int i = 0; i < chans.size(); i++) {
            if(i==0 && page == 1)
                output += "/* Text Channel(s): " + tcList.size() + " *\n\n";
            if(i==tcList.size()-((page-1)*10))
                output += "/* Voice Channel(s): " + vcList.size() + " *\n\n";
            
            output += (i+index) + ". " + chans.get(i).getName() + " <Members: " + chans.get(i).getMembers().size() + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by channel position)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list channel [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue( (Message msg) -> {
            SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                @Override
                public void action(int chose) {
                    switch(chose) {
                        case 0:
                            ListCommand lc = new ListCommand();
                            lc.action(new String[]{"channel",(page-1)+""},e);
                            break;
                        case 1:
                            ListCommand lc2 = new ListCommand();
                            lc2.action(new String[]{"channel",(page+1)+""},e);
                            break;
                        default:
                            break;
                    }
                    UtilBot.deleteMessage(msg);
                }
            });
        });
    }
    
}
