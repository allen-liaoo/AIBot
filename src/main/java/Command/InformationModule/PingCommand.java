/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

//Set to SUPPORT PRIVATE CHANNEL.

import Constants.Emoji;
import Setting.Prefix;
import Constants.Constants;
import Command.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;

        
/**
 *
 * @author liaoyilin
 */
public class PingCommand extends Command {

    public final static String HELP = "This command is for Pong.\n"
                             + "Command Usage: `" + Prefix.getDefaultPrefix() + "ping`\n"
                             + "Parameter: `-h | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Ping -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
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
            long ping = e.getMessage().getJDA().getPing();
            
            String respond = Emoji.PING + " Pong.\n";
            String respond2 = "Current ping `" + ping + "` ms";
            e.getChannel().sendMessage(respond+respond2).queue();
        }
    }

    
}
