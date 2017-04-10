/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import static Command.Command.embed;
import Resource.Info;
import Resource.Prefix;
import Game.*;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GameCommand implements Command {

    public final static String HELP = "*Command Group:* Game\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "game`\n"
                                    + "Parameter: `-h | [Number] | null`\n"
                                    + "[Number]: Choose the games to get help message.";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Game -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            e.getChannel().sendMessage("Choose a game below to get its help message:\n"
                                    + "1: Rock Paper Scissors\n"
                                    + "2: Guess Number\n"
                                    + "3: Tic Tac Toe\n"
                                    + "4: Hang Man\n"
                                    + "Hint: Use `" + Prefix.getDefaultPrefix() + "game [Number]` to choose.").queue();
        }
        
        else if("-h".equals(args[0])) 
        {
            help(e);
        }
        
        else
        {
            if("1".equals(args[0]))
            {
                RPSCommand rps = new RPSCommand();
                rps.help(e);
            }
            else if("2".equals(args[0]))
            {
                GuessNumberCommand ttt = new GuessNumberCommand();
                ttt.help(e);
            }
            else if("3".equals(args[0]))
            {
                TicTacToeCommand ttt = new TicTacToeCommand();
                ttt.help(e);
            }
            
            else if("4".equals(args[0]))
            {
                HangManCommand hm = new HangManCommand();
                hm.help(e);
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
