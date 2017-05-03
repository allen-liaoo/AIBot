/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Audio.Music;
import Audio.TrackScheduler;
import Command.Command;
import Constants.Emoji;
import Setting.Prefix;
import Utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class RepeatCommand extends Command {
    public final static  String HELP = "Repeat the queued songs.\n"
                                     + "Command Usage: `" + Prefix.getDefaultPrefix() +"repeat` or `" + Prefix.getDefaultPrefix() +"rp`\n"
                                     + "Parameter: `-h | null`\n";    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Repeat -Help", HELP, true);
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
            if(UtilBot.isMajority(e.getMember()) ||
                UtilBot.isMod(e.getMember()))
            {
                if(Music.checkMode(e, TrackScheduler.PlayerMode.REPEAT))
                    Music.repeat(e);
            }
            else {
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                + "members with `Administrator` or `Manage Server` permissions only.\n"
                + "You can also shuffle the queue if there is less than 3 members in the voice channel.").queue();
            }
        }
    }
    
}
