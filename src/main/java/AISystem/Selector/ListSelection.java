package AISystem.Selector;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.util.List;

	/**
	 *
	 * @author Alien Ideology <alien.ideology at alien.org>
	 */
public abstract class ListSelection extends AISelector {

    private List<String> option;

    /**
     * Parametric Constructor
     * @param msg
     * @param ar
     * @param opt
     */
    public ListSelection(Message msg, Member ar, List<String> opt) {
        super(msg, ar);
        this.option = opt;
    }

    public ListSelection() {
        super();
    }

    /**
     * Filter the event and return selection
     * @param event
     * @return boolean
     */
    @Override
    public boolean isSelection(GenericMessageEvent event) {
        if(event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            if (!isSameAuthor(e.getMember()) || !isSamePlace(e.getGuild(), e.getTextChannel()))
                return false;
            if (!e.getMessageId().equals(this.getMessage().getId()))
                return false;
            for (Object unicode : option) {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns the selection
     * @param choice
     * @return a index of the options, or -1 for no result
     */
    @Override
    public int selector(String choice) {
        for(int i = 0; i < option.size(); i++) {
            if(option.get(i) == null ? choice == null : option.get(i).equals(choice))
                return i;
        }
        return -1;
    }

    /**
     * Specified actions of a selector
     * @param chose
     */
    public abstract void action(int chose);

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

    public void addOption(String option) {
        this.option.add(option);
    }

}
