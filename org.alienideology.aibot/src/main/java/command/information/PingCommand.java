/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

//Set to SUPPORT PRIVATE CHANNEL.

import constants.Emoji;
import setting.Prefix;
import constants.Global;
import command.Command;

import net.dv8tion.jda.core.entities.Message;
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
        embed.setFooter("Command Help/Usage", null);
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
            long time = System.currentTimeMillis();
            String respond = Emoji.PING + " Pong.\n";
            e.getChannel().sendMessage(respond).queue((Message m) ->
                    m.editMessage(respond+"Current ping `%d` ms", System.currentTimeMillis() - time).queue());
        }
    }

    
}
