package audio;

import java.util.*;

/**
 * Created by liaoyilin on 5/3/17.
 */
public class QueueList extends LinkedList<AudioTrackWrapper> {

    public QueueList() {
    }

    /**
     * Find a AudioTrackWrapper's index by Keyword
     * @param keyword
     * @return
     */
    public int find(String keyword) {
        int count = -1;
        for(AudioTrackWrapper each : this) {
            count++;
            if(each.getTrack().getInfo().title.toLowerCase().contains(keyword.toLowerCase())) {
                return count;
            }
        }
        return -1;
    }

    /**
     * Shuffle the queue.
     */
    public void shuffle() {
        Collections.shuffle(this);
    }

}
