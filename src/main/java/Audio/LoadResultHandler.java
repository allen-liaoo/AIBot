package Audio;

import AISystem.AILogger;
import Audio.AudioTrackWrapper.TrackType;
import Constants.Emoji;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Created by liaoyilin on 5/4/17.
 */
public class LoadResultHandler implements AudioLoadResultHandler {

    private String requester;
    private TrackType type;
    private TextChannel tc;

    public LoadResultHandler (String requester, TrackType type, TextChannel tc) {
        this.requester = requester;
        this.type = type;
        this.tc = tc;
    }

    @Override
    public void trackLoaded(AudioTrack track) {

    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        addPlayList(playlist, requester);
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
