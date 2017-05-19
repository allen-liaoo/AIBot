/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.Command;
import constants.Global;
import setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
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
                                    + "**Announcement:** Working on Eval and Soft Ban~~\n"
                                    + "**Latest Changes:** music- AutoPlay\n"
                                    + "**Github Link:** " + Global.B_GITHUB;
    

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
            EmbedBuilder embeds = new EmbedBuilder();
            embeds.setColor(Color.red).setTimestamp(Instant.now()).setThumbnail(Global.B_AVATAR);
            embeds.setAuthor("What is " + Global.B_NAME + "?", Global.B_DISCORD_BOT, Global.I_INFO);
            embeds.setDescription(BOT_DES);
            embeds.setFooter("Request by " + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), e.getAuthor().getEffectiveAvatarUrl());
            e.getChannel().sendMessage(embeds.build()).queue();
        }
    }

    
}
