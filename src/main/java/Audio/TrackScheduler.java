/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Audio.AudioTrackWrapper.TrackType;
import Constants.Emoji;
import AISystem.AILogger;
import Utility.UtilNum;
import Utility.UtilString;
import Utility.WebScraper;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.io.IOException;
import java.util.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */

/**
 * This class schedules tracks for the audio PLAYER. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

    private VoiceChannel vc;
    private TextChannel tc;

    /**
    * Track fields.
    */
    private final AudioPlayer player;
    private QueueList queue;
    private AudioTrackWrapper NowPlayingTrack;

    /**
    * Skip System fields.
    */
    private final ArrayList<User> skipper;

    /**
    * FM fields.
    */
    private ArrayList<String> fmSongs = new ArrayList<String>();
    private int auto = -1, previous = -1;

    /**
    * Enum type of the playing mode.
    */
    private PlayerMode Mode;

    public enum PlayerMode {
        DEFAULT,    //Default Mode, nothing playing

        /* Modes that can be override */
        NORMAL,     //Normal Mode, playing track radio, or playlist

        /* Overriding Mode */
        AUTO_PLAY,  //AutoPlay Mode, play the next song from YouTube AutoPlay mode
        REPEAT,     //Repeat Mode, retrieve the first queue and add to the last

        /* Unique Modes */
        FM;         //FM Mode, play automatic playlist

        @Override
        public String toString() {
            return UtilString.VariableToString("_", name());
        }
    }

  /**
   * @param player The audio PLAYER this scheduler uses=
   */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;

        this.queue = new QueueList();
        this.NowPlayingTrack = new AudioTrackWrapper();
        this.skipper = new ArrayList<>();
        this.Mode = PlayerMode.DEFAULT;
  }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply STOP the PLAYER.

        clearVote();

        if(Mode == PlayerMode.FM) {
            autoFM();
        } else if(Mode == PlayerMode.REPEAT) {
            AudioTrackWrapper repeat = NowPlayingTrack.makeClone();
            queue.add(repeat);
            NowPlayingTrack = queue.peek();
            player.startTrack(queue.poll().getTrack(), false);
        } else if(Mode == PlayerMode.AUTO_PLAY) {
            if(!queue.isEmpty()) {
                NowPlayingTrack = queue.peek();
                player.startTrack(queue.poll().getTrack(), false);
            } else {
                try {
                    autoPlay();
                } catch (IOException e) {
                    tc.sendMessage(Emoji.ERROR + " Fail to load the next song.").queue();
                }
            }
        } else if(queue.isEmpty()) {
            stopPlayer();
        } else if(Mode == PlayerMode.NORMAL) {
            NowPlayingTrack = queue.peek();
            player.startTrack(queue.poll().getTrack(), false);
        }
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
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

        if (!player.startTrack(track.getTrack(), true)) {
            queue.offer(track);
            e.getTextChannel().sendMessage(Emoji.SUCCESS + " Queued `" + track.getTrack().getInfo().title + "`").queue();
            return;
        }
        NowPlayingTrack = track;
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

    /**
     * Automatically load a FM song from fmSongs.
     */
    public void autoFM() {
        Mode = PlayerMode.FM;

        while (auto == previous) {
            auto = UtilNum.randomNum(0, this.fmSongs.size()-1);
        }
        previous = auto;
        String url = this.fmSongs.get(auto);

        if (Music.urlPattern.matcher(url).find()) {
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
                    tc.sendMessage(Emoji.ERROR + " Fail to load the song.").queue();
                    AILogger.errorLog(exception, null, this.getClass().getName(), "Failed to load fm");
                }
            });
        }
    }

    public void autoPlay() throws IOException {
        Mode = PlayerMode.AUTO_PLAY;

        String url = WebScraper.getYouTubeAutoPlay(NowPlayingTrack.getTrack().getInfo().uri);

        if (Music.urlPattern.matcher(url).find()) {
            Music.playerManager.loadItemOrdered(Music.playerManager, url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    NowPlayingTrack = new AudioTrackWrapper(track, "YouTube auto play", TrackType.NORMAL_REQUEST);
                    player.startTrack(track, false);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    addPlayList(playlist, "YouTube auto play");
                }

                @Override
                public void noMatches() {
                    tc.sendMessage(Emoji.ERROR + " No match found.").queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    tc.sendMessage(Emoji.ERROR + " Fail to load the next song.").queue();
                    AILogger.errorLog(exception, null, this.getClass().getName(), "Failed to load AutoPlay");
                }
            });
        }
    }

    /**
     * Show now playing message
     * @param player
     * @param track
     */
    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        if(tc!=null)
            tc.sendMessage(Emoji.NOTES + " Now playing `" + track.getInfo().title + "`").queue();

        System.out.println("Track Started: " + track.getInfo().title);
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

        if (endReason == AudioTrackEndReason.REPLACED)
            tc.sendMessage(Emoji.NEXT_TRACK + " Skipped the current song `" + track.getInfo().title + "`").queue();
        else if (endReason == AudioTrackEndReason.STOPPED)
            tc.sendMessage(Emoji.STOP + " Stopped and reset the player.").queue();

        if (endReason.mayStartNext) {
            nextTrack();
        }
        System.out.println("Track Ended: " + track.getInfo().title + " By reason: " + endReason.toString());
    }

    /**
     * Inform the user that track has stuck
     * @param player
     * @param track
     * @param thresholdMs
     */
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
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
        tc.sendMessage(Emoji.ERROR + " An error occurred! (Informed the owner)\n```\n\n"+exception.getLocalizedMessage()+"\n\n...```").queue();
        nextTrack();
        AILogger.errorLog(exception, this.getClass(), "TrackException(FriendlyException)", "Probably track decoding problem");
    }

    /**
     * Inform the user when the player is paused
     * @param player
     */
    @Override
    public void onPlayerPause(AudioPlayer player) {
        tc.sendMessage(Emoji.PAUSE + " Player paused.").queue();
    }

    /**
     * Inform the user when the player is resumed
     * @param player
     */
    @Override
    public void onPlayerResume(AudioPlayer player) {
        tc.sendMessage(Emoji.RESUME + " Player resumed.").queue();
    }

    /**
    * Clear methods
    * @return TrackScheduler, easier for chaining
    */
    public void stopPlayer() {
        clearNowPlayingTrack()
        .clearQueue()
        .clearVote()
        .clearMode()
        .player.stopTrack();
    }

    public TrackScheduler clearQueue() {
        queue.clear();
        fmSongs.clear();
        return this;
    }

    public TrackScheduler clearMode() {
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
    public TextChannel getTc() {
        return tc;
    }

    public void setTc(TextChannel tc) {
        this.tc = tc;
    }

    public VoiceChannel getVc() {
        return vc;
    }

    public void setVc(VoiceChannel vc) {
        this.vc = vc;
    }

    public PlayerMode getMode() {
        return Mode;
    }

    public void setMode(PlayerMode Mode) {
        this.Mode = Mode;
    }

    public QueueList getQueue() {
        return queue;
    }

    public ArrayList<String> getFmSongs() {
        return fmSongs;
    }

    public void setFmSongs(ArrayList<String> fmSongs) {
        this.fmSongs = fmSongs;
    }

    public void addFMSong(String song) {
        this.fmSongs.add(song);
    }

    public AudioTrackWrapper getNowPlayingTrack() {
        return NowPlayingTrack;
    }

    public ArrayList<User> getVote() {
        return skipper;
    }

    /**
     * Add vote
     * @param vote
     * @return
     */
    public boolean addVote(User vote) {
        if(!skipper.contains(vote)) {
            skipper.add(vote);
            return true;
        }
        return false;
    }

    public int getRequiredVote() {
        double mem = 0;
        //Only count non-Bot Users
        List<Member> members = vc.getMembers();
        for(Member m : members) {
            if(!m.getUser().isBot())
                mem++;
        }

        //Check if majority of the members agree to skip
        return (int) Math.ceil(mem / 2);
    }

}

