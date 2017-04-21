/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public interface Game {
    public void startGame(); //Keep the game run'in
    
    /*{
        e.getChannel().sendMessage("Game Mode ON!\nInput: row column").queue();
        turn = starter;
    }*/
    
    public void endGame(); //Stop da game
    
    /*{
        if(e.getAuthor() == starter || e.getAuthor() == opponent)
            e.getChannel().sendMessage(Emoji.E_success + " Game Ended!").queue();
    }*/
    
    public void sendInput(String[] in, MessageReceivedEvent event);  //Set the input called by a Command class

    
    /*{
    public void switchTurn();
        if("starter".equals(turn)) 
            turn = opponent;
        else
            turn = starter;
    }*/
}
