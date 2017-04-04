/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.Command;
import Config.Info;
import Config.Prefix;
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
public class AboutCommand implements Command{

    public final static  String HELP = "This command is for getting the bot's discription.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"about`\n"
                                     + "Parameter: `-h | null`";
    
    public final static String BOT_DES = "The AIBot that started in the March of 2017 for music and fun commands.\n"
                                    + "**Developer:** Ayy™#3103 *aka* AlienIdeology, Allen Liao\n"
                                    + "**Version:** 0.1.2\n"
                                    + "**Annoucement:** Working on Music Module~~\n"
                                    + "**Latest Changes:** Reconstruct Help Message\n"
                                    + "**Github Link:** https://github.com/AlienIdeology/AIBot";
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embeddes = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.addField("About -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
            e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) 
        {
            embeddes.setColor(Color.red);
            embeddes.setAuthor("What is AIBot?", Info.B_URL, Info.I_info);
            embeddes.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
            embeddes.setDescription(BOT_DES);
            embeddes.setFooter("Request by " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), e.getAuthor().getEffectiveAvatarUrl());
            embeddes.setTimestamp(Instant.now());

            MessageEmbed medes = embeddes.build();
            
            e.getChannel().sendMessage(medes).queue();
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
