/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import static Command.Command.embed;
import Game.GuessNumber;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import Utility.AILogger;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GuessNumberCommand implements Command{

    public final static String HELP = "Play Number Guessing Game with the bot!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "guessnum` or `" + Prefix.getDefaultPrefix() + "gn`\n"
                                    + "Parameter: `-h | start | end | [Number] | null`\n"
                                    + "start or null: Start the game.\n"
                                    + "end: End the game.\n"
                                    + "[Number]: Guess a number.\n";
    
    private GuessNumber gn;
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Fun Module", null);
        embed.addField("Guess Number -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0]))
        {
            help(e);
        }
        
        else if(args.length == 0 || (args.length > 0 && "start".equals(args[0])))
        {
            gn = new GuessNumber(e);
        }
        
        else if(args.length > 0 && "end".equals(args[0]))
        {
            gn.endGame();
            e.getChannel().sendMessage("Game ended! The number was " + gn.getNumber() + ".").queue();
        }
        
        else
        {
            try {
                if(!gn.isEnded)
                    gn.sendInput(args, e);
                else
                    e.getChannel().sendMessage(Emoji.ERROR + " Game haven't started yet!").queue();
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " Game haven't started yet!").queue();
                AILogger.errorLog(en, e, this.getClass().getName(), "Game haven't started.");
            }
        }
    }

    
}
