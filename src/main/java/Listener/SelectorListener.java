/* 
 * AIBot by AlienIdeology
 * 
 * Selector Listener
 * Send selections to specific selector in some Command Class
 */
package Listener;

import system.selector.EmojiSelection;
import command.music.PlayCommand;
import java.util.HashMap;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SelectorListener extends ListenerAdapter {
    
    private static HashMap<String, EmojiSelection> emojiSelector = new HashMap<String, EmojiSelection>();
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        /**
         * Reject Commands from Bots and Fake Users.
         */
        if(e.getAuthor().isBot() || e.getAuthor().isFake())
            return;
        
        /**
         * Reject Commands from unavailable guild, Text Channels that the bot 
         * does not have permission to send message or fake PrivateConstant Channels.
         */
        if(e.getChannelType().isGuild() && !e.getGuild().isAvailable() ||
            (e.getChannelType().isGuild() && !e.getTextChannel().canTalk()) || 
            (!e.getChannelType().isGuild() && e.getPrivateChannel().isFake()))
            return;
        
        //selector
        char choice = '\u0000';
        if(e.getMessage().getContent().length() > 0)
            choice = e.getMessage().getContent().charAt(0);
        String message = e.getMessage().getContent();
        
        PlayCommand.selector(message, choice, e);
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(emojiSelector.containsKey(event.getUser().getId())) {
            EmojiSelection selection = emojiSelector.get(event.getUser().getId());
            if(selection.isSelection(event)) {
                selection.action(selection.selector(event.getReactionEmote().getName()));
                emojiSelector.remove(event.getUser().getId());
            }
        }
    }
    
    public static void addEmojiSelection(String author, EmojiSelection select)
    {
        if(select.getGuild().getSelfMember().hasPermission((Channel) select.getchannel(), Permission.MESSAGE_ADD_REACTION)) {
            for(String em : select.getOption()) {
                select.getMessage().addReaction(em).queue();
            }
            emojiSelector.put(author, select);
        }
    }
    
}
