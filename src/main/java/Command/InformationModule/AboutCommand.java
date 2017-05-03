/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.Command;
import Constants.Global;
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
public class AboutCommand extends Command{

    public final static  String HELP = "This command is for getting the bot's description.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"about`\n"
                                     + "Parameter: `-h | null`";
    
    public final static String BOT_DES = "The AIBot that started in the March of 2017 for music and fun commands.\n"
                                    + "**Developer:** Ayy™#3103 *aka* AlienIdeology\n"
                                    + "**Version:** " + Global.VERSION +"\n"
                                    + "**Annoucement:** Working on Eval, Dump, Shuffle, Repeat and Softban~~\n"
                                    + "**Latest Changes:** LogCommand and Server Join Message\n"
                                    + "**Github Link:** " + Global.B_GITHUB;
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embeddes = new EmbedBuilder();
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("About -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
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
            embeddes.setColor(Color.red);
            embeddes.setAuthor("What is " + Global.B_NAME + "?", Global.B_DISCORD_BOT, Global.I_INFO);
            embeddes.setThumbnail(Global.B_AVATAR);
            embeddes.setDescription(BOT_DES);
            embeddes.setFooter("Request by " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), e.getAuthor().getEffectiveAvatarUrl());
            embeddes.setTimestamp(Instant.now());

            MessageEmbed medes = embeddes.build();
            
            e.getChannel().sendMessage(medes).queue();
        }
    }

    
}
