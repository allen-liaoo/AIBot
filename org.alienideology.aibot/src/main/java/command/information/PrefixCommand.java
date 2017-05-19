/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import constants.Emoji;
import setting.Prefix;
import constants.Global;
import command.Command;
import net.dv8tion.jda.core.EmbedBuilder;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class PrefixCommand extends Command {
    public final static String HELP = "This command is for setting the prefix.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() + "prefix`\n"
                                    + "Parameter: `-h | Prefix`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Prefix -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0) {
            e.getChannel().sendMessage("Current prefix: `" + Prefix.getDefaultPrefix() + "`").queue();
        }
        
        else {
            //Prefix.setPrefix(args[0], e);
            e.getChannel().sendMessage(Emoji.ERROR + " setting Prefix is not supported.").queue();
        }
            
    }

    
}
