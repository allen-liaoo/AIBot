/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

//Setted to SUPPORT PRIVATE CHANNEL.

import Command.*;
import Config.*;
import Main.*;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.Color;
import java.time.Instant;
        
/**
 *
 * @author liaoyilin
 */
public class PingCommand implements Command {

    public final static String HELP = "This command is for Pong.\n"
                             + "Command Usage: `" + Prefix.getDefaultPrefix() + "ping`\n"
                             + "Parameter: `-h | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.addField("Ping -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) 
        {
            long ping = e.getMessage().getJDA().getPing();
            
            String respond = Emoji.E_ping + " Pong.\n";
            String respond2 = "Current ping `" + ping + "` ms";
            e.getChannel().sendMessage(respond).queue();
            e.getChannel().sendMessage(respond2).queue();
        }
        else if("-h".equals(args[0]))
        {
            help(e);      
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
    }
    
}
