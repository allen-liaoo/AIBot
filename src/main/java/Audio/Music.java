/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Audio.AudioConnection;
import Config.Emoji;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.regex.Matcher;
import net.dv8tion.jda.core.JDA;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Music {
    public static AudioPlayerManager playerManager;
    public static GuildMusicManager guildManager;
    public static final Pattern urlPattern = Pattern.compile("^(https?|ftp)://([A-Za-z0-9-._~/?#\\\\[\\\\]:!$&'()*+,;=]+)$");
    
    public static void musicStartup(JDA jda, String id){
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        guildManager = new GuildMusicManager(playerManager);
        jda.getGuildById(id).getAudioManager().setSendingHandler(guildManager.getSendHandler());
    }
    
    public static void play(String[] args, MessageReceivedEvent e)
    {
        Matcher m = Music.urlPattern.matcher(args[0]);
        AudioConnection.connect(e);
        
        if(m.find()){
            Music.playerManager.loadItemOrdered(Music.playerManager, args[0], new AudioLoadResultHandler() {
                public void trackLoaded(AudioTrack track) {
                    if(track.getState() == track.getState().PLAYING)
                        e.getTextChannel().sendMessage(Emoji.success + " Queued `" + track.getInfo().title + "`").queue();
                    else
                        e.getTextChannel().sendMessage(Emoji.success + " Now playing `" + track.getInfo().title + "`. Track loaded successfully!").queue();
                    Music.guildManager.scheduler.queue(track);
                    return;
                }

                public void playlistLoaded(AudioPlaylist playlist) {
                    e.getTextChannel().sendMessage("Playlist loaded successfully!").queue();
                    return;
                }

                public void noMatches() {
                    e.getTextChannel().sendMessage("No match found :c").queue();
                    return;
                }

                public void loadFailed(FriendlyException exception) {
                    e.getTextChannel().sendMessage(exception.getMessage()).queue();
                    return;
                }
            });
            return;
        }
        else
        {
            e.getTextChannel().sendMessage("No match found :c").queue();
            return;
        }
    }
    
    public static void stop(MessageReceivedEvent e)
    {
        Music.guildManager.player.stopTrack();
        AudioConnection.disconnect(e);
    }
}
