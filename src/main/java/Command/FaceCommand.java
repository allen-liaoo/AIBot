/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import Config.Emoji;
import Config.Info;
import Config.Prefix;
import Main.*;
import java.awt.Color;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class FaceCommand implements Command{

    public final static String HELP = "This command is for... ( ͡° ͜ʖ ͡°)\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"face` or `" + Prefix.getDefaultPrefix() + "num`\n"
                                    + "Parameter: `-h | Face Name | -list | null`\n"
                                    + "null: Get a random face.\n"
                                    + "Face Name: Get a specific face.\n"
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
        embed.setTitle("Utility Module", null);
        embed.addField("Number -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        setFaces();
        
        if(args.length == 0)
        {
            int num = (int)(Math.random() * 8 + 1);
            
            switch (num) {  //Different outputs for different number.
                        case 1: output = faces.get("lenny");
                            break;
                        case 2: output = faces.get("crying");
                            break;
                        case 3: output = faces.get("cute");
                            break;
                        case 4: output = faces.get("disapprove");
                            break;
                        case 5: output = faces.get("midfinger");
                            break;
                        case 6: output = faces.get("wink");
                            break;
                        case 7: output = faces.get("tableflip");
                            break;
                        case 8: output = faces.get("running");
                            break;
                        case 9: output = faces.get("shrug");
                            break;
                        default : output = faces.get("lenny");
                            break;
            }
            
            e.getChannel().sendMessage(output).queue();
        }
        
        else if(args.length == 1 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length == 1 && "-list".equals(args[0])) 
        {
            for(Map.Entry<String, String> entry : faces.entrySet()) {
                facelist += entry.getKey() + "\t" + entry.getValue() + "\n";
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
                e.getTextChannel().sendMessage(Emoji.E_envelope + "Shhh... Check your Direct Message.").queue();
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
            
            if(faces.containsKey(input))
                output = faces.get(input);
            
            e.getChannel().sendMessage(output).queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    public void setFaces()
    {
        faces.put("lenny", "( ͡° ͜ʖ ͡°)");
        faces.put("crying", "(ಥ ͜ʖಥ)");
        faces.put("cute", "(͡o‿O͡)");
        faces.put("disapprove", "( ◔ ʖ̯ ◔ )");
        faces.put("midfinger", "╭∩╮( ͡° ل͟ ͡° )╭∩╮");
        faces.put("wink", "( ͡~ ͜ʖ ͡°)");
        faces.put("running", "┌(° ͜ʖ͡°)┘");
        faces.put("tableflip", "（╯ ͡°  ▽ ͡°）╯︵ ┻━┻");
        faces.put("shrug", "¯\\_(ツ)_/¯");
   
    }
    
}
