/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package command.music;

import command.Command;

import constants.Global;
import constants.HelpText;
import setting.Prefix;
import utility.UtilBot;

import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class MusicCommand extends Command{

    public final static  String HELP = "This command is for getting a list of commands of Music Module.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"music` or `"+ Prefix.getDefaultPrefix() +"m`\n"
                                     + "Parameter: `-h | null`\n";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Music -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0)
        {
            EmbedBuilder embedm = new EmbedBuilder();
            
            embedm.setColor(UtilBot.randomColor());
            embedm.setAuthor("Music Module", null, Global.I_HELP);
            embedm.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
            embedm.setTimestamp(Instant.now());
            embedm.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
            
            embedm.addField("Commands", HelpText.MUSIC_CMD, true);
            embedm.addField("Description", HelpText.MUSIC_DES, true);
            
            e.getChannel().sendMessage(embedm.build()).queue();
            embedm.clearFields();
        }
    }

    
}
