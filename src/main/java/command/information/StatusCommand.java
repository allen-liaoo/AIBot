/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.*;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import main.AIBot;
import utility.UtilBot;
import utility.UtilString;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class StatusCommand extends Command{

    public final static String HELP = "This command is for getting this bot's status.\n"
                              + "Command Usage: `" + Prefix.getDefaultPrefix() + "status` or `" + Prefix.getDefaultPrefix() + "uptime`\n"
                              + "Parameter: `-h | null`";
    
    private EmbedBuilder embedstatus = new EmbedBuilder();
    private String type = "";
    
    public StatusCommand(String invoke)
    {
        if("status".equals(invoke)) type = "status";
        else if("uptime".equals(invoke)) type = "uptime";
    }
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Status -Help", HELP, true);
        embed.setFooter("command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0)
        {
            if("uptime".equals(type))
            {
                String uptime = UtilString.formatTime(System.currentTimeMillis() - AIBot.timeStart);
                e.getChannel().sendMessage(Emoji.STOPWATCH + " AIBot has been up for: " + uptime).queue();
            }

            else if("status".equals(type))
            {
                e.getChannel().sendMessage(UtilBot.postStatus(e.getJDA()).build()).queue();
            }
        }
    }
    
}
