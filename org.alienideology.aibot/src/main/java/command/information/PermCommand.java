package command.information;

import command.Command;
import constants.Emoji;
import utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;

import java.util.List;
import java.util.ArrayList;

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
        Member mem = null;
        
        if (args.length == 0) {     // Self
            mem = e.getMember();
        } else if (args[0].length == 18) {  // ID
            mem = e.getGuild().getMemberById(args[0]);
        } else if(!e.getMessage().getMentionedUsers().isEmpty()) {  // Mention
            mem = e.getGuild().getMemebrById(e.getMessage().getMentionedUsers().get(0).getId());
        }
        
        

    }
    
    private void sendMemberPerms(MessageReceivedEvent e, Member mem) {
        StringBuilder message = new StringBuilder("Permissions for user **").append(mem.getEffectiveName())
            .append("**#").append(mem.getUser().getDiscriminator()).append(":");
        
        for(Permission perm : Permission.values()) {
            if(mem.hasPermissions(perm)) {
                message.append(Emoji.GREEN_TICK);
            } else {
                message.append(Emoji.RED_TICK);
            }
            message.append(perm.getName()).append("\n");
        }
        
        e.getChannel().sendMessage(message.toString());
    }
    
    private void sendRolePerms(MessageReceivedEvent e, Role role) {
        StringBuilder message = new StringBuilder("Permissions for role **").append(role.getName())
            .append("** ").append("(Position: ").append(role.getPosition()).append(") :");
        
        for(Permission perm : Permission.values()) {
            if(mem.hasPermissions(perm)) {
                message.append(Emoji.GREEN_TICK);
            } else {
                message.append(Emoji.RED_TICK);
            }
            message.append(perm.getName()).append("\n");
        }
        
        e.getChannel().sendMessage(message.toString());
    }
    
}
