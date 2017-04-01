/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Config.Emoji;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Game1 {
    private static MessageReceivedEvent e;
    private static User starter;
    private static User opponent;
    private static User turn;
    
    /*
    constructor
    public Game1(MessageReceivedEvent event)
    {
        e = event;
        
        startGame();
    }*/
    
    public void startGame() //Keep the game run'in
    {
        e.getChannel().sendMessage("Game Mode ON!\nInput: row column").queue();
        turn = starter;
    }
    
    public void endGame() //Stop da game
    {
        if(e.getAuthor() == starter || e.getAuthor() == opponent)
            e.getChannel().sendMessage(Emoji.E_success + " Game Ended!").queue();
    }
    
    public void sendInput(String[] in, MessageReceivedEvent event)  //Set the input called by a Command class
    {
    
    }
    
    public void switchTurn() //Switch turn between starter to opponent
    {
        if("starter".equals(turn)) 
            turn = opponent;
        else
            turn = starter;
    }
}
