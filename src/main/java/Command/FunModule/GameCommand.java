/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Setting.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GameCommand extends Command {

    public final static String HELP = "*Command Group:* Game\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "game`\n"
                                    + "Parameter: `-h | [Number] | null`\n"
                                    + "[Number]: Choose the games to get help message.";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Game -Help", HELP, true);
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
            e.getChannel().sendMessage("Choose a game below to get its help message:\n"
                                    + "1: Rock Paper Scissors\n"
                                    + "2: Guess Number\n"
                                    + "3: Tic Tac Toe\n"
                                    + "4: Hang Man\n"
                                    + "Hint: Use `" + Prefix.getDefaultPrefix() + "game [Number]` to choose.").queue();
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

    
}
