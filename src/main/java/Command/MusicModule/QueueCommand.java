/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.Music;
import Command.Command;
import Constants.Constants;
import Constants.Emoji;
import Setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class QueueCommand extends Command{
    public final static String HELP = "This command is getting a list of queued songs\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"queue`\n"
                                    + "Parameter: `-h | [Number] | null`\n"
                                    + "[Number]: Page number of the queue.\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Queue -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        else
        {
            try {
                int page = 1;
                if(args.length != 0)
                    page = Integer.parseInt(args[0]);
                Music.queueList(e, page);
            } catch (IllegalArgumentException  | IndexOutOfBoundsException ex) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " Please enter a valid page number.").queue();
                return;
            }
        }
    }

    
}
