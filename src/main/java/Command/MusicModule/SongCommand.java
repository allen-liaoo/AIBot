/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import AISystem.AILogger;
import Audio.AudioTrackWrapper;
import Command.Command;
import Main.Main;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import Utility.UtilBot;
import Utility.UtilString;
import Utility.WebScraper;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SongCommand extends Command{

    public final static  String HELP = "This command is for getting informations about a current playing or queued song.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"nowplaying` or `"+ Prefix.getDefaultPrefix() +"song` or `"+ Prefix.getDefaultPrefix() +"np`\n"
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
                   AudioTrackWrapper nowplaying = Main.getGuild(e.getGuild()).getScheduler().getNowPlayingTrack();
                   e.getChannel().sendMessage(trackInfo(e, nowplaying, "Now Playing").build()).queue();
                } catch (NullPointerException npe) {
                    e.getChannel().sendMessage(Emoji.ERROR + " No song is playing.").queue();
                }
            }
            else if(args.length >= 1 && Character.isDigit(args[0].charAt(0)))
            {
                BlockingQueue<AudioTrackWrapper> queue = Main.getGuild(e.getGuild()).getScheduler().getQueue();
                int count = 0, target = Integer.parseInt(args[0]);
                AudioTrackWrapper songinfo = null;
                
                if(target > queue.size()) 
                {
                    e.getChannel().sendMessage(Emoji.ERROR + " The position exceeds the range of this queue (" + queue.size() + ").").queue();
                    return;
                }

                for(AudioTrackWrapper song : queue)
                {
                    count++;
                    if(count == target)
                        songinfo = song;
                }

                e.getChannel().sendMessage(trackInfo(e, songinfo, "Queue Song (Position " + args[0] + ")").build()).queue();
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
        embedBuilder.setAuthor(title, trackInfo.uri, Constants.B_AVATAR);
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
        embedBuilder.setThumbnail(Constants.B_AVATAR);
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
