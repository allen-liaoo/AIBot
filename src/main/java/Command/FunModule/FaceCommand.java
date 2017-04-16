/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.FunModule;

import Command.Command;
import Resource.Emoji;
import Resource.Info;
import Setting.Prefix;
import Main.*;
import Resource.FilePath;
import Utility.SmartLogger;
import Utility.UtilTool;
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
    private static String facelistc = "";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Number -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
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
                BufferedReader reader = new BufferedReader(new FileReader(FilePath.FaceList));
                
                while((output = reader.readLine()) != null)
                {
                    lines++;
                }
                reader.close();
                num = UtilTool.randomNum(1, lines);
                if(num % 2 != 0) num += 1; //Make the random number always even
            } catch (IOException io) {
                SmartLogger.errorLog(io, e, this.getClass().getName(), "BufferedReader at getting sum of line numbers.");
            }
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader(FilePath.FaceList));
                
                while((output = reader.readLine()) != null)
                {
                    count++;
                    if(num == count) break;
                }
                reader.close();
                
            } catch (IOException ioe) {
                SmartLogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at getting face.");
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
                BufferedReader reader = new BufferedReader(new FileReader(FilePath.FaceList));
                
                while((output = reader.readLine()) != null)
                {
                    count++;
                    if(count % 2 == 0) //Put tab and nextLine into place
                        facelist += output + "\n";
                    else
                        facelistc += output + "\n";
                }
            } catch (IOException ioe) {
                SmartLogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at -list.");
            }
            
            EmbedBuilder embedfl = new EmbedBuilder();
            embedfl.setColor(Color.red);
            embedfl.setTitle("Lenny Faces List", null);
            embedfl.addField("Name", facelist, true);
            embedfl.addBlankField(true);
            embedfl.addField("Face", facelistc, true);
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
                BufferedReader reader = new BufferedReader(new FileReader(FilePath.FaceList));
                
                while((output = reader.readLine()) != null)
                {
                    if(output.equals(args[0])) //If the face name is available
                    {
                        output = reader.readLine(); //Read the next line, which is the face String
                        break;
                    }
                }
            } catch (IOException ioe) {
                SmartLogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at specified face.");
            }
            
            if(output != null)
                e.getChannel().sendMessage(output).queue();
            else
                e.getChannel().sendMessage("No such face " + Emoji.face_tongue).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
