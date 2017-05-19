package system;

import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple voting system. (Like vote skip)
 * Does not support multiple choices
 * Created by liaoyilin on 5/5/17.
 */
public abstract class AIVote {

    private List<User> votes;

    public AIVote() {
        this.votes = new ArrayList();
    }

    /**
     * Get the votes of a specific choice
     * @param voter
     * @return
     */
    public boolean addVote(User voter) {
        if(!votes.contains(voter)) {
            votes.add(voter);
            return true;
        }
        return false;
    }

    public abstract int getRequiredVote();

    public List<User> getVotes() {
        return votes;
    }

    public int getVoteSize() {
        return votes.size();
    }

    public void clear() {
        votes.clear();
    }

}
