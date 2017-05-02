/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Setting.Prefix;
import Audio.*;
import Main.Main;
import Command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class LeaveCommand extends Command{

    public final static String HELP = "This command is for removing the bot to your current voice channel.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"leave`\n"
                                    + "Parameter: `-h | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Leave -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE) {
            Main.getGuild(e.getGuild()).getPlayer().setPaused(true);
            AudioConnection.disconnect(e, true);
        } else if (e.getChannelType() == e.getChannelType().PRIVATE) {
            e.getTextChannel(); //Return null pointer :P
        }
    }

    
}
