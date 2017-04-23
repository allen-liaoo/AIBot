/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.RestrictedModule;

import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Main.*;
import Utility.AILogger;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class ShutDownCommand implements Command{

    public final static  String HELP = "This command is for shutting down the bot remotely. **(Server Owner Only)**\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"shutdown`\n"
                                     + "Parameter: `-h | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Restricted Module", null);
        embed.addField("ShutDown -Help", HELP, true);
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
            if(Constants.D_ID.equals(e.getAuthor().getId()))
            {
                e.getChannel().sendMessage(Emoji.SUCCESS + " Shutting down...").queue();

                try {
                    Thread.sleep(2000);
                    
                    AILogger.updateLog("Bot Shut Down Attemp");
                    Main.shutdown();
                } catch (InterruptedException ite) {
                    AILogger.errorLog(ite, e, this.getClass().getName(), "Thread Sleep process interrupted.");
                } catch (IOException ex) {
                    Logger.getLogger(ShutDownCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for **Bot Owner** only!").queue();
                
        }

        else if("-h".equals(args[0])) 
        {
            help(e);
        }
    }

    
}
