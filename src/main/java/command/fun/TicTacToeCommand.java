/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.fun;

import command.Command;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import game.TicTacToe;
import system.AILogger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TicTacToeCommand extends Command {

    public final static String HELP = "Play a Tic Tac Toe game with anyone!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "tictactoe` or `" + Prefix.getDefaultPrefix() + "ttt`\n"
                                    + "Parameter: `-h | start @mention | x y | end | null`\n"
                                    + "start @mention: Mention an opponent to start the game.\n"
                                    + "x y: Type in the cordinate of the Tic Tac Toe board.\n"
                                    + "end: End the game.";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Tic Tac Toe -Help", HELP, true);
        embed.setFooter("command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        TicTacToe game = null;
        
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length > 0 && "start".equals(args[0]))
        {
            AILogger.commandLog(e, this.getClass().getName(), "TicTacToe Started.");
            game = new TicTacToe(e);
        }
        
        else if(args.length > 0 && "end".equals(args[0]))
        {
            try {
                game.endGame();
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " game haven't started yet!").queue();
                AILogger.errorLog(en, e, this.getClass().getName(), "game haven't started.");
            }
        }
        
        else
        {
            try {
                game.sendInput(args, e);
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " game haven't started yet!").queue();
                AILogger.errorLog(en, e, this.getClass().getName(), "game haven't started.");
            }
        }
    }

}
