/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Main.Main;
import Resource.Emoji;
import Resource.Info;
import Setting.SmartLogger;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
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
        AudioConnection.connect(e, false);
        
        if(!e.getMember().getVoiceState().inVoiceChannel())
            return;
        
        SmartLogger.commandLog(e, "Music#Play", "Music played for " + link);
        
        if(m.find()){
            Music.playerManager.loadItemOrdered(Music.playerManager, link, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    if(Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack() != null)
                        e.getTextChannel().sendMessage(Emoji.success + " Queued `" + track.getInfo().title + "`").queue();
                    //else
                    //    e.getTextChannel().sendMessage(Emoji.notes + " Now playing `" + track.getInfo().title + "`").queue();
                    
                    Main.guilds.get(e.getGuild().getId()).getScheduler().queue(track, e);
                    return;
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    e.getTextChannel().sendMessage(Emoji.success + " Playlist loaded successfully!").queue();
                    return;
                }

                @Override
                public void noMatches() {
                    e.getTextChannel().sendMessage(Emoji.error + " No match found.").queue();
                    return;
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    e.getTextChannel().sendMessage(Emoji.error + " Fail to load the video.").queue();
                    SmartLogger.errorLog(exception, e, this.getClass().getName(), "Failed to load this video: " + link);
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
        Main.guilds.get(e.getGuild().getId()).getScheduler().clearQueue();
        AudioConnection.disconnect(e, false);
    }
    
    public static void setVolume(MessageReceivedEvent e, int in)
    {
        Main.guilds.get(e.getGuild().getId()).getPlayer().setVolume(in);
    }
    
    public static void trackInfo(MessageReceivedEvent e, AudioTrack track)
    {
        AudioTrackInfo trackInfo = track.getInfo();
        Long duration = track.getDuration();
        String trackTime = (duration/60000)%60 + ":" + (duration/1000)%60;
        //String requester = Main.guilds.get(e.getGuild().getId()).getScheduler().getRequester().get(0).getName();
        
        ArrayList<User> queuer = Main.guilds.get(e.getGuild().getId()).getScheduler().getRequester();
        
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Now Playing", trackInfo.uri, null);
        embedBuilder.setColor(Info.setColor());
        embedBuilder.addField("Song Title:", trackInfo.title, false);
        embedBuilder.addField("Song Link:", trackInfo.uri, false);
        embedBuilder.addField("Song Duration:", trackTime, false);
        embedBuilder.addField("Requested by:", queuer.get(0).getName(), false);
        embedBuilder.setTimestamp(Instant.now());
        embedBuilder.setThumbnail(Info.B_AVATAR);
        e.getTextChannel().sendMessage(embedBuilder.build()).queue();
        embedBuilder.clearFields();
    }
    
    public static void queueList(MessageReceivedEvent e)
    {
        BlockingQueue<AudioTrack> queue = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue();
        Iterator<AudioTrack> list = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueueIterator();
        ArrayList<User> queuer = Main.guilds.get(e.getGuild().getId()).getScheduler().getRequester();
        
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("Queue List", Info.B_INVITE, Info.B_AVATAR);
        embed.setColor(Info.setColor());
        embed.setThumbnail(Info.B_AVATAR);
        embed.setFooter("Reqested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
        embed.setTimestamp(Instant.now());
        
        //Now Playing
        AudioTrack playing = Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack();
        if(playing == null)
        {
            embed.addField("Now Playing", "None", false);   
        }
        else
        {
            String ptitle = playing.getInfo().title;
            String purl = playing.getInfo().uri;
            embed.addField("Now Playing", "[" + ptitle + "](" + purl + ")  \n"
                    + " Requested by " + queuer.get(0).getName(), false);
        }
        
        int count = 0;
        String songs = "";
        
        if(queue.peek() == null)
        {
            songs += "The queue is curently empty.";
            if(playing == null)
            {
                e.getChannel().sendMessage("The queue is currently empty, and there is no song playing.").queue();
                return;
            }
        }
        else
        {
            //Queue
            while(list.hasNext())
            {
                count++;

                AudioTrack track = list.next();
                String title = track.getInfo().title;
                String url = track.getInfo().uri;
                String requester = queuer.get(count).getName();
                songs += "**" + count + ".** [" + title + "](" + url + ")  Requested by " + requester + "\n";
            }
        }
        
        
        embed.addField("Coming Next", songs, false);
        
        e.getChannel().sendMessage(embed.build()).queue();
        embed.clearFields();
    }
}
