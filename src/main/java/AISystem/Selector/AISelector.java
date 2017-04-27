/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package AISystem.Selector;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

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
    
    public abstract int selector(String choice);

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
     * Check if this is a valid selection for MessageReceivedEvent
     * @param e
     * @return
     */
    public boolean isSelection(MessageReceivedEvent e) {
        return !(!isSamePlace(e) || !isSameAuthor(e.getMember()));
    }
    
    private boolean isSamePlace(MessageReceivedEvent e) {
        return e.getGuild().getId().equals(this.guild.getId()) && e.getChannel().getId().equals(this.channel.getId());
    }
    
    /**
     * Check if this is a valid selection for MessageReactionAddEvent
     * @param e
     * @return
     */
    public boolean isSelection(MessageReactionAddEvent e) {
        return !(!isSamePlace(e) || !isSameAuthor(e.getMember()));
    }
    
    private boolean isSamePlace(MessageReactionAddEvent e) {
        return e.getGuild().getId().equals(this.guild.getId()) && e.getChannel().getId().equals(this.channel.getId());
    }
    
    private boolean isSameAuthor(Member mem) {
        return mem.getUser().getId().equals(this.author.getUser().getId());
    }
    
}
