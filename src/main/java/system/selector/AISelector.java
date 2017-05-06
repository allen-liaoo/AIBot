/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package system.selector;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public abstract class AISelector<T> {

    private Message message;
    private Member author;
    private Guild guild;
    private MessageChannel channel;
    
    public AISelector(Message msg, Member ar) {
        this.message = msg;
        this.author = ar;
        this.guild = msg.getGuild();
        this.channel = msg.getChannel();
    }
    
    public AISelector() {
        this.message = null;
        this.author = null;
        this.guild = null;
        this.channel = null;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public MessageChannel getchannel() {
        return channel;
    }

    public void setchannel(MessageChannel textchannel) {
        this.channel = textchannel;
    }

    /**
     * Check if this is a valid selection for Generics
     * @param event
     * @return
     */
    public abstract <T> boolean isSelection(GenericMessageEvent event);

    public abstract int selector(String choice);
    
    protected boolean isSamePlace(Guild g, TextChannel tc) {
        return g.getId().equals(this.guild.getId()) && tc.getId().equals(this.channel.getId());
    }
    
    protected boolean isSameAuthor(Member mem) {
        return mem.getUser().getId().equals(this.author.getUser().getId());
    }
    
}
