/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Resource.Emoji;
import Resource.Info;
import Resource.Prefix;
import Game.TicTacToe;
import Main.*;
import static Command.Command.embed;
import Setting.SmartLogger;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TicTacToeCommand implements Command {

    public final static String HELP = "Play a Tic Tac Toe game with anyone!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "tictactoe` or `" + Prefix.getDefaultPrefix() + "ttt`\n"
                                    + "Parameter: `-h | start @mention | x y | end | null`\n"
                                    + "start @mention: Mention an opponent to start the game.\n"
                                    + "x y: Type in the cordinate of the Tic Tac Toe board\n"
                                    + "end: End the game.";
    TicTacToe game;
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Tic Tac Toe -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length > 0 && "start".equals(args[0]))
        {
            game = new TicTacToe(e);
        }
        
        else if(args.length > 0 && "end".equals(args[0]))
        {
            game.endGame();
        }
        
        else
        {
            try {
                game.sendInput(args, e);
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.error + "Game haven't started yet!").queue();
                SmartLogger.errorLog(en, e.getGuild().getName(), this.getClass().getName(), "Game haven't started.");
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
}
