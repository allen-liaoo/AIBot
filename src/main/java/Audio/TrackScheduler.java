/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Audio.AudioTrackWrapper.TrackType;
import Resource.Emoji;
import Utility.AILogger;
import Utility.UtilNum;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
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
 * This class schedules tracks for the audio PLAYER. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    
    private static TextChannel tc;
    
    /*
    * Track fields
    */
    private final AudioPlayer player;
    private BlockingQueue<AudioTrackWrapper> queue;
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
   * @param player The audio PLAYER this scheduler uses=
   */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.NowPlayingTrack = new AudioTrackWrapper();
        this.skipper = new ArrayList<>();
        this.Mode = PlayerMode.DEFAULT;
  }

    /**
     * Start the next track, stopping the current ONE if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply STOP the PLAYER.
        
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

    /**
     * Show now playing message
     * @param player
     * @param track
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
        if(tc!=null)
            tc.sendMessage(Emoji.NOTES + " Now playing `" + track.getInfo().title + "`").queue();
    }

    /**
     * Determine the PLAYER mode and start the next track
     * @param player
     * @param track
     * @param endReason
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        clearVote();
        if (endReason.mayStartNext) {
            nextTrack();
        }
        System.out.println("Track Ended: " + track.getInfo().title + " By reason: " + endReason.toString());
    }

    /**
     *Inform the user that track has stuck
     * @param player
     * @param track
     * @param thresholdMs
     */
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
        if(tc!=null)
            tc.sendMessage(Emoji.ERROR + " Track stuck! Skipping to the next track...").queue();
        nextTrack();
    }

    /**
     * Inform the user that track has thrown an exception
     * @param player
     * @param track
     * @param exception
     */
    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
        
        if(tc!=null)
            tc.sendMessage(Emoji.ERROR + " An error occurred!\n```\n\n"+exception.getMessage()+"```").queue();
    }
    
    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     * @param e
     */
    public void queue(AudioTrackWrapper track, MessageReceivedEvent e) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the PLAYER was already playing so this
        // track goes to the queue instead.
        
        if(this.Mode == PlayerMode.FM) {
            e.getChannel().sendMessage(Emoji.ERROR + " FM mode is ON! Only request radio or songs when FM is not playing.").queue();
            return;
        }

        this.Mode = PlayerMode.NORMAL;

        if (!player.startTrack(track.getTrack(), true)) {
            queue.offer(track);
            e.getTextChannel().sendMessage(Emoji.SUCCESS + " Queued `" + track.getTrack().getInfo().title + "`").queue();
            return;
        }
        NowPlayingTrack = track;
    }
    
    public void autoPlay() {
        Mode = PlayerMode.FM;
        
        while (auto == previous) {
            auto = UtilNum.randomNum(0, fmSongs.size()-1);
            System.out.println(auto);
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
                    addPlayList(playlist, "AIBot FM");
                }

                @Override
                public void noMatches() {
                    tc.sendMessage(Emoji.ERROR + " No match found.").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    tc.sendMessage(Emoji.ERROR + " Fail to load the video.").queue();
                    AILogger.errorLog(exception, null, this.getClass().getName(), "Failed to load fm");
                }
            });
        }
    }
    
    /**
     * Add the play list to the queue
     * @param list
     * @param requester
     */
    public void addPlayList(AudioPlaylist list, String requester) {
        List<AudioTrack> tracklist = list.getTracks();
        
        for(AudioTrack track : tracklist) {
            AudioTrackWrapper wrapper = new AudioTrackWrapper(track, requester, TrackType.PLAYLIST);
            if (!player.startTrack(wrapper.getTrack(), true)) {
                queue.offer(wrapper);
                continue;
            }
            NowPlayingTrack = wrapper;
        }
    }
    
    public void shuffle() {
        List<AudioTrackWrapper> queueList = new ArrayList(queue);
        Collections.shuffle(queueList);
        queue = new LinkedBlockingQueue (queueList);
    }
    
    /**
    * Clear methods
    * @return TrackScheduler, easier for chaining
    */
    public void stopPlayer() {
        Mode = PlayerMode.DEFAULT;
        clearNowPlayingTrack()
        .clearQueue()
        .clearVote()
        .player.stopTrack();
    }
    
    public TrackScheduler clearQueue() {
        queue.clear();
        return this;
    }
    
    public TrackScheduler clearFM() {
        fmSongs.clear();
        Mode = PlayerMode.DEFAULT;
        return this;
    }
    
    public TrackScheduler clearNowPlayingTrack() {
        NowPlayingTrack = new AudioTrackWrapper();
        return this;
    }
    
    public TrackScheduler clearVote() {
        skipper.clear();
        return this;
    }
    
    /**
     * Getter and Setter
     */
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

    public AudioTrackWrapper getNowPlayingTrack() {
        return NowPlayingTrack;
    }

    public static TextChannel getTc() {
        return tc;
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
    
}

