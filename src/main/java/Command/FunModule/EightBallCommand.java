/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Config.Info;
import Config.Prefix;
import Main.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class EightBallCommand implements Command{
    public final static String HELP = "Ask the Magic 8Ball a question!\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "8ball`\n"
                                    + "Parameter: `-h | question | null`";

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("EightBall -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        String msg = e.getAuthor().getAsMention() + " " + eightball();
        if(args.length > 0 && !"-h".equals(args[0]))
        {
            e.getChannel().sendMessage(msg).queue();
        }
            
        else if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    public String eightball()
    {
        String respond = "", output = "";
        int totalline = 0;
        
        //Generate Random Number base on the lines in 8Ball.txt
        try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/8Ball.txt"));
                
                while((output = reader.readLine()) != null)
                {
                    totalline++;
                }
                reader.close();
            } catch (IOException io) {
                io.printStackTrace();
        }
        int magic = (int) Math.ceil(Math.random() * totalline), line = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/8Ball.txt"));

            while((respond = reader.readLine()) != null)
            {
                line++;
                if(line >= magic)
                    break;
            }
            reader.close();
                
        } catch (IOException io) {
            io.printStackTrace();
        }
        
        /*
        switch(magic)
        {
            case 1: respond = "Yes.";
            break;
            case 2: respond = "No.";
            break;
            case 3: respond = "The odds are in favor.";
            break;
            case 4: respond = "The odds are against you.";
            break;
            case 5: respond = "Never.";
            break;
            case 6: respond = "Definitely!";
            break;
            case 7: respond = "Maybe.";
            break;
            case 8: respond = "I don't think so.";
            break;
            case 9: respond = "I'd say no.";
            break;
            case 10: respond = "Probably.";
            break;
            default: respond = "Try Again.";
            break;
        }*/
        return respond;
    }
}
