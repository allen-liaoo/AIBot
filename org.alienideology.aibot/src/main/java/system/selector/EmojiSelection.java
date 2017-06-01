/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package system.selector;

import java.util.List;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public abstract class EmojiSelection extends AISelector {

    private List<String> option;

    /**
     * Parametric Constructor
     * @param msg
     * @param ar
     * @param opt
     */
    public EmojiSelection(Message msg, Member ar, List<String> opt) {
        super(msg, ar);
        this.option = opt;
    }

    public EmojiSelection() {
        super();
    }

    /**
     * Filter the event and return selection
     * @param event
     * @return boolean
     */
    @Override
    public boolean isSelection(GenericMessageEvent event) {
        if(event instanceof MessageReactionAddEvent) {
            MessageReactionAddEvent e = (MessageReactionAddEvent) event;
            if (e.isFromType(ChannelType.PRIVATE)) {
                return option.contains(e.getReaction().getEmote().getName());
            } else {
                if (!isSameAuthor(e.getMember()) || !isSamePlace(e.getGuild(), e.getTextChannel()))
                    return false;
                if (!e.getMessageId().equals(this.getMessage().getId()))
                    return false;
                if (e.getReaction().isSelf())
                    return false;
                return option.contains(e.getReaction().getEmote().getName());
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
