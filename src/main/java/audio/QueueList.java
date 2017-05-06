package audio;

import java.util.*;

/**
 * Created by liaoyilin on 5/3/17.
 */
public class QueueList extends LinkedList<AudioTrackWrapper> {

    private LinkedList<AudioTrackWrapper> queue;

    public QueueList() {
        this.queue = new LinkedList<>();
    }

    /**
     * Find a AudioTrackWrapper's index by Keyword
     * @param keyword
     * @return
     */
    public int findIndex(String keyword) {
        return queue.indexOf(find(keyword));
    }

    /**
     * Find a AudioTrackWrapper by Keyword
     * @param keyword
     * @return
     */
    private AudioTrackWrapper find(String keyword) {
        for(AudioTrackWrapper each : queue) {
            if(each.getTrack().getInfo().title.toLowerCase().contains(keyword.toLowerCase()))
                return each;
        }
        return null;
    }

    /**
     * Shuffle the queue.
     */
    public void shuffle() {
        Collections.shuffle(queue);
    }
}
