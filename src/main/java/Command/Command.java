/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Command.*;
import Main.*;
import net.dv8tion.jda.core.EmbedBuilder;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public interface Command {
    
    //Custom help String.
    public final static String HELP = "";
    
    //Embed Message for help String.
    public final EmbedBuilder embed = new EmbedBuilder();

    // Call command
    public boolean called(String[] args, MessageReceivedEvent e);
    
    // Command Usage.
    public void help(MessageReceivedEvent e);
    
    // Command Responses and actions.
    public void action(String[] args, MessageReceivedEvent e);
    
    public void executed(boolean success, MessageReceivedEvent e);
    
}
