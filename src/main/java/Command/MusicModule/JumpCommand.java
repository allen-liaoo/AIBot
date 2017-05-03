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
import Constants.Constants;
import Constants.Emoji;
import Main.Main;
import Setting.Prefix;
import Utility.UtilBot;
import Utility.UtilString;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class JumpCommand extends Command {
    
    public final static String HELP = "This command is for jumping to a certain position of the current playing song.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"jump`  or `" + Prefix.getDefaultPrefix() +"jp`\n"
                                    + "Parameter: `-h | [Position] | null`\n"
                                    + "[Position]: The time you want to jump to in HH:MM:SS format.\n"
                                    + "Hours and minutes are optional.";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Jump -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        else
        {
            String requester = Main.getGuild(e.getGuild()).getScheduler().getNowPlayingTrack().getRequester();
            
            if((requester != null && requester.equals(e.getAuthor().getId())) ||
                UtilBot.isMajority(e.getMember()) ||
                UtilBot.isMod(e.getMember()))
            {
                try {
                    String duration = UtilString.formatDurationString(args[0]);
                    long position = Duration.between(LocalTime.MIN, LocalTime.parse(duration)).toMillis();

                    //Prevent user from jumping too far ahead or too far behind
                    long dur = Main.getGuild(e.getGuild()).getPlayer().getPlayingTrack().getDuration();
                    long pos = Main.getGuild(e.getGuild()).getPlayer().getPlayingTrack().getPosition();
                    if(position>((dur-pos)/2+pos)) {
                        e.getChannel().sendMessage(Emoji.ERROR + " You can only jump to a position "
                            + "that is smaller than half the remaining duration!").queue();
                        return;
                    } else if(position<=(pos/2)) {
                        e.getChannel().sendMessage(Emoji.ERROR + " You can only jump to a position "
                            + "that is greater than half of the current duration!").queue();
                        return;
                    }

                    Music.jump(e, position);
                    e.getChannel().sendMessage(Emoji.JUMP + " Jumped to `"+duration+"`.").queue();
                } catch (ArithmeticException | DateTimeParseException ae) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid duration.").queue();
                } catch (NullPointerException npe) {
                    e.getChannel().sendMessage(Emoji.ERROR + " No song is playing!").queue();
                } 
            }
            else
            {
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, the song requester or "
                    + "members with `Administrator` or `Manage Server` permissions only.\n"
                    + "You can also stop the player if there is less than 3 members in the voice channel.").queue();
            }
        }
    }
    
}
