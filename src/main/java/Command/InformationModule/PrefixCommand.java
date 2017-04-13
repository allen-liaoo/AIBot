/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Resource.Emoji;
import Setting.Prefix;
import Resource.Info;
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
public class PrefixCommand implements Command {
    public final static String HELP = "This command is for setting the prefix.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() + "prefix`\n"
                                    + "Parameter: `-h | Prefix`";
    
    private final EmbedBuilder embed = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.setTitle("Prefix -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE)
        {
            e.getChannel().sendMessage("Current prefix: `" + Prefix.getDefaultPrefix() + "`").queue();
        }
        else if("-h".equals(args[0])) 
        {
            help(e);
        }
        else 
        {
            //Prefix.setPrefix(args[0], e);
            e.getChannel().sendMessage(Emoji.error + " Setting Prefix is not supported.").queue();
        }
            
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
