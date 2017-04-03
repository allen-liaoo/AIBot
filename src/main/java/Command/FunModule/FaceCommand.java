/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Config.Emoji;
import Config.Info;
import Config.Prefix;
import Main.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class FaceCommand implements Command{

    public final static String HELP = "This command is for... ( ͡° ͜ʖ ͡°)\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"face` or `" + Prefix.getDefaultPrefix() + "lenny` or `" + Prefix.getDefaultPrefix() + "f`\n"
                                    + "Parameter: `-h | [Face Name] | -list | null`\n"
                                    + "null: Get a random face.\n"
                                    + "[Face Name]: Get a specific face by name.\n"
                                    + "-list: Get a list of faces and their names in your Direct Message.";
    
    private final EmbedBuilder embed = new EmbedBuilder();
    
    public static HashMap<String, String> faces = new HashMap<String, String>(); //K: Fave Name, Value: Face String
    private static String output = "";
    private static String facelist = "";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Number -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            int count = 0, lines = 0, num = 0;
            
            //Generate Random Number base on the lines in FaceList.txt
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/FaceList.txt"));
                
                while((output = reader.readLine()) != null)
                {
                    lines++;
                }
                reader.close();
                num = (int)(Math.random() * lines + 1);
                if(num % 2 != 0) num += 1; //Make the random number always even
            } catch (IOException io) {
                io.printStackTrace();
            }
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/FaceList.txt"));
                
                while((output = reader.readLine()) != null)
                {
                    count++;
                    if(num == count) break;
                }
                reader.close();
                
            } catch (IOException io) {
                io.printStackTrace();
            }
            
            e.getChannel().sendMessage(output).queue();
        }
        
        else if(args.length == 1 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length == 1 && "-list".equals(args[0])) 
        {
            String name = "", face = "";
            int count = 0;
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/FaceList.txt"));
                
                while((output = reader.readLine()) != null)
                {
                    count++;
                    if(count % 2 == 0) //Put tab and nextLine into place
                        facelist += output + "\n";
                    else
                        facelist += output + "\t";
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            
            EmbedBuilder embedfl = new EmbedBuilder();
            embedfl.setColor(Color.red);
            embedfl.setTitle("Face Command", null);
            embedfl.addField("Lenny Faces List", facelist, true);
            embedfl.setFooter("( ͡° ͜ʖ ͡°)", null);
            embedfl.setTimestamp(Instant.now());

            MessageEmbed mefl = embedfl.build();
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {
                e.getTextChannel().sendMessage(Emoji.envelope + "Shhh... Check your Direct Message.").queue();
                e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(mefl).queue());
            }
            else
            {
                e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(mefl).queue());
            }
            
            facelist = "";
            embedfl.clearFields();
        }
        
        else
        {
            String input = args[0];
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Game/FaceList.txt"));
                
                while((output = reader.readLine()) != null)
                {
                    if(output.equals(args[0])) //If the face name is available
                    {
                        output = reader.readLine(); //Read the next line, which is the face String
                        break;
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            
            e.getChannel().sendMessage(output).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
