/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Command.Command;
import Main.Main;
import Constants.Constants;
import Constants.Emoji;
import Setting.Prefix;
import Utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class DumpCommand extends Command {

    public final static String HELP = "This command is for clearing the queue and skip votes.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"stop`\n"
                                    + "Parameter: `-h | null`";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Dump -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            e.getChannel().sendMessage(Emoji.ERROR + " You are not in a voice channel.").queue();
            return;
        } else if (Main.getGuild(e.getGuild()).getScheduler().getQueue().isEmpty()) {
            e.getChannel().sendMessage(Emoji.ERROR + " There is no song in the queue.").queue();
            return;
        }

        if(UtilBot.isMajority(e.getMember()) ||
            e.getMember().isOwner() || 
            e.getMember().hasPermission(Constants.PERM_MOD) ||
            Constants.D_ID.equals(e.getAuthor().getId()))
        {
            //Prevent user that is not in the same voice channel from stopping the PLAYER
            if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel() ||
                    !e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
                e.getChannel().sendMessage(Emoji.ERROR + " You and I are not in the same voice channel.").queue();
                return;
            }
            Main.getGuild(e.getGuild()).getScheduler().clearQueue().clearVote();

            e.getChannel().sendMessage(Emoji.STOP + " Cleared queue and dumped Trump.").queue();
        }
        else
        {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                    + "members with `Administrator` or `Manage Server` permissions only.\n"
                    + "You can also dump the queue if there is less than 3 members in the voice channel.").queue();
        }
    }

    
}
