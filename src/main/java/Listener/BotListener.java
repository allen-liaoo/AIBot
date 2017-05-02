/* 
 * AIBot by AlienIdeology
 * 
 * ConsoleListener
 * Listener for console commands
 */
package Listener;

import Main.Main;
import Utility.LyricsSearch;
import Utility.UtilBot;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class BotListener extends ListenerAdapter implements Runnable {
    
    private Thread t;
    private final String threadName = "Console Listener Thread";
    
    public BotListener()
    {
        t = new Thread(this,threadName);
        t.start();
    }
    
    public void start()
    {
        if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
    }
    
    @Override
    public void run() 
    {
        Scanner scanner = new Scanner(System.in);
        while (true) 
        {
            System.out.print("AIBot Console $ ");
            String input = scanner.nextLine();
            
            //ShutDown
            if (input.equals("shutdown")) 
            {
                try {
                    Main.shutdown();
                } catch (IOException ex) {
                    Logger.getLogger(BotListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //Test Commands
            else if(input.startsWith("test"))
            {
                System.out.println("Test wot?");
                LyricsSearch.getSongLyrics();
            }
            
            //Presence
            //-SetGame
            else if(input.startsWith("setGame"))
            {
                System.out.println("Game set to " + UtilBot.setGame(input.substring(8)));
            }
            
            //-SetStatus
            else if(input.startsWith("setStatus"))
            {
                try {
                    OnlineStatus status = UtilBot.setStatus(input.substring(10));
                    System.out.println("Status set to " + status.toString());
                } catch (IllegalArgumentException iae) {
                    System.out.println("Please enter a valid status.");
                }
            }
        }
    }

    @Override
    public void onException(ExceptionEvent event) {
        super.onException(event);
        if(!event.isLogged())
            System.out.println(event.getCause());
    }
}
