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
    /*
    @Override
    public void sort(Comparator c) {
        Collections.sort(queue, c);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public AudioTrackWrapper get(int index) {
        return queue.get(index);
    }

    @Override
    public AudioTrackWrapper set(int index, AudioTrackWrapper element) {
        return queue.set(index, element);
    }

    @Override
    public AudioTrackWrapper remove(int index) {
        return queue.remove(index);
    }

    @Override
    public Object clone() {
        return queue.clone();
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return queue.subList(fromIndex, toIndex);
    }

    @Override
    public AudioTrackWrapper peek() {
        return queue.peek();
    }

    @Override
    public AudioTrackWrapper poll() {
        return queue.poll();
    }

    @Override
    public AudioTrackWrapper remove() {
        return queue.remove();
    }

    @Override
    public boolean offer(AudioTrackWrapper o) {
        return queue.offer(o);
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public Iterator iterator() {
        return queue.iterator();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void clear() {
        queue.clear();
    }
    */

}
