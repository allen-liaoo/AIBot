/* 
 * AIBot by AlienIdeology
 * 
 * ConsoleListener
 * Listener for console commands
 */
package Listener;

import Main.Main;
import java.util.Scanner;
import net.dv8tion.jda.core.OnlineStatus;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ConsoleListener implements Runnable {
    
    private Thread t;
    private final String threadName = "Console Listener Thread";
    
    public ConsoleListener()
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
                Main.shutdown();
            }
            
            //Test Commands
            else if(input.startsWith("test"))
            {
                System.out.println("Test wot?");
            }
            
            //Presence
            //-SetGame
            else if(input.startsWith("setGame"))
            {
                System.out.println("Game set to " + Main.setGame(input.substring(8)));
            }
            
            //-SetStatus
            else if(input.startsWith("setStatus"))
            {
                try {
                    OnlineStatus status = Main.setStatus(input.substring(10));
                    System.out.println("Status set to " + status.toString());
                } catch (IllegalArgumentException iae) {
                    System.out.println("Please enter a valid status.");
                }
            }
        }
    }
}
