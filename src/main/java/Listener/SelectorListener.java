/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Listener;

import Command.MusicModule.PlayCommand;
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
        
        char choice = e.getMessage().getContent().charAt(0);
        if(e.getMessage().getContent().length() == 1 && Character.isDigit(choice))
        {
            if(PlayCommand.selecter.containsKey(e.getGuild().getId()))
            {
                if(PlayCommand.selecter.containsValue(e.getAuthor()))
                {
                    PlayCommand.selector(choice, e);
                    System.out.println("got here");
                }
            }
        }
    }
}
