/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.Command;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class SupportCommand extends Command{
    public final static  String HELP = "This command is for supporting the bot.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"support`\n"
                                     + "Parameter: `-h | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Support -Help", HELP, true);
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
            String msg = Emoji.INVITE + " Join this server for music!\n"
                                        + Constants.L_MUSIC_HUB;
            
            e.getChannel().sendMessage(msg).queue();
        }
    }

    
}
