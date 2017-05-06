/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.utility;

import command.Command;
import constants.Emoji;
import constants.Global;
import Setting.Prefix;
import system.AILogger;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.dv8tion.jda.core.EmbedBuilder;

/**
 *
 * @author liaoyilin
 */
public class MathCommand extends Command{

    public final static String HELP = "This command is for calculating math operations.\n"
                                    + "command Usage: `" + Prefix.getDefaultPrefix() + "math` or `" + Prefix.getDefaultPrefix() + "calc` or `" + Prefix.getDefaultPrefix() + "m`\n"
                                    + "Parameter: `-h | [Math Operation] | null`\n"
                                    + "Supported values: `pi, π, e, φ`\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("utility Module", null);
        embed.addField("Math -Help", HELP, true);
        embed.setFooter("command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length >= 1)
        {
            String input = "";
            //Convert args(without math invoke) into a String
            for(String in : args)
            {
                if(!in.equals("math") && !in.equals("calc") && !in.equals("m"))
                    input += in;
            }

            try {
                Expression ex = new ExpressionBuilder(input)
                    .build();
                double result = ex.evaluate();
                int integer = (int)result;
                
                if(result >= 2147483647 || result <= -2147483647)
                        e.getChannel().sendMessage(Emoji.ERROR +  " Calculation exceeds the range I am able to display!\n Range: `2147483647 ~ -2147483648`").queue();
                
                else if(result % 1 == 0)
                {
                    e.getChannel().sendMessage(Emoji.PRINT + Emoji.NUMBER + "  `" + input + "` is  `" 
                    + integer + "`").queue();
                }
                else
                {
                    e.getChannel().sendMessage(Emoji.PRINT + Emoji.NUMBER + "  `" + input + "` is  `"
                    + result + "`").queue();
                }
                
            } catch (IllegalArgumentException iae) {
                e.getChannel().sendMessage(Emoji.ERROR + iae.getLocalizedMessage() + ".").queue();
            }catch (ArithmeticException ae) {
                e.getChannel().sendMessage(Emoji.ERROR + " Do not divide a value by 0.").queue();
            } catch (RuntimeException rte) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid math operation.").queue();
                AILogger.errorLog(rte, e, this.getClass().getName(), "Unvalid operation \"" + input + "\"");
            }
        }
    }

    
}
