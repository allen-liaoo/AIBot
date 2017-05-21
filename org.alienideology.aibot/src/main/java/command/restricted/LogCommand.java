/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package command.restricted;

import command.Command;
import constants.Emoji;
import constants.FilePath;
import constants.Global;
import setting.Prefix;
import system.AILogger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class LogCommand extends Command {
    
    public final static String HELP = "This command is for getting the logs of this bot. Server owner, bot owner or or "
                                    + "members that have `Manage Message` permissions only.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"log`\n"
                                    + "Parameter: `-h | main | error | command | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Restricted Module", null);
        embed.addField("Log -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 &&
            (!e.getChannelType().isGuild() || 
            e.getMember().isOwner() || 
            e.getMember().hasPermission(Permission.MESSAGE_MANAGE) ||
            Global.D_ID.equals(e.getAuthor().getId())))
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
        else if(args.length>0 && !"-h".equals(args[0])) {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, "
                    + "or members that have `Manage Message` permissions only.").queue();
        }
    }

    
}
