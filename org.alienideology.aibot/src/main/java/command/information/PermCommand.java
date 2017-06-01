package command.information;

import command.Command;
import constants.Emoji;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;

import java.util.List;

/**
 * @author AlienIdeology
 */
public class PermCommand extends Command {

    public final static String HELP = "This command is for getting a list of permissions of a user or role.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"perm`\n"
                                    + "Parameter: `-h | @Mention | [ID] | null`\n"
                                    + "@Mention: Can be use mention or role mention.\n"
                                    + "[ID]: Can be user ID or role ID.\n";

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
        } else if (args[0].length() == 18) {  // ID

            if(e.getGuild().getMemberById(args[0]) == null) {
                Role role = e.getGuild().getRoleById(args[0]);
                sendRolePerms(e, role);
                return;
            } else {
                mem = e.getGuild().getMemberById(args[0]);
            }

        } else if (!e.getMessage().getMentionedUsers().isEmpty()) {  // Mention
            mem = e.getGuild().getMemberById(e.getMessage().getMentionedUsers().get(0).getId());
        } else if (!e.getMessage().getMentionedRoles().isEmpty()) {  // Mention
            Role role = e.getGuild().getRoleById(e.getMessage().getMentionedRoles().get(0).getId());
            sendRolePerms(e, role);
            return;
        }

        sendMemberPerms(e, mem);        
    }
    
    private void sendMemberPerms(MessageReceivedEvent e, Member mem) {
        StringBuilder message = new StringBuilder("Permissions for user **").append(mem.getEffectiveName())
            .append("**#").append(mem.getUser().getDiscriminator()).append(":\n");
        
        for(Permission perm : Permission.values()) {
            if(mem.hasPermission(perm)) {
                message.append(Emoji.CHECK);
                message.append(perm.getName()).append("\n");
            } else {
                List<TextChannel> channels = e.getGuild().getTextChannels();
                for (TextChannel channel : channels) {
                    if(mem.hasPermission(channel, perm)) {
                        message.append(Emoji.CHECK).append(perm.getName()).append(" (In channel: ")
                                .append(channel.getAsMention()).append(")\n");
                        break;
                    }

                    if (channel.getId().equals(channels.get(channels.size()-1).getId())) {  // Last text channel
                        message.append(Emoji.UNCHECK).append(perm.getName()).append("\n");
                    }
                }
            }
        }
        
        e.getChannel().sendMessage(message.toString()).queue();
    }
    
    private void sendRolePerms(MessageReceivedEvent e, Role role) {
        StringBuilder message = new StringBuilder("Permissions for role **").append(role.getName())
            .append("** ").append("(Position: ").append(role.getPosition()).append(") :\n");
        
        for(Permission perm : Permission.values()) {
            if(role.hasPermission(perm)) {
                message.append(Emoji.CHECK).append(perm.getName()).append("\n");
            } else {
                List<TextChannel> channels = e.getGuild().getTextChannels();
                for (TextChannel channel : channels) {
                    if(role.hasPermission(channel, perm)) {
                        message.append(Emoji.CHECK).append(perm.getName()).append(" (In channel: ")
                                .append(channel.getAsMention()).append(")\n");
                        break;
                    }

                    if (channel.getId().equals(channels.get(channels.size()-1).getId())) {  // Last text channel
                        message.append(Emoji.UNCHECK).append(perm.getName()).append("\n");
                    }
                }
            }
        }
        
        e.getChannel().sendMessage(message.toString()).queue();
    }
    
}
