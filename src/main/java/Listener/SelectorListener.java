/* 
 * AIBot by AlienIdeology
 * 
 * Selector Listener
 * Send selections to specific selector in some Command Class
 */
package Listener;

import Command.MusicModule.PlayCommand;
import Main.Main;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SelectorListener extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        //Reject Commands from Bots and Fake Users
        if(e.getAuthor().isBot() || e.getAuthor().isFake())
            return;
        
        //Selector
        char choice = '\u0000';
        if(e.getMessage().getContent().length() > 0)
            choice = e.getMessage().getContent().charAt(0);
        String message = e.getMessage().getContent();
        
        PlayCommand.selector(message, choice, e);
    }
}
