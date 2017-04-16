/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Resource.Emoji;
import Utility.SmartLogger;
import Utility.UtilTool;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    
    private static TextChannel tc;
    
    /*
    * Track fields
    */
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrackWrapper> queue;
    private AudioTrackWrapper NowPlayingTrack;
    
    /*
    * Skip System fields
    */
    public final ArrayList<User> skipper;
    
    /*
    * FM fields
    */
    public static ArrayList<String> fmSongs = new ArrayList<String>();
    private int auto = -1, previous = -1;
    
    /*
    * Enum type of the playing mode
    */
    private PlayerMode Mode;
    
    public enum PlayerMode {
        DEFAULT,
        NORMAL,
        FM;
        
        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

  /**
   * @param player The audio player this scheduler uses=
   */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.NowPlayingTrack = new AudioTrackWrapper();
        this.skipper = new ArrayList<>();
        this.Mode = PlayerMode.DEFAULT;
  }

  /**
   * Add the next track to queue or play right away if nothing is in the queue.
   *
   * @param track The track to play or add to queue.
   * @param e
   */
    public void queue(AudioTrackWrapper track, MessageReceivedEvent e) {
    // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
    // something is playing, it returns false and does nothing. In that case the player was already playing so this
    // track goes to the queue instead.
    
        if(this.Mode == PlayerMode.FM) {
            e.getChannel().sendMessage(Emoji.error + " FM mode is ON! Only request radio or songs when FM is not playing.").queue();
            return;
        }
        
        this.Mode = PlayerMode.NORMAL;
        
        if (!player.startTrack(track.getTrack(), true)) {
            queue.offer(track);
            e.getTextChannel().sendMessage(Emoji.success + " Queued `" + track.getTrack().getInfo().title + "`").queue();
            return;
        }
        NowPlayingTrack = track;
  }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        
        clearVote();
        
        if(Mode == PlayerMode.FM){
            autoPlay();
        } else if (queue.peek() != null) {
            NowPlayingTrack = queue.peek();
            player.startTrack(queue.poll().getTrack(), false);
        } else {
            stopPlayer();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
        tc.sendMessage(Emoji.notes + " Now playing `" + track.getInfo().title + "`").queue();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        skipper.clear();
        if (endReason.mayStartNext) {
            if(Mode == PlayerMode.FM) {
                autoPlay();
            }
            else {
                nextTrack();
            }
        }
    }
    
    public void autoPlay() {
        Mode = PlayerMode.FM;
        
        while (auto == previous) {
            auto = UtilTool.randomNum(0, fmSongs.size()-1);
        }
        previous = auto;
        String url = fmSongs.get(auto);
        
        Matcher m = Music.urlPattern.matcher(url);
        if (m.find()) {
            Music.playerManager.loadItemOrdered(Music.playerManager, url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    NowPlayingTrack = new AudioTrackWrapper(track, "AIBot FM", AudioTrackWrapper.TrackType.FM);
                    player.startTrack(track, false);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    tc.sendMessage(Emoji.success + " Playlist loaded successfully! `" + playlist.getName() + "`").queue();
                }

                @Override
                public void noMatches() {
                    tc.sendMessage(Emoji.error + " No match found.").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    tc.sendMessage(Emoji.error + " Fail to load the video.").queue();
                    SmartLogger.errorLog(exception, null, this.getClass().getName(), "Failed to load fm");
                }
            });
        }
    }
    
    public void stopPlayer() {
        clearNowPlayingTrack();
        clearQueue();
        clearFM();
        clearVote();
        player.stopTrack();
    }
    
    public PlayerMode getMode() {
        return Mode;
    }

    public void setMode(PlayerMode Mode) {
        this.Mode = Mode;
    }
    
    public BlockingQueue<AudioTrackWrapper> getQueue() {
        return queue;
    }
    
    public Iterator getQueueIterator() {
        return queue.iterator();
    }
    
    public void clearQueue() {
        queue.clear();
    }
    
    public void clearFM() {
        fmSongs.clear();
        Mode = PlayerMode.DEFAULT;
    }

    public AudioTrackWrapper getNowPlayingTrack() {
        return NowPlayingTrack;
    }
    
    public void clearNowPlayingTrack() {
        NowPlayingTrack = null;
    }

    public void setTc(TextChannel tc) {
        this.tc = tc;
    }

    public ArrayList<User> getVote() {
        return skipper;
    }

    public boolean addVote(User vote) {
        if(!skipper.contains(vote))
        {
            skipper.add(vote);
            return true;
        }
        else
            return false;
    }
    
    public void clearVote() {
        skipper.clear();
    }
    
}

