/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Audio.Music;
import Audio.PlayerMode;
import Audio.TrackScheduler;
import Command.Command;
import Constants.Emoji;
import Main.Main;
import Setting.Prefix;
import Utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class RepeatCommand extends Command {
    public final static String HELP = "Repeat the queued songs or current track.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() +"repeat` or `" + Prefix.getDefaultPrefix() +"rp`\n"
                                    + "Parameter: `-h | this/track | null`\n"
                                    + "this/track: Repeat the current track.\n";

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

        if(UtilBot.isMajority(e.getMember()) ||
            UtilBot.isMod(e.getMember()))
        {
            if(args.length == 0) {
                if (Music.checkMode(e, PlayerMode.REPEAT))
                    repeat(e);
            } else if("this".equals(args[0]) || "track".equals(args[0])) {
                if (Music.checkMode(e, PlayerMode.REPEAT_SINGLE))
                    repeatSingle(e);
            }
        }
        else {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
            + "members with `Administrator` or `Manage Server` permissions only.\n"
            + "You can also shuffle the queue if there is less than 3 members in the voice channel.").queue();
        }
    }

    public void repeat(MessageReceivedEvent e)
    {
        //Prevent user that is not in the same voice channel from repeating the Queue
        if(!Music.checkVoiceChannel(e)) {
            return;
        }

        TrackScheduler scheduler = Main.getGuild(e.getGuild()).getScheduler();
        if(scheduler.getMode() != PlayerMode.REPEAT) {
            scheduler.setMode(PlayerMode.REPEAT);
            e.getChannel().sendMessage(Emoji.REPEAT + " Repeat mode on.").queue();
        } else {
            scheduler.setMode(PlayerMode.NORMAL);
            e.getChannel().sendMessage(Emoji.REPEAT + " Repeat mode off.").queue();
        }
    }

    public void repeatSingle(MessageReceivedEvent e)
    {
        //Prevent user that is not in the same voice channel from repeating the song
        if(!Music.checkVoiceChannel(e)) {
            return;
        }

        TrackScheduler scheduler = Main.getGuild(e.getGuild()).getScheduler();
        if(scheduler.getMode() != PlayerMode.REPEAT_SINGLE) {
            scheduler.setMode(PlayerMode.REPEAT_SINGLE);
            e.getChannel().sendMessage(Emoji.REPEAT + " Repeat (Current Track) mode on.").queue();
        } else {
            scheduler.setMode(PlayerMode.NORMAL);
            e.getChannel().sendMessage(Emoji.REPEAT + " Repeat (Current Track) mode off.").queue();
        }
    }
    
}
