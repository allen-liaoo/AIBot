/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.UtilityModule;

import Command.Command;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import AISystem.AILogger;
import Utility.UtilNum;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class NumberCommand extends Command {

    public final static String HELP = "This command is for Number tools.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "number`, `" + Prefix.getDefaultPrefix() + "num` or  `" + Prefix.getDefaultPrefix() + "n`\n"
                                    + "Parameter: `-h | count [from] [to] | random [from] [to] | roll | coinflip | null`\n"
                                    + "random: Return a random number between 0 to 100 if the range is not specified.\n"
                                    + "roll: Dice roller, generates random number bewteen 1 and 6.";
    
    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Utility Module", null);
        embed.addField("Number -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        //Number Counter
        if("count".equals(args[0]) | "cunt".equals(args[0]) | "c".equals(args[0]))
        {
            AILogger.commandLog(e, "NumberCommand#Count", "Called");
            
            if(args.length == 1) //Default count from 1 to 4
            {
                e.getChannel().sendMessage("Counting from 1 to 4...").queue();
                int from = 1;
                int to = 4;
                while(from != to+1){ //Counting
                    e.getChannel().sendMessage(from + "\n").complete();
                    from ++;
                }
            }
            
            else //Count from sepcified range
            {
                try {
                Long from = Long.parseLong(args[1]);
                Long to = Long.parseLong(args[2]);
                
                if(to < from)
                {
                    Long temp = to;
                    to = from;
                    from = temp;
                }
                
                if("cunt".equals(args[0]))
                    e.getChannel().sendMessage("Cunting...").queue();
                else
                    e.getChannel().sendMessage("Counting...").queue();

                while(!from.equals(to+1)){ //Counting
                    e.getChannel().sendMessage(from + "\n").queue();
                    from ++;
                }
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter two valid integers.").queue();
            } catch (ArrayIndexOutOfBoundsException aioe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter two integer with a space in between.").queue();
            }
            }
        }
        
        //Random Number Generator and Dice Roller
        else if("random".equals(args[0]) | "roll".equals(args[0]) | "r".equals(args[0]))
        {
            try {   
                int num;
                
                if("roll".equals(args[0])) //Roll the dice
                {
                    num = UtilNum.randomNum(1, 6);
                    String number = Emoji.numToEmoji(num);
                    e.getChannel().sendMessage(Emoji.ROLL + " Dice Rolled: " + number).queue();
                }
                    
                else if(args.length == 1) //Defualt random range 1~100
                {
                    num = UtilNum.randomNum(1, 100);
                    String number = Emoji.stringToEmoji(num + "");
                    e.getChannel().sendMessage(Emoji.NUMBER + " Random Number generated: " + number
                    + "\nBy default range `0~100`").queue();
                }
                    
                else //Set range
                {
                    Integer low = Integer.parseInt(args[1]);
                    Integer high = Integer.parseInt(args[2]);
                    
                    int numlong = UtilNum.randomNum(high, low);
                    String number = Emoji.stringToEmoji(numlong + "");
                        
                    e.getChannel().sendMessage(Emoji.NUMBER + " Random Number generated: " + number
                    + "\nBy specified range ` " + low + "~" + high + "`").queue();
                }
                
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter valid numbers.").queue();
            } catch (ArrayIndexOutOfBoundsException aiobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter two numbers.").queue();
            }
        }
        
        //Coin Flip
        else if("coinflip".equals(args[0]) | "cf".equals(args[0]))
        {
            if (Math.random() < 0.5) e.getChannel().sendMessage(Emoji.UP + " Head!").queue();
            else e.getChannel().sendMessage(Emoji.DOWN + " Tail!").queue();
        }
    }

    
}
