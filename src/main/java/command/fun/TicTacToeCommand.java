/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.fun;

import command.Command;
import constants.Emoji;
import constants.Global;
import main.AIBot;
import main.GuildWrapper;
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
                                    + "x y: Type in the coordinate of the Tic Tac Toe board.\n"
                                    + "end: End the game.";
    private TicTacToe game = null;

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Fun Module", null);
        embed.addField("Tic Tac Toe -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        GuildWrapper wrapper = AIBot.getGuild(e.getGuild());

        if(args.length == 0) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        
        else if(e.getMessage().getMentionedUsers().size() == 1) {
            wrapper.setTicTacToe(new TicTacToe(e));
        }
        
        else if("end".equals(args[0])) {
            try {
                wrapper.getTicTacToe().endGame();
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " Game haven't started yet!").queue();
            }
        }
        
        else {
            try {
                wrapper.getTicTacToe().sendInput(args, e);
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " Game haven't started yet!").queue();
            }
        }
    }

}
