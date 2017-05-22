package command.utility;

import command.Command;
import constants.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import setting.Prefix;
import utility.UtilBot;
import utility.UtilNum;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AlienIdeology
 */
public class ModsCommand extends Command {

    public final static String HELP = "This command is for getting a list of mods in this server. The list also shows online status. "
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"mods`\n"
                                    + "Parameter: `-h | pin [message] | null`\n"
                                    + "pin [message]: Automatically pin a mod, and leave a message. For example, `"+Prefix.getDefaultPrefix()+"pin Someone is spamming`.\n"
                                    + "null: Get a list of mods.";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Moderation Module", null);
        embed.addField("Mods -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        if (args.length >= 1 && ("pin".equals(args[0]))) {
            String message = "";
            for(int i = 0; i < args.length; i++) { message += i==0 ? "" : args[i] + " "; }

            if (e.getGuild().getMembers().size() > 100) {     // ilter large servers
                e.getChannel().sendMessage(Emoji.ERROR + " The server is too big. I'm afraid I'll get banned by pinning mod...").queue();
            } else {
                List<Member> onlineMod = getMods(e.getGuild()).stream()
                    .filter(mod -> mod.getOnlineStatus().equals(OnlineStatus.ONLINE)).collect(Collectors.toList());

                if (onlineMod.isEmpty()) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Sorry, no mod is available.").queue();
                } else {
                    Member randomMod = onlineMod.get(UtilNum.randomNum(0, onlineMod.size()-1));
                    e.getChannel().sendMessage(randomMod.getAsMention() + ", **" + e.getMember().getEffectiveName() + "#" + e.getAuthor().getDiscriminator() +
                        "** say: " + message).queue();
                }
            }

        } else {
            List<Member> mods = sortByStatus(getMods(e.getGuild()));
            StringBuilder output = new StringBuilder("Mods in **").append(e.getGuild().getName()).append("**\n");

            /* Iterate through mod list */
            for (Member mem : mods) {
                output.append(UtilBot.getStatusEmoji(mem.getOnlineStatus()))
                        .append(mem.getUser().getName()).append("#").append(mem.getUser().getDiscriminator())
                        .append(mem.isOwner() ? " (Owner)" : "")
                        .append(mem.hasPermission(Permission.ADMINISTRATOR) && !mem.isOwner() ? " (Admin)" : "").append("\n");
            }

            e.getChannel().sendMessage(output.toString()).queue();
        }
    }

    private List<Member> sortByStatus(List<Member> members) {
        Collections.sort(members, Comparator.comparing(Member::getOnlineStatus).thenComparing((o1, o2) -> {
            if(o1.isOwner()) return -1;
            else if (o2.isOwner()) return 1;
            if(!o1.getRoles().isEmpty() && !o2.getRoles().isEmpty())
                return o1.getRoles().get(0).getPosition() > o2.getRoles().get(0).getPosition() ? -1 : (o1.getRoles().get(0).getPosition() < o2.getRoles().get(0).getPosition()) ? 1 : (o1.getEffectiveName().compareTo(o2.getEffectiveName()));
            else if (!o1.getRoles().isEmpty() && o2.getRoles().isEmpty())
                return -1;
            else if (o1.getRoles().isEmpty() && !o2.getRoles().isEmpty())
                return 1;
            return o1.getEffectiveName().compareTo(o2.getEffectiveName());
        }));
        return members;
    }

    private List<Member> getMods(Guild guild) {
        List<Member> mods = guild.getMembers().stream()
            .filter(member -> !member.getUser().isBot())
            .filter(member ->
                    (member.getPermissions().contains(Permission.BAN_MEMBERS) ||
                        member.getPermissions().contains(Permission.KICK_MEMBERS) ||
                        member.getPermissions().contains(Permission.ADMINISTRATOR)))
            .collect(Collectors.toList());
        return mods;
    }
}
