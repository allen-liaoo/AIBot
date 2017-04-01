/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Setted to SUPPORT PRIVATE CHANNEL.

package Command;

import Config.*;
import Main.*;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.Color;
import java.time.Instant;

/**
 *
 * @author liaoyilin
 */
public class InviteCommand implements Command {

    public final static  String HELP = "This command is for inviting the bot to your own server.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"invite`\n"
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
        embed.addField("Invite -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        String msg = Emoji.E_invite + " Invite me to your server here:\n"
                + "https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567\n"
                + "You can also join my Discord Server if you require support here: https://discord.gg/EABc8Kc";
        
        if(args.length == 0) 
        {
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
