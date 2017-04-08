/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.UtilityModule;

import Command.Command;
import Resource.Emoji;
import Resource.Info;
import Resource.Prefix;
import Main.*;
import Setting.SmartLogger;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 *
 * @author liaoyilin
 */
public class MathCommand implements Command{

    public final static String HELP = "This command is for calculating math operations.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "math` or `" + Prefix.getDefaultPrefix() + "calc` or `" + Prefix.getDefaultPrefix() + "m`\n"
                                    + "Parameter: `-h | [Math Operation] | null`\n"
                                    + "Supported values: `pi, π, e, φ`\n";
    
    private static EmbedBuilder embed = new EmbedBuilder();
    
    private static String input = "";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("Math -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
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
            boolean isPrivate = false;
            if(e.getChannelType() == e.getChannelType().PRIVATE)
                isPrivate = true;

            //Convert args(without math invoke) into a String
            for(String in : args)
            {
                if(!in.equals("math") && !in.equals("calc") && !in.equals("m"))
                    input += in;
            }
            

            double result;

            try {
                Expression ex = new ExpressionBuilder(input)
                    .build();
                result = ex.evaluate();
                int integer = (int)result;
                
                if(result >= 2147483647 || result <= -2147483647)
                        e.getChannel().sendMessage(Emoji.error +  " Calculation exceeds the range I am able to display!\n Range: `2147483647 ~ -2147483648`").queue();
                
                else if(result % 1 == 0)
                {
                    e.getChannel().sendMessage(Emoji.output + Emoji.number + "  `" + input + "` is  `" 
                    + integer + "`").queue();
                    
                        
                }
                else
                {
                    e.getChannel().sendMessage(Emoji.output + Emoji.number + "  `" + input + "` is  `"
                    + result + "`").queue();
                }
                
            } catch (ArithmeticException ae) {
                e.getChannel().sendMessage(Emoji.error + " Do not devide a value by 0.").queue();
            } catch (RuntimeException rte) {
                e.getChannel().sendMessage(Emoji.error + " Please enter a valid math operation.").queue();
                SmartLogger.errorLog(rte, e, this.getClass().getName(), "Unvalid operation \"" + input + "\"");
            }
            
            input = "";
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
