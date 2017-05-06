/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import system.AILogger;
import audio.AudioTrackWrapper;
import audio.QueueList;
import command.Command;
import main.AIBot;
import constants.Emoji;
import constants.Global;
import Setting.Prefix;
import utility.UtilBot;
import utility.UtilNum;
import utility.UtilString;
import utility.WebScraper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.io.IOException;
import java.time.Instant;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SongCommand extends Command{

    public final static  String HELP = "This command is for getting informations about a current playing or queued song.\n"
                                     + "command Usage: `"+ Prefix.getDefaultPrefix() +"nowplaying` or `"+ Prefix.getDefaultPrefix() +"song` or `"+ Prefix.getDefaultPrefix() +"np`\n"
                                     + "Parameter: `-h | [Queue Position] | null`";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Song -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        else
        {
            if(args.length == 0)
            {
                try {
                   AudioTrackWrapper nowplaying = AIBot.getGuild(e.getGuild()).getScheduler().getNowPlayingTrack();
                   e.getChannel().sendMessage(trackInfo(e, nowplaying, "Now Playing").build()).queue();
                } catch (NullPointerException npe) {
                    e.getChannel().sendMessage(Emoji.ERROR + " No song is playing.").queue();
                }
            }
            else
            {
                QueueList queue = AIBot.getGuild(e.getGuild()).getScheduler().getQueue();
                AudioTrackWrapper np = AIBot.getGuild(e.getGuild()).getScheduler().getNowPlayingTrack();

                int target = 0;
                String search = "";
                if(UtilNum.isInteger(args[0]))
                    target = Integer.parseInt(args[0]);
                else {
                    for(String s : args) { search += s; }
                    target = queue.findIndex(search);
                    // If return queue from QueueList#findIndex(), then check nowplayingtrack.
                    // Return -1 if nowplayingtrack is the result. If no result, return -2.
                    target = target == -1 ? (np.getTrack().getInfo().title.toLowerCase().contains(search) ? -1 : -2) : target;
                }

                if(target > queue.size()-1)
                {
                    e.getChannel().sendMessage(Emoji.ERROR + " The position exceeds the range of this queue (" + queue.size() + ").").queue();
                    return;
                } else if(target == -2) { //No search result
                    e.getChannel().sendMessage(Emoji.ERROR + " No result of " + search + " in the queue.").queue();
                    return;
                }

                AudioTrackWrapper songinfo = null;
                if(target == -1) {
                    songinfo = np;
                    e.getChannel().sendMessage(trackInfo(e, songinfo, "Now Playing").build()).queue();
                } else if(target == -2) {
                    songinfo = AIBot.getGuild(e.getGuild()).getScheduler().getQueue().get(target);
                    e.getChannel().sendMessage(trackInfo(e, songinfo, "Queue Song (Position " + target + ")").build()).queue();
                }
            }
        }
    }
    
    /**
     * Track Information Getter
     * @param e
     * @param track Wrapper class for getting basic informations and requesters
     * @param title
     */
    public EmbedBuilder trackInfo(MessageReceivedEvent e, AudioTrackWrapper track, String title) {
        AudioTrackInfo trackInfo = track.getTrack().getInfo();
        String trackTime = UtilString.formatDurationToString(track.getTrack().getPosition());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title, trackInfo.uri, Global.B_AVATAR);
        embedBuilder.setColor(UtilBot.randomColor());
        embedBuilder.addField("Song Title:", trackInfo.title, true);
        embedBuilder.addField("Author:", trackInfo.author, true);
        embedBuilder.addField("Song Link:", trackInfo.uri, false);
        if (track.getType() != AudioTrackWrapper.TrackType.RADIO) {
            trackTime += " / " + UtilString.formatDurationToString(track.getTrack().getDuration());
        }
        embedBuilder.addField("Song Duration:", trackTime, true);
        embedBuilder.addField("Track Type:", track.getType().toString(), true);
        embedBuilder.addField("Stream:", UtilString.VariableToString(null, trackInfo.isStream + "") + "", true);
        embedBuilder.addField("Requested by:", track.getRequester(), true);
        embedBuilder.setThumbnail(Global.B_AVATAR);
        embedBuilder.setTimestamp(Instant.now());
        try {
            embedBuilder.setImage(WebScraper.getYouTubeThumbNail(track.getTrack().getInfo().uri));
        } catch (IOException ex) {
            AILogger.errorLog(ex, e, "Music#trackInfo", "IOException on getting thumbnail of " + track.getTrack().getInfo().uri);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
        }
        return embedBuilder;
    }

    
}
