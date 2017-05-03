/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Constants.Emoji;
import Constants.Global;
import Setting.Prefix;
import Game.HangMan;
import AISystem.AILogger;

import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class HangManCommand extends Command{
    public final static String HELP = "Play Hang Man with anyone!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "hangman` or `" + Prefix.getDefaultPrefix() + "hm`\n"
                                    + "Parameter: `-h | start | [letter] | end | null`\n"
                                    + "start: Start the game.\n"
                                    + "[letter]: Type in the letter to guess.\n"
                                    + "end: End the game.";
    
    HangMan game;

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("HangMan -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        embed.setTimestamp(Instant.now());
        return embed;
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
