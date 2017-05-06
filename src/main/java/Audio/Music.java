/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Audio.AudioTrackWrapper.TrackType;
import Audio.PlayerMode;
import Main.Main;
import Constants.Emoji;
import AISystem.AILogger;
import Setting.Prefix;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Music  {
    public static AudioPlayerManager playerManager;
    public static final Pattern urlPattern = Pattern.compile("^(https?|ftp)://([A-Za-z0-9-._~/?#\\\\[\\\\]:!$&'()*+,;=]+)$");
    
    public static void musicStartup(){
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }
    
    /**
     * Play the song
     * @param link the link to play the song
     * @param e
     * @param type Track Type: NORMAL_REQUEST, RADIO
     */
    public static void play(String link, MessageReceivedEvent e, TrackType type)
    {
        Matcher m = Music.urlPattern.matcher(link);
        Connection.connect(e, false);
        
        if(!e.getMember().getVoiceState().inVoiceChannel())
            return;
        
        AILogger.commandLog(e, "Music#Play", "Music played for " + link);
        
        if(m.find()){
            try {
                TrackScheduler scheduler = Main.getGuild(e.getGuild()).getScheduler();
                //Only turn the mode to normal is this was in default mode,
                //So repeat or AutoPlay mode will not be turned off
                if(scheduler.getMode() == Audio.PlayerMode.DEFAULT)
                    scheduler.setMode(Audio.PlayerMode.NORMAL);
                        
                Music.playerManager.loadItemOrdered(Music.playerManager, link, new LoadResultHandler(scheduler) {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        scheduler.queue(new AudioTrackWrapper(track, e.getAuthor().getName(), type), e);
                    }
                    
                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        scheduler.addPlayList(playlist, e.getAuthor().getName());
                        e.getTextChannel().sendMessage(Emoji.SUCCESS + " Queued Playlist: `" + playlist.getName() + "`").queue();
                    }
                }).get();
            } catch (InterruptedException ex) {
                AILogger.errorLog(ex, e, "Music#play", "Interrupted when retrieving AudioTrack");
            } catch (ExecutionException ex) {
                AILogger.errorLog(ex, e, "Music#play", "ExecutionException when retrieving AudioTrack");
            }
        } else {
            e.getTextChannel().sendMessage(Emoji.ERROR + " No match found.").queue();
        }
    }
    
    /**
     * Pause the player
     * @param e
     */
    public static void pause(MessageReceivedEvent e) { Main.getGuild(e.getGuild()).getPlayer().setPaused(true); }
    
    /**
     * Resume the player
     * @param e
     */
    public static void resume(MessageReceivedEvent e)
    {
        Main.getGuild(e.getGuild()).getPlayer().setPaused(false);
    }
    
    /**
     * Play or pause
     * @param e
     */
    public static void pauseOrPlay(MessageReceivedEvent e)
    {
        if(!checkVoiceChannel(e))
            return;

        if(Main.getGuild(e.getGuild()).getPlayer().getPlayingTrack() == null)
            throw new NullPointerException("No track is playing in this guild");

        if(Main.getGuild(e.getGuild()).getPlayer().isPaused())
            Music.resume(e);
        else if(!Main.getGuild(e.getGuild()).getPlayer().isPaused())
            Music.pause(e);
    }
    
    /**
     * Set volume of the bot
     * @param e
     * @param vol
     */
    public static void setVolume(MessageReceivedEvent e, int vol)
    {
        Main.getGuild(e.getGuild()).getPlayer().setVolume(vol);
    }
    
    /**
     * Jump/Seek to a position
     * @param e
     * @param position
     */
    public static void jump(MessageReceivedEvent e, long position) 
    {
        //Prevent user that is not in the same voice channel from jumping to song
        if(!checkVoiceChannel(e)) {
            return;
        }
        
        AudioTrack track = Main.getGuild(e.getGuild()).getPlayer().getPlayingTrack();
        if(track.isSeekable()) {
            track.setPosition(position);
        }
    }
    
    /**
     * Shuffle queue
     * @param e
     */
    public static void shuffle(MessageReceivedEvent e)
    {
        //Prevent user that is not in the same voice channel from shuffling the Queue
        if(!checkVoiceChannel(e)) {
            return;
        }
        
        Main.getGuild(e.getGuild()).getScheduler().getQueue().shuffle();
        e.getChannel().sendMessage(Emoji.SHUFFLE + " Shuffled queue.").queue();
    }

    /**
     * Check if the user is in the same voice channel than the bot
     * @param e
     * @return true if the bot can play music
     */
    public static boolean checkVoiceChannel(MessageReceivedEvent e)
    {
        //Check if the user is in a voice channel
        if(!e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
            e.getChannel().sendMessage(Emoji.ERROR + " You are not in a voice channel").queue();
            return false;
        }

        //Prevent user that is not in the same voice channel from executing a command
        if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel()) {
            e.getChannel().sendMessage(Emoji.ERROR + " You and I are not in the same voice channel.").queue();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if the mode is not restricted(controversial).
     * Decide between Normal, AutoPlay or FM Mode.
     * @param e
     * @param mode the mode wish to proceed
     * @return true if can proceed
     */
    public static boolean checkMode(MessageReceivedEvent e, PlayerMode mode)
    {
        /**
         * PlayerMode Rules:
         * Default mode is only when no music is playing.
         * Normal is the general mode, can be override by Repeat or AutoPlay.
         * FM is strictly unique. It does not work with any mode.
         * Repeat or AutoPlay can not work with each other.
         */
        PlayerMode current = Main.getGuild(e.getGuild()).getScheduler().getMode();
        if((mode == PlayerMode.AUTO_PLAY && !current.canAutoPlay()) ||
            (mode == PlayerMode.REPEAT && !current.canRepeat()) ||
            (mode == PlayerMode.REPEAT_SINGLE && !current.canRepeatSingle()) ||
            (mode == PlayerMode.FM && !current.canFM())) {
            e.getChannel().sendMessage(Emoji.ERROR + " Cannot activate `" + mode.toString()
                    + " Mode` because ` " + current.toString() + " Mode` is already on.").queue();
            return false;
        }
        return true;
    }
    
    /**
     * Turn the position of the current player to a String, i.e. "▬ ▬ ▬ O ▬ ▬ ▬"
     * @param e
     * @return
     */
    public static String positionToString(MessageReceivedEvent e)
    {
        String start = "", progress = "";
        
        //Inverse play and pause button, like a media player would.
        if(!Main.getGuild(e.getGuild()).getPlayer().isPaused()) {
            start = Emoji.PAUSE;
        } else if (Main.getGuild(e.getGuild()).getPlayer().isPaused()) {
            start = Emoji.RESUME;
        }
        
        long duration = Main.getGuild(e.getGuild()).getPlayer().getPlayingTrack().getDuration();
        long position = Main.getGuild(e.getGuild()).getPlayer().getPlayingTrack().getPosition();
        long unit = duration/10;
        int pos = (int) ((int) position/unit);
        
        for(int i = 0; i < 10; i ++) {
            progress += i==pos ? Emoji.RADIO + " " : i==9 ? "▬" : "▬ ";
        }
        
        return start+" "+progress;
    }
    
    /**
     * Turn the volume of the current player to a String
     * @param e
     * @return
     */
    public static String volumeToString(MessageReceivedEvent e) {
        int vol = Main.getGuild(e.getGuild()).getPlayer().getVolume();
        return (vol>49 ? Emoji.VOLUME_HIGH : vol>0 ? Emoji.VOLUME_LOW : Emoji.VOLUME_NONE)+" "+vol;
    }
}
