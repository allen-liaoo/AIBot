/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Main.Main;
import Resource.Emoji;
import Resource.Info;
import Resource.UtilTool;
import Resource.WebScraper;
import Setting.SmartLogger;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;;
import java.io.IOException;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;import java.io.IOException;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;import java.io.IOException;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;import java.io.IOException;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
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
     */
    public static void play(String link, MessageReceivedEvent e)
    {
        Matcher m = Music.urlPattern.matcher(link);
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
    
    /**
     * Vote Skip System
     * @param e
     * @param position The position of the song
     * @param force force skip
     * @return 0 if the song is skipped
     * @return a number more than 0 for the required votes
     * @return -1 if the voter already voted
     */
    public static int skip(MessageReceivedEvent e, int position, boolean force)
    {
        /*
        if(force)
        {
            Iterator it = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueueIterator();
            int count = 0;
            while(it.hasNext())
            {
                count++;
                if(count == position)
                    it.remove();
            }
            return 0;
        }*/
        
        //Vote Skip for current song
        if(position == 0)
        {
            boolean isAdded = Main.guilds.get(e.getGuild().getId()).getScheduler().addVote(e.getAuthor());
            int votes = Main.guilds.get(e.getGuild().getId()).getScheduler().getVote().size();
            if(isAdded)
            {
                int mem = 0;
                //Only count non-Bot Users
                List<Member> members = Main.guilds.get(e.getGuild().getId()).getVc().getMembers();
                for(Member m : members)
                {
                    if(!m.getUser().isBot())
                        mem++;
                }
                
                mem = (int) Math.ceil(mem / 2);
                if(votes >= mem)
                {
                    Main.guilds.get(e.getGuild().getId()).getScheduler().nextTrack();
                    Main.guilds.get(e.getGuild().getId()).getScheduler().clearVote();
                    return 0;
                }
                return votes - mem;
            }
            else
                return -1;
        }
        
        //Skip a song in the queue
        else if(position != 0)
        {
            return -1;
        }
        return 0;
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
        Long position = track.getPosition();
        Long duration = track.getDuration();
        String trackTime = (position/60000)%60 + ":" + (position/1000)%60 + 
                " / " + (duration/60000)%60 + ":" + (duration/1000)%60;
        
        ArrayList<String> queuer = Main.guilds.get(e.getGuild().getId()).getScheduler().getRequester();
        
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Now Playing", trackInfo.uri, null);
        embedBuilder.setColor(UtilTool.setColor());
        embedBuilder.addField("Song Title:", trackInfo.title, false);
        embedBuilder.addField("Song Link:", trackInfo.uri, false);
        embedBuilder.addField("Song Duration:", trackTime, false);
        embedBuilder.addField("Requested by:", queuer.get(0), false);
        embedBuilder.setThumbnail(Info.B_AVATAR);
        embedBuilder.setTimestamp(Instant.now());
        
        try {
            embedBuilder.setImage(WebScraper.getYouTubeThumbNail(track.getInfo().uri));
        } catch (IOException ex) {
            SmartLogger.errorLog(ex, e, "Music#trackInfo", "IOException on getting thumbnail of " + track.getInfo().uri);
        }
        
        e.getTextChannel().sendMessage(embedBuilder.build()).queue();
        embedBuilder.clearFields();
    }
    
    public static void queueList(MessageReceivedEvent e)
    {
        BlockingQueue<AudioTrack> queue = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue();
        Iterator<AudioTrack> list = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueueIterator();
        ArrayList<String> queuer = Main.guilds.get(e.getGuild().getId()).getScheduler().getRequester();
        
        EmbedBuilder embed = new EmbedBuilder();
        
        //Now Playing
        AudioTrack playing = Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack();
        Long position = 0L, duration = 0L;
        
        if(playing == null)
        {
            embed.addField("Now Playing", "None", false);   
        }
        else
        {
            String ptitle = playing.getInfo().title;
            String purl = playing.getInfo().uri;
            embed.addField("Now Playing", "[" + ptitle + "](" + purl + ")  \n"
                    + "Requested by " + queuer.get(0), false);
            
            //Current Position / Total Duration
            position = playing.getPosition();
            duration += playing.getDuration();
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
                String requester = queuer.get(count);
                songs += "**" + count + ".** [" + title + "](" + url + ")  Requested by " + requester + "\n";
                duration += track.getDuration();
            }
        }
        
        embed.setAuthor("Queue List (" + 
                (position/60000)%60 + ":" + (position/1000)%60 + " / " +
                + (duration/60000)%60 + ":" + (duration/1000)%60 + ")"
                , Info.B_INVITE, Info.B_AVATAR);
        embed.setColor(UtilTool.setColor());
        embed.setThumbnail(Info.B_AVATAR);
        embed.setFooter("Reqested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
        embed.setTimestamp(Instant.now());
        embed.addField("Coming Next", songs, false);
        
        e.getChannel().sendMessage(embed.build()).queue();
        embed.clearFields();
    }
}
