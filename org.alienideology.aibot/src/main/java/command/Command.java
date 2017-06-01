/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import java.awt.Color;
import java.time.Instant;

import constants.Global;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public abstract class Command {
    
    /**
     * Command Usage
     * @param e the value of e
     * @return the net.dv8tion.jda.core.EmbedBuilder
     */
    public EmbedBuilder help(MessageReceivedEvent e) {
        return (new EmbedBuilder()
            .setColor(Global.B_COLOR)
            .setTimestamp(Instant.now())
            .setFooter("Command Help/Usage",null));
    }
    
    /**
     * Command Responses and actions
     * @param args
     * @param e
     */
    public abstract void action(String[] args, MessageReceivedEvent e);
    
}
