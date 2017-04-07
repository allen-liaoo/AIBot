/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import Audio.Lyrics;
import static Listener.CommandListener.handleCommand;
import Main.Main;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.OnlineStatus;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ConsoleListener extends Thread {
    
    private final File mainLog = new File("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Resource/LogMain.txt");
    private final File errorLog = new File("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Resource/LogError.txt");
    
    public ConsoleListener()
    {
        run();
    }
    
    @Override
    public void run() 
    {
        Scanner scanner = new Scanner(System.in);
        while (true) 
        {
            System.out.print("AIBot Console $ ");
            String input = scanner.nextLine();
            if (input.equals("shutdown")) 
            {
                Main.shutdown();
            }
            else if (input.equals("lyric")) 
            {
                try {
                    System.out.println(Lyrics.getSongLyrics("U2", "With or Without You"));
                } catch (IOException ex) {
                    Logger.getLogger(ConsoleListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(input.startsWith("setGame"))
            {
                Main.setGame(input.substring(8));
                System.out.println("Game set to " + input.substring(8));
            }
            else if(input.startsWith("setStatus"))
            {
                OnlineStatus status;
                switch(input.substring(10)) {
                        case "online":
                            status = OnlineStatus.ONLINE;
                            break;
                        case "idle":
                            status = OnlineStatus.IDLE;
                            break;
                        case "dnd":
                            status = OnlineStatus.DO_NOT_DISTURB;
                            break;
                        case "invisible":
                            status = OnlineStatus.INVISIBLE;
                            break;
                        case "offline":
                            status = OnlineStatus.OFFLINE;
                            break;
                        default:
                            System.out.println("Unknown Status");
                            return;
                    }
                
                System.out.println("Status set to " + status.toString());
                Main.setStatus(status);
            }
        }
    }
}
