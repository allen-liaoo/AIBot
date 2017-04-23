/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Audio.AudioTrackWrapper.TrackType;
import Main.Main;
import Resource.Emoji;
import Resource.Constants;
import Utility.UtilNum;
import Utility.WebScraper;
import Utility.AILogger;
import Utility.AIPages;
import Utility.UtilString;
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
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
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
        AudioConnection.connect(e, false);
        
        if(!e.getMember().getVoiceState().inVoiceChannel())
            return;
        
        AILogger.commandLog(e, "Music#Play", "Music played for " + link);
        
        if(m.find()){
            try {
                Music.playerManager.loadItemOrdered(Music.playerManager, link, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack track) {
                        Main.guilds.get(e.getGuild().getId()).getScheduler().queue
                                    (new AudioTrackWrapper(track, e.getAuthor().getName(), type), e);
                    }
                    
                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        Main.guilds.get(e.getGuild().getId()).getScheduler().addPlayList(playlist, e.getAuthor().getName());
                        e.getTextChannel().sendMessage(Emoji.success + " Queued Playlist: `" + playlist.getName() + "`").queue();
                    }
                    
                    @Override
                    public void noMatches() {
                        e.getTextChannel().sendMessage(Emoji.error + " No match found for " + link).queue();
                    }
                    
                    @Override
                    public void loadFailed(FriendlyException exception) {
                        e.getTextChannel().sendMessage(Emoji.error + " Fail to load the video " + link).queue();
                        AILogger.errorLog(exception, e, this.getClass().getName(), "Failed to load this video: " + link);
                    }
                }).get();
            } catch (InterruptedException ex) {
                AILogger.errorLog(ex, e, "Music#play", "Interrupted when retrieving AudioTrack");
            } catch (ExecutionException ex) {
                AILogger.errorLog(ex, e, "Music#play", "ExecutionException when retrieving AudioTrack");
            }
        }
        else
        {
            e.getTextChannel().sendMessage(Emoji.error + " No match found.").queue();
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
    
    public static void stop(MessageReceivedEvent e)
    {
        //Prevent user that is not in the same voice channel from stopping the player
        if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel() ||
                !e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
            e.getChannel().sendMessage(Emoji.error + " You and I are not in the same voice channel.").queue();
            return;
        }
        
        Main.guilds.get(e.getGuild().getId()).getScheduler().stopPlayer();
        AudioConnection.disconnect(e, false);
        e.getChannel().sendMessage(Emoji.stop + " Stopped the player, left the voice channel and cleared queue.").queue();
    }
    
    public static void setVolume(MessageReceivedEvent e, int in)
    {
        Main.guilds.get(e.getGuild().getId()).getPlayer().setVolume(in);
    }
    
    /**
     * Vote Skip System
     * @param e
     * @param position The position of the song
     * @param force force skip
     * @return 0 if the song is skipped
     * @return a number more than 0 for the required votes
     * @return -1 if the voter already voted
     * @return -2 if there is no song playing
     */
    public static int skip(MessageReceivedEvent e, int position, boolean force)
    {
        if(Main.guilds.get(e.getGuild().getId()).getScheduler().getNowPlayingTrack().isEmpty()) {
            return -2;
        }
        //Force skip the current song
        if(force) {
            Main.guilds.get(e.getGuild().getId()).getScheduler().nextTrack();
            Main.guilds.get(e.getGuild().getId()).getScheduler().clearVote();
            return 0;
        }
        
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
                for(Member m : members) {
                    if(!m.getUser().isBot())
                        mem++;
                }
                
                //Check if majority of the members agree to skip
                mem = (int) Math.ceil(mem / 2);
                if(votes >= mem) {
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
            BlockingQueue<AudioTrackWrapper> queue = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue();
            int countindex = 0;
            for(AudioTrackWrapper song : queue) {
                countindex++;
                if(countindex == position) {
                    queue.remove(song);
                }
            }
            return 0;
        }
        return -1;
    }
    
    /**
     * Track Information Getter
     * @param e
     * @param track Wrapper class for getting basic informations and requesters
     * @param title 
     */
    public static void trackInfo(MessageReceivedEvent e, AudioTrackWrapper track, String title)
    {   
        AudioTrackInfo trackInfo = track.getTrack().getInfo();
        String trackTime = UtilString.formatDuration(track.getTrack().getPosition());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(title, trackInfo.uri, Constants.B_AVATAR);
        embedBuilder.setColor(UtilNum.randomColor());
        embedBuilder.addField("Song Title:", trackInfo.title, true);
        embedBuilder.addField("Author:", trackInfo.author, true);
        embedBuilder.addField("Song Link:", trackInfo.uri, false);
        
        if(track.getType() != AudioTrackWrapper.TrackType.RADIO)
        {
            trackTime += " / " + UtilString.formatDuration(track.getTrack().getDuration());
        }
        
        embedBuilder.addField("Song Duration:", trackTime, true);
        embedBuilder.addField("Track Type:", track.getType().toString(), true);
        embedBuilder.addField("Stream:", UtilString.VariableToString(null, trackInfo.isStream+"") + "", true);
        embedBuilder.addField("Requested by:", track.getRequester(), true);
        embedBuilder.setThumbnail(Constants.B_AVATAR);
        embedBuilder.setTimestamp(Instant.now());
            
            
        try {
            embedBuilder.setImage(WebScraper.getYouTubeThumbNail(track.getTrack().getInfo().uri));
        } catch (IOException ex) {
            AILogger.errorLog(ex, e, "Music#trackInfo", "IOException on getting thumbnail of " + track.getTrack().getInfo().uri);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            
        }
        
        e.getTextChannel().sendMessage(embedBuilder.build()).queue();
        embedBuilder.clearFields();
    }
    
    public static void queueList(MessageReceivedEvent e, int page)
    {
        BlockingQueue<AudioTrackWrapper> queue = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue();
        Iterator<AudioTrackWrapper> list = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueueIterator();
        
        EmbedBuilder embed = new EmbedBuilder();
        
        //Now Playing
        AudioTrackWrapper playing = Main.guilds.get(e.getGuild().getId()).getScheduler().getNowPlayingTrack();
        Long position = 0L, duration = 0L;
        
        if(playing.isEmpty()) {
            embed.addField("Now Playing", "None", false);   
        }
        else
        {
            String ptitle = playing.getTrack().getInfo().title;
            String purl = playing.getTrack().getInfo().uri;
            embed.addField("Now Playing", "**[" + ptitle + "](" + purl + ")**\n"
                    + "Requested by `" + playing.getRequester() + "`  Type: `" + playing.getType().toString() + "`\n", false);
            
            //Current Position / Total Duration
            position = playing.getTrack().getPosition();
            TrackType TrackType = null;
            if(playing.getType() != TrackType.RADIO)
                duration += playing.getTrack().getDuration();
        }
        
        int count = 0;
        String songs = "";
        List<AudioTrackWrapper> queueList = new ArrayList();
        
        if(queue.peek() == null)
        {
            songs += "The queue is curently empty.";
            if(playing.isEmpty()) {
                e.getChannel().sendMessage("The queue is currently empty, and there is no song playing.").queue();
                return;
            }
            embed.addField("Coming Next", songs, false);
        }
        else
        {   
            //Initialize QueueList for AIPages. Add duration
            while(list.hasNext())
            {
                count++;

                AudioTrackWrapper trackwrapper = list.next();
                AudioTrack track = trackwrapper.getTrack();
                
                queueList.add(trackwrapper);
                if(trackwrapper.getType() != TrackType.RADIO)
                    duration += track.getDuration();
            }
            
            String mode = Main.guilds.get(e.getGuild().getId()).getScheduler().getMode().toString();
            songs += "**Player Mode:** " + mode + "\n**Queue Count:** " + count + "\n\n";
            
            //AIPages
            AIPages pages = new AIPages(queueList);
            List<AudioTrackWrapper> song = pages.getPage(page);
            
            //Add each queued songs to songQueue
            for(int i = 0; i < song.size(); i++) {
                AudioTrackWrapper wrap = song.get(i);
                String title = wrap.getTrack().getInfo().title;
                String url = wrap.getTrack().getInfo().uri;
                int index = (page-1) * pages.getPageSize() +1;
                songs += "`"+(i+index)+".` **["+title+"]("+url+")**\n";
            }
            embed.addField("Coming Next (Page " + page + " / " + pages.getPages() +")", songs, false);
        }
        
        String durationWithoutRadio = "";
        if("00:00".equals(UtilString.formatDuration(duration)))
            durationWithoutRadio = "";
        else
            durationWithoutRadio = " / " + UtilString.formatDuration(duration);
        
        embed.setAuthor("Queue List (" + 
                UtilString.formatDuration(position) + 
                durationWithoutRadio + ")"
                , Constants.B_INVITE, Constants.B_AVATAR);
        embed.setColor(UtilNum.randomColor());
        embed.setThumbnail(Constants.B_AVATAR);
        embed.setFooter("Reqested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
        embed.setTimestamp(Instant.now());
        
        e.getChannel().sendMessage(embed.build()).queue();
        embed.clearFields();
    }
}
