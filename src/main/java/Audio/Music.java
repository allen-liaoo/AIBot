/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Main.Main;
import Resource.Emoji;
import Setting.SmartLogger;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
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
    private static MessageReceivedEvent event;
    
    public static void musicStartup(){
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }
    
    public static void play(String link, MessageReceivedEvent e)
    {
        Matcher m = Music.urlPattern.matcher(link);
        event = e;
        AudioConnection.connect(e);
        
        if(m.find()){
            Music.playerManager.loadItemOrdered(Music.playerManager, link, new AudioLoadResultHandler() {
                public void trackLoaded(AudioTrack track) {
                    if(Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack() != null)
                        e.getTextChannel().sendMessage(Emoji.success + " Queued `" + track.getInfo().title + "`").queue();
                    else
                        e.getTextChannel().sendMessage(Emoji.success + " Now playing `" + track.getInfo().title + "`. Track loaded successfully!").queue();
                    
                    Main.guilds.get(e.getGuild().getId()).scheduler.queue(track);
                    return;
                }

                public void playlistLoaded(AudioPlaylist playlist) {
                    e.getTextChannel().sendMessage(Emoji.success + " Playlist loaded successfully!").queue();
                    return;
                }

                public void noMatches() {
                    e.getTextChannel().sendMessage(Emoji.error + " No match found.").queue();
                    return;
                }

                public void loadFailed(FriendlyException exception) {
                    e.getTextChannel().sendMessage(Emoji.error + " Fail to load the video.").queue();
                    SmartLogger.errorLog(exception, e.getGuild().getName(), this.getClass().getName(), "Playing Load Failed.");
                    return;
                }
            });
            return;
        }
        else
        {
            e.getTextChannel().sendMessage(Emoji.error + " No match found.").queue();
            return;
        }
    }
    
    public static void pause(MessageReceivedEvent e)
    {
        Main.guilds.get(e.getGuild().getId()).getPlayer().setPaused(true);
    }
    
    public static void resume(MessageReceivedEvent e)
    {
        Main.guilds.get(e.getGuild().getId()).getPlayer().setPaused(false);
    }
    
    public static void skip(MessageReceivedEvent e)
    {
        Main.guilds.get(e.getGuild().getId()).getScheduler().nextTrack();
    }
    
    public static void stop(MessageReceivedEvent e)
    {
        Main.guilds.get(e.getGuild().getId()).getPlayer().stopTrack();
        AudioConnection.disconnect(e);
    }
    
    public static void setVolume(MessageReceivedEvent e, int in)
    {
        Main.guilds.get(e.getGuild().getId()).getPlayer().setVolume(in);
    }
}
