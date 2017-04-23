/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.RestrictedModule;

import Command.Command;
import Resource.Emoji;
import Resource.FilePath;
import Resource.Constants;
import Setting.Prefix;
import Utility.AILogger;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class LogCommand implements Command {
    
    public final static String HELP = "This command is for getting the logs of this bot. Server owner, bot owner or or "
                                    + "members that have `Manage Message` permissions only.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"log`\n"
                                    + "Parameter: `-h | main | error | command | null`";
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Restricted Module", null);
        embed.addField("Log -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0])) {
            help(e);
        }
        
        else if(!e.getChannelType().isGuild() || 
            e.getMember().isOwner() || 
            e.getMember().hasPermission(Permission.MESSAGE_MANAGE) ||
            Constants.D_ID.equals(e.getAuthor().getId())) 
        {
            String filepath = "", line;

            if(args.length > 0 && "main".equals(args[0].toLowerCase())) filepath = FilePath.LogMain;
            else if(args.length > 0 && "error".equals(args[0].toLowerCase())) filepath = FilePath.LogError;
            else if(args.length > 0 && "command".equals(args[0].toLowerCase())) filepath = FilePath.LogCommand;
            else { 
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid log type.\nValid type: `main, error, command`").queue();
                return;
            }
        
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filepath));
                String output = "";

                while((line = reader.readLine()) != null)
                {
                    output += line + "\n";
                }
                reader.close();

                //Split Strings into 1500 characters
                List<String> outputs = new ArrayList<>();
                int index = 0;
                while (index < output.length()) {
                    outputs.add(output.substring(index, Math.min(index + 1500,output.length())));
                    index += 1500;
                }
                
                if(outputs.isEmpty()) {
                    e.getChannel().sendMessage("The Log is empty.").queue();
                    return;
                }
                else if(e.getChannelType().isGuild())
                    e.getChannel().sendMessage(Emoji.ENVELOPE + " Log has been sent to your private message.").queue();

                for(String s : outputs) {
                    e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage("```" + s + "```").complete());
                }

            } catch (IOException io) {
                AILogger.errorLog(io, e, this.getClass().getName(), "BufferedReader at getting logs.");
            }
        }
        else {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, "
                    + "or members that have `Manage Message` permissions only.").queue();
        }
    }

    
}
