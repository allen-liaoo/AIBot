/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listener;

import static Listener.CommandListener.handleCommand;
import Main.Main;
import java.io.File;
import java.util.Scanner;

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
            if (scanner.next().equals("shutdown")) 
            {
                Main.shutdown();
            }
        }
    }
}
