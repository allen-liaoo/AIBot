/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.fun;

import command.Command;
import constants.Global;
import game.GuessNumber;
import constants.Emoji;
import setting.Prefix;
import system.AILogger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GuessNumberCommand extends Command{

    public final static String HELP = "Play Number Guessing game with the bot!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "guessnum` or `" + Prefix.getDefaultPrefix() + "gn`\n"
                                    + "Parameter: `-h | start | end | [Number] | null`\n"
                                    + "start or null: Start the game.\n"
                                    + "end: End the game.\n"
                                    + "[Number]: Guess a number.\n";
    
    private GuessNumber gn;
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        
        embed.setTitle("Fun Module", null);
        embed.addField("Guess Number -Help", HELP, true);
        embed.setFooter("command Help/Usage", Global.I_HELP);
        return embed;
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
            e.getChannel().sendMessage("game ended! The number was " + gn.getNumber() + ".").queue();
        }
        
        else
        {
            try {
                if(!gn.isEnded)
                    gn.sendInput(args, e);
                else
                    e.getChannel().sendMessage(Emoji.ERROR + " game haven't started yet!").queue();
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " game haven't started yet!").queue();
                AILogger.errorLog(en, e, this.getClass().getName(), "game haven't started.");
            }
        }
    }

    
}
