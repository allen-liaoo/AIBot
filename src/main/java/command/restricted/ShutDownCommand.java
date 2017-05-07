/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.restricted;

import command.Command;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import main.*;
import system.AILogger;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class ShutDownCommand extends Command{

    public final static  String HELP = "This command is for shutting down the bot remotely. **(Server Owner Only)**\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"shutdown`\n"
                                     + "Parameter: `-h | null`";
    
    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Restricted Module", null);
        embed.addField("ShutDown -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0) 
        {
            if(Global.D_ID.equals(e.getAuthor().getId()))
            {
                e.getChannel().sendMessage(Emoji.SUCCESS + " Shutting down...").queue();

                try {
                    Thread.sleep(2000);
                    
                    AILogger.updateLog("Bot Shut Down Attemp");
                    AIBot.shutdown();
                } catch (InterruptedException ite) {
                    AILogger.errorLog(ite, e, this.getClass().getName(), "Thread Sleep process interrupted.");
                } catch (IOException ex) {
                    Logger.getLogger(ShutDownCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(!"-h".equals(args[0])) 
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for **Bot Owner** only!").queue();
                
        }
    }
}
