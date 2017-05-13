/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import net.dv8tion.jda.core.entities.ChannelType;
import setting.Prefix;
import audio.*;
import command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class JoinCommand extends Command{

    public final static String HELP = "This command is for adding the bot to your current voice channel.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"join`  or `" + Prefix.getDefaultPrefix() + "summon` or `" + Prefix.getDefaultPrefix() + "j`\n"
                                    + "Parameter: `-h | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Join -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(e.getChannelType() == ChannelType.PRIVATE) {
            throw new NullPointerException();
        }
        else if(args.length == 0) {
            Music.connect(e, true);
        }
    }

    
}
