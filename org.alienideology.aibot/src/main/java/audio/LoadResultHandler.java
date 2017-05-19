package audio;

import system.AILogger;
import constants.Emoji;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Created by liaoyilin on 5/4/17.
 */
public abstract class LoadResultHandler implements AudioLoadResultHandler {

    private final GuildPlayer scheduler;
    private TextChannel tc;

    public LoadResultHandler (GuildPlayer scheduler) {
        this.scheduler = scheduler;
        this.tc = scheduler.getTc();
    }

    @Override
    public abstract void trackLoaded(AudioTrack track);

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        scheduler.addPlayList(playlist, scheduler.getMode().toString());
    }

    @Override
    public void noMatches() {
        tc.sendMessage(Emoji.ERROR + " No results for the provided url.").queue();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        tc.sendMessage(Emoji.ERROR + " Fail to load the song.").queue();
        AILogger.errorLog(exception, null, this.getClass().getName(), "Failed to load a song");
    }
}
