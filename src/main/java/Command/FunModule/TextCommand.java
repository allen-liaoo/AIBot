/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.FunModule;

import Command.Command;
import static Command.Command.embed;
import static Command.FunModule.EightBallCommand.HELP;
import Resource.Info;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class TextCommand implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Miscellaneous Module", null);
        embed.addField("EightBall -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
