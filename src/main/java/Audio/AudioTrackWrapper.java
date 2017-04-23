/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Audio;

import Utility.UtilNum;
import Utility.UtilString;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class AudioTrackWrapper {
    AudioTrack track;
    String requester;
    TrackType type;
    
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
    
    public AudioTrack getTrack() {
        return track;
    }

    public void setTrack(AudioTrack track) {
        this.track = track;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public TrackType getType() {
        return type;
    }

    public void setType(TrackType type) {
        this.type = type;
    }
}
