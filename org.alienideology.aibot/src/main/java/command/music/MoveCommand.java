package command.music;

import audio.GuildPlayer;
import audio.Music;
import audio.QueueList;
import command.Command;
import constants.Emoji;
import main.AIBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;
import utility.UtilBot;

/**
 * Move to a position in the queue.
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class MoveCommand extends Command {

    public final static String HELP = "This command is for moving to a position in the queue and start playing directly from there.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"move`  or `" + Prefix.getDefaultPrefix() +"mv`\n"
                                    + "Parameter: `-h | [index] | null`\n"
                                    + "[index]: Go to a position in the queue.";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Move -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
        AIBot.getGuild(e.getGuild()).setTc(e.getTextChannel());

        moveToQueue(args, e, player);
    }

    /**
     * Go to a certain index of the queue and play from there
     * @param args
     * @param e
     * @param player
     */
    public void moveToQueue(String[] args, MessageReceivedEvent e, GuildPlayer player)
    {
        QueueList queue = player.getQueue();

        if(UtilBot.isMod(e.getMember()) || UtilBot.isMajority(e.getMember()))
        {
            if(!Music.checkVoiceChannel(e))
                return;

            try {
                int target = Integer.parseInt(args[0]);
                if(target > queue.size() || target <= 0) { // Check if the target is too large or small
                    e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid index between 1 and "+(queue.size()-1)).queue();
                } else { //Go to the position in the queue
                    queue.skipTo(target-2);
                    player.nextTrack();
                }
            } catch (ArrayIndexOutOfBoundsException aoobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a number to go to.").queue();
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid index between 1 and "+(queue.size()-1)).queue();
            }
        }
        else {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                    + "members with `Administrator` or `Manage Server` permissions only.\n"
                    + "It'll also work if there is less than 3 members in the voice channel.").queue();
        }
    }

}
