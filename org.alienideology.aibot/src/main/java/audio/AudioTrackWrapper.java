/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package audio;

import utility.UtilString;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AudioTrackWrapper {
    private final AudioTrack track;
    private final String requester;
    private final TrackType type;
    
    public enum TrackType {
        NORMAL_REQUEST,
        PLAYLIST,
        FM,
        RADIO;
        
        @Override
        public String toString() {
            return UtilString.VariableToString("_", name());
        }
    }
    
    public AudioTrackWrapper(AudioTrack track, String requester, TrackType type)
    {
        this.track = track;
        this.requester = requester;
        this.type = type;
    }
    
    public AudioTrackWrapper()
    {
        this.track = null;
        this.requester = null;
        this.type = null;
    }

    public boolean isEmpty() {
        if(track == null)
            return true;
        return false;
    }
    
    public AudioTrackWrapper makeClone() {
        return new AudioTrackWrapper(this.track.makeClone(), this.requester, this.type);
    }
    
    public AudioTrack getTrack() {
        return track;
    }

    public String getRequester() {
        return requester;
    }

    public TrackType getType() {
        return type;
    }
}
