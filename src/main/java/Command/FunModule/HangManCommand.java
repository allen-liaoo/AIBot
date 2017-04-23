/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Game.HangMan;
import Utility.AILogger;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class HangManCommand implements Command{
    public final static String HELP = "Play Hang Man with anyone!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "hangman` or `" + Prefix.getDefaultPrefix() + "hm`\n"
                                    + "Parameter: `-h | start | [letter] | end | null`\n"
                                    + "start: Start the game.\n"
                                    + "[letter]: Type in the letter to guess.\n"
                                    + "end: End the game.";
    
    HangMan game;

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("HangMan -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
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
            AILogger.commandLog(e, this.getClass().getName(), "HangMan Started.");
            game = new HangMan(e);
        }
        
        else if(args.length > 0 && "end".equals(args[0]))
        {
            if(e.getAuthor() == HangMan.starter)
                game.endGame();
            else
                e.getChannel().sendMessage(Emoji.ERROR + " Only the game starter can end the game.").queue();
        }
        
        else
        {
            try {
                game.sendInput(args, e);
            } catch(NullPointerException en) {
                e.getChannel().sendMessage(Emoji.ERROR + " Game haven't started yet!").queue();
                AILogger.errorLog(en, e, this.getClass().getName(), "Game haven't started.");
            }
        }
    }

    
}
