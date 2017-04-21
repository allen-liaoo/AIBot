/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Resource.Emoji;
import Utility.UtilNum;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GuessNumber implements Game{

    public boolean isEnded = false;
    private int number = 0;
    private int count = 8;
    private MessageReceivedEvent e;
    
    public GuessNumber(MessageReceivedEvent event)
    {
        e = event;
        
        startGame();
    }
    
    @Override
    public void startGame() {
        number = UtilNum.randomNum(0, 100);
        
        e.getChannel().sendMessage(Emoji.number + " Guess a number between 0 and 100! You have " + count  + " chances.").queue();
    }

    @Override
    public void endGame() {
        number = 0;
        count = 8;
        isEnded = true;
    }

    @Override
    public void sendInput(String[] in, MessageReceivedEvent event) {
        int innum = Integer.parseInt(in[0]);
        
        if(isEnded)
        {
            e.getChannel().sendMessage(Emoji.error + "Game haven't started yet!").queue();
            return;
        }
        
        count--;
        if(innum == number)
        {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " won! The number was " + number + ".").queue();
            endGame();
            return;
        }
        
        else if(count == 0)
        {
            e.getChannel().sendMessage(Emoji.stopwatch + " Time's up! The number was " + number + ".").queue();
            endGame();
            return;
        }
        
        else if(innum < number)
        {
            e.getChannel().sendMessage("Higher! " + Emoji.up + "\nYou got " + count + " chances left.").queue();
        }
        
        else if(innum > number)
        {
            e.getChannel().sendMessage("Lower! " + Emoji.down + "\nYou got " + count + " chances left.").queue();
        }
    }
    
    public int getNumber() {
        return number;
    }
    
}
