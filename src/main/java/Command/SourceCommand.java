/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import Command.*;
import static Command.InviteCommand.HELP;
import Config.Emoji;
import Config.Info;
import Config.Prefix;
import Main.*;
import java.awt.Color;
import java.io.*;  
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class SourceCommand implements Command{

    public final static  String HELP = "This command is for getting the source code of a command class.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"source`\n"
                                     + "Parameter: `-h | Class Name | Class Name [from] [to] | null`\n"
                                    + "Class Name: Return the whole file.\n"
                                    + "Class Name [from] [to]: Return the codes from line `[from]` to `[to]`.";
    private final EmbedBuilder embed = new EmbedBuilder();
    private static int count = 0, fromOrig = 0, from = 0, to = 0;
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Restricted Module", null);
        embed.addField("Source -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length >= 1) 
        {
            if(!"248214880379863041".equals(e.getAuthor().getId()))// || !e.getMember().equals(e.getGuild().getOwner()))
            {
                e.getChannel().sendMessage(Emoji.E_error + " This command is for **Server Owner** or **Bot Owner** only.").queue();
            }
            
            else
            {
                try{
                    String output = "";
                    args[0] = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);
                    
                    //Check and assign line numbers
                    if(args.length == 3)
                    {
                        fromOrig = Integer.parseInt(args[1]);
                        to = Integer.parseInt(args[2]);
                        
                        //Check if fromOrig and to are negative.
                        if(fromOrig < 0)
                            fromOrig *= -1;
                        if(to < 0)
                            to *= -1;
                        
                        //Reverse the value if fromOrig is more than to.
                        if(fromOrig > to)
                        {
                            int temp = to;
                            to = fromOrig;
                            fromOrig = temp;
                        }
                        
                        from = fromOrig;
                    }
                            
                    //Read File
                    FileInputStream fstream = new FileInputStream("/Users/liaoyilin/NetBeansProjects/DiscordBot/src/main/java/Command/" + args[0] + "Command.java");
                    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));  

                    String s;  
                    if(args.length == 3)
                    {
                        while((s = br.readLine() ) != null)
                        {
                            if(count >= from)
                            {
                                output += s + "\n";
                                from++;
                                if(to == from)
                                    break;
                            }
                            
                            count++;
                        }
                        
                        br.close();
                    }
                    else
                    {
                        while((s = br.readLine() ) != null)  
                        {  
                            output += s + "\n";
                        }  
                        br.close();
                    }
                    
                    //Split Strings into 1500 characters
                    List<String> outputs = new ArrayList<>();
                    int index = 0;
                    while (index < output.length()) {
                        outputs.add(output.substring(index, Math.min(index + 1500,output.length())));
                        index += 1500;
                    }
                    
                    //Success Message
                    if(e.getChannelType() != e.getChannelType().PRIVATE)
                    {
                        e.getChannel().sendMessage(Emoji.E_success + " This is the source code of `" + args[0] + "Command.java`: \n").queue();   
                        if(args.length == 3) e.getChannel().sendMessage("Fom line `" + fromOrig + " to " + to + "`.").queue();   
                    }
                    
                    //Output
                    for(String out : outputs)
                    {
                        e.getChannel().sendMessage("```java\n" + out + "```").queue();
                    }
                    
                } catch(FileNotFoundException fnfe) {
                    e.getChannel().sendMessage(Emoji.E_error + " `" + args[0] +  "Command.java` does not exist.").queue();
                    
                } catch(Exception ex){
                    ex.printStackTrace();
                }  
                
                fromOrig = 0;
                count = 0;
                from = 0;
                to = 0;
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
