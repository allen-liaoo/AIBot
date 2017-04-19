/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.ModerationModule;

import Resource.Emoji;
import Setting.Prefix;
import Resource.Constants;
import Command.Command;
import Main.*;
import Utility.SmartLogger;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class PruneCommand implements Command{
    
    public final static  String HELP = "This command is for deleting messages.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"prune` or `" + Prefix.getDefaultPrefix() + "p`\n"
                                     + "Parameter: `-h | Number`";
    private final EmbedBuilder embed = new EmbedBuilder();

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.setTitle("Prune -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            e.getChannel().sendMessage(Emoji.error + " You must add a number after Prune command to delete an amount of messages.\n"
                                         + "Use `" + Prefix.getDefaultPrefix() + "prune -h` for help.").queue();
        }
        else if("-h".equals(args[0]))
        {
            help(e);
        }
        else
        {
            //Parse String to int, detect it the input is valid.
            Integer msgs = new Integer(0);
            try {
                msgs = Integer.parseInt(args[0]);
                SmartLogger.commandLog(e, "PruneCommand", "Called to prune " + msgs + " messages.");
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.error + " Please enter a number.").queue();
            } 
            
            try {
                e.getChannel().getHistory().retrievePast(msgs + 1).queue((List<Message> messages) -> messages.forEach((Message msg) ->
                {
                    try {
                        msg.delete().queue();
                    } catch (PermissionException pe) {
                        e.getChannel().sendMessage(Emoji.error + " I need to have **Manage Messages** Permission to delete messages.").queue();
                        return;
                    }
                })
                );
            } catch (IllegalArgumentException iae) { //Detect if the number is in range.
                e.getChannel().sendMessage(Emoji.error + " Please enter a number between **1 ~ 100**.").queue();
            } 
            
            //Delay the message deletion.
            try {
                Thread.sleep(1000);
                //Only show this message when 100 > msgs > 0.
                if(msgs < 100 && msgs > 0) e.getChannel().sendMessage(Emoji.success + " `" + args[0] + "` messages deleted.").complete().delete().complete();
                //Thread.sleep(2000);
            } catch (InterruptedException ite) {
                SmartLogger.errorLog(ite, e, this.getClass().getName(), "Pruning interrupted.");
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
