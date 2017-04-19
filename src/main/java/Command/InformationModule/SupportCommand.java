/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class SupportCommand implements Command{
    public final static  String HELP = "This command is for supporting the bot.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"support`\n"
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
        embed.addField("Support -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());
        
        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) 
        {
            String msg = Emoji.invite + " Join this server for music!\n"
                                        + Constants.L_MUSIC_HUB;
            
            e.getChannel().sendMessage(msg).queue();
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
