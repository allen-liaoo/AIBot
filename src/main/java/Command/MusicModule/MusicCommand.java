/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import Command.Command;
import static Command.Command.embed;
import static Command.MusicModule.PlayCommand.HELP;
import Resource.HelpText;
import Resource.Info;
import Setting.Prefix;
import Utility.UtilTool;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class MusicCommand implements Command{

    public final static  String HELP = "This command is for getting a list of commands of Music Module.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"music` or `"+ Prefix.getDefaultPrefix() +"m`\n"
                                     + "Parameter: `-h | null`\n";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Play -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());
        
        e.getChannel().sendMessage(embed.build()).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            EmbedBuilder embedm = new EmbedBuilder();
            
            embedm.setColor(UtilTool.randomColor());
            embedm.setAuthor("Music Module", null, Info.I_HELP);
            embedm.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
            embedm.setTimestamp(Instant.now());
            embedm.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
            
            embedm.addField("Commands", HelpText.MUSIC_CMD, true);
            embedm.addField("Description", HelpText.MUSIC_DES, true);
            
            e.getChannel().sendMessage(embedm.build()).queue();
            embedm.clearFields();
        }
        
        else if("-h".equals(args[0]))
        {
            help(e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
