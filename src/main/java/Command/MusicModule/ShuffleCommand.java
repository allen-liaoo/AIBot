/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Audio.Music;
import Command.Command;
import static Command.Command.embed;
import Resource.Constants;
import Resource.Emoji;
import Setting.Prefix;
import Utility.UtilBot;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ShuffleCommand implements Command {
    public final static  String HELP = "Shuffle the queue.\n"
                                     + "Command Usage: `" + Prefix.getDefaultPrefix() +"shuffle`\n"
                                     + "Parameter: `-h | null`\n";    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Shuffle -Help", HELP, true);
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
            if(UtilBot.isMajority(e.getMember()) ||
                e.getMember().isOwner() || 
                e.getMember().hasPermission(Constants.PERM_MOD) ||
                Constants.D_ID.equals(e.getAuthor().getId()))
            {
                Music.shuffle(e);
            }
            else {
                e.getChannel().sendMessage(Emoji.error + " This command is for server owner, bot owner, or "
                + "members with `Administrator` or `Manage Server` permissions only.\n"
                + "You can also shuffle the queue if there is less than 3 members in the voice channel.").queue();
            }
        }
        
        else if(args.length > 0 && "-h".endsWith(args[0])) {
            help(e);
        }
    }
}
