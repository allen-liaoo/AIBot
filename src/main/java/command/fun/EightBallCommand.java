/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.fun;

import command.Command;
import constants.Global;
import setting.Prefix;
import constants.Emoji;
import constants.FilePath;
import system.AILogger;
import utility.UtilNum;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class EightBallCommand extends Command{
    public final static String HELP = "Ask the Magic 8Ball a question!\n"
                                    + "command Usage: `" + Prefix.getDefaultPrefix() + "8ball`\n"
                                    + "Parameter: `-h | question | null`";


    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("EightBall -Help", HELP, true);
        embed.setFooter("command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        String msg = e.getAuthor().getAsMention() + " " + Emoji.EIGHT_BALL + " " + eightball(e);
        if(args.length > 0 && !"-h".equals(args[0]))
        {
            if(!e.getMessage().getContent().endsWith("?"))
            {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " " + Emoji.EIGHT_BALL + " That doesn't sounds like a question...").queue();
                return;
            }
            e.getChannel().sendMessage(msg).queue();
        }
    }

    
    public String eightball(MessageReceivedEvent e)
    {
        String respond = "", output = "";
        int totalline = 0;
        
        //Generate Random Number base on the lines in 8Ball.txt
        try {
                BufferedReader reader = new BufferedReader(new FileReader(FilePath.EightBall));
                
                while((output = reader.readLine()) != null)
                {
                    totalline++;
                }
                reader.close();
            } catch (IOException io) {
                AILogger.errorLog(io, e, this.getClass().getName(), "BufferedReader at reading line numbers");
        }
        int magic = UtilNum.randomNum(0, totalline), line = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FilePath.EightBall));

            while((respond = reader.readLine()) != null)
            {
                line++;
                if(line >= magic)
                    break;
            }
            reader.close();
                
        } catch (IOException io) {
            AILogger.errorLog(io, e, this.getClass().getName(), "BufferedReader at getting response.");
        }
        return respond;
    }
}
