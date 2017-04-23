/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Setting.Prefix;
import Constants.Constants;
import Audio.*;
import Command.Command;
import Main.*;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class LeaveCommand implements Command{

    public final static  String HELP = "This command is for removing the bot to your current voice channel.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"leave` or `" + Prefix.getDefaultPrefix() + "l`\n"
                                     + "Parameter: `-h | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Leave -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE) 
        {
            AudioConnection.disconnect(e, true);
        }
        else if(args.length == 1 && "-h".equals(args[0])) 
        {
            help(e);
        }
    }

    
}
