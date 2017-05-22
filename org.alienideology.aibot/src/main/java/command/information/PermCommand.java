package command.information;

import command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;

/**
 * @author AlienIdeology
 */
public class PermCommand extends Command {

    public final static String HELP = "This command is for getting a list of permissions of a user or role.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"perm`\n"
                                    + "Parameter: `-h | @Mention | [ID] | null`\n"
                                    + "@Mention: Can be use mention or role mention.\n"
                                    + "[ID]; Can be user ID or role ID.\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Perm -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

    }
}
