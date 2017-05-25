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
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "8ball`\n"
                                    + "Parameter: `-h | question | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Fun Module", null);
        embed.addField("EightBall -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        
        String msg = e.getAuthor().getAsMention() + " " + Emoji.EIGHT_BALL + " " + eightball(e);

        if(args.length > 0)
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

        try {
            totalline = UtilNum.getLineCount(FilePath.EightBall);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        int magic = UtilNum.randomNum(0, totalline), line = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FilePath.EightBall));

            while((respond = reader.readLine()) != null) {
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
