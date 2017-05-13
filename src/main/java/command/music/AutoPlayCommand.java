package command.music;

import audio.GuildPlayer;
import audio.Music;
import audio.PlayerMode;
import command.Command;
import constants.Emoji;
import main.AIBot;
import setting.Prefix;
import utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
            if(Music.checkMode(e, PlayerMode.AUTO_PLAY))
                autoPlay(e);
        }
        else {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                    + "members with `Administrator` or `Manage Server` permissions only.\n"
                    + "You can also stop the player if there is less than 3 members in the voice channel.").queue();
        }
    }

    public void autoPlay(MessageReceivedEvent e)
    {
        if(!Music.checkVoiceChannel(e))
            return;

        GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
        AIBot.getGuild(e.getGuild()).setTc(e.getTextChannel());

        if(player.getMode() != audio.PlayerMode.AUTO_PLAY) {
            player.setMode(audio.PlayerMode.AUTO_PLAY);
            e.getChannel().sendMessage(Emoji.AUTOPLAY + " AutoPlay mode on.").queue();
        } else {
            player.setMode(audio.PlayerMode.NORMAL);
            e.getChannel().sendMessage(Emoji.AUTOPLAY + " AutoPlay mode off.").queue();
        }
    }
}
