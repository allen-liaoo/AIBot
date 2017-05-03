package Command.MusicModule;

import Audio.Music;
import Audio.TrackScheduler;
import Command.Command;
import Constants.Emoji;
import Main.Main;
import Setting.Prefix;
import Utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;

/**
 * Created by liaoyilin on 5/2/17.
 */
public class AutoPlayCommand extends Command{

    public final static String HELP = "The bot will automatically get the next song from YouTube.\n"
            + "Command Usage: `"+ Prefix.getDefaultPrefix() +"autoplay` or `"+ Prefix.getDefaultPrefix() +"ap`\n"
            + "Parameter: `-h | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("AutoPlay -Help", HELP, true);
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
            if(Music.checkMode(e, TrackScheduler.PlayerMode.AUTO_PLAY))
                Music.autoPlay(e);
        }
        else {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                    + "members with `Administrator` or `Manage Server` permissions only.\n"
                    + "You can also stop the player if there is less than 3 members in the voice channel.").queue();
        }

    }
}
