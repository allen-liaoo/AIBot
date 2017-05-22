/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.fun;

import command.Command;
import constants.Emoji;
import constants.Global;
import net.dv8tion.jda.core.entities.ChannelType;
import setting.Prefix;
import constants.FilePath;
import system.AILogger;
import utility.UtilNum;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class FaceCommand extends Command{

    public final static String HELP = "This command is for... ( ͡° ͜ʖ ͡°)\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"face` or `" + Prefix.getDefaultPrefix() + "lenny` or `" + Prefix.getDefaultPrefix() + "f`\n"
                                    + "Parameter: `-h | [Face Name] | -list | null`\n"
                                    + "null: Get a random face.\n"
                                    + "[Face Name]: Get a specific face by name.\n"
                                    + "-list: Get a list of faces and their names in your Direct Message.";
    
    public static HashMap<String, String> faces = new HashMap<String, String>(); //K: Face Name, Value: Face String
    private static String output = "";
    private static String facelist = "";
    private static String facelistc = "";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Number -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0)
        {
            int count = 0, lines = 0, num = 0;
            
            try {
                lines = UtilNum.getLineCount(FilePath.EightBall);
                num = UtilNum.randomNum(1, lines);
                if(num % 2 != 0) num += 1; //Make the random NUMBER always even
            } catch (IOException io) {
                AILogger.errorLog(io, e, this.getClass().getName(), "BufferedReader at getting sum of line numbers.");
            }
            
            try {
                BufferedReader reader = new BufferedReader(new FileReader(FilePath.FaceList));
                while((output = reader.readLine()) != null) {
                    count++;
                    if(num == count) break;
                }
                reader.close();
            } catch (IOException ioe) {
                AILogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at getting face.");
            }
            
            e.getChannel().sendMessage(output).queue();
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
                AILogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at -list.");
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
            
            if(e.getChannelType() != ChannelType.PRIVATE) {
                e.getTextChannel().sendMessage(Emoji.ENVELOPE + "Shhh... Check your Direct Message.").queue();
                e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(mefl).queue());
            } else {
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
                AILogger.errorLog(ioe, e, this.getClass().getName(), "BufferedReader at specified face.");
            }
            
            if(output != null)
                e.getChannel().sendMessage(output).queue();
            else
                e.getChannel().sendMessage("No such face " + Emoji.FACE_TONGUE).queue();
        }
    }

    
}
