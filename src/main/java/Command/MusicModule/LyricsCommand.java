/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class LyricsCommand implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
