/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.Command;
import constants.Global;
import setting.Prefix;
import constants.Emoji;
import java.awt.Color;
import java.util.List;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Guild;
import utility.UtilString;

/**
 *
 * @author liaoyilin
 */
public class InfoServerCommand extends Command{
    public final static String HELP = "This command is for getting informations about this server.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "serverinfo` or `" + Prefix.getDefaultPrefix() + "si` \n"
                                    + "Parameter: `-h | [ID] | null`\n"
                                    + "[ID]: Get a server by ID (The Bot must be in that server).\n";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("ServerInfo -Help", HELP, true);
        embed.setFooter("Command Help/Usage", null);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length >= 0 || "-m".equals(args[0]))
        {
            Guild guild = e.getGuild();
            
            //Detects ID
            if(args.length > 0) {
                if("-m".equals(args[0]) && args.length > 1 && args[1].length() == 18)
                    guild = e.getJDA().getGuildById(args[1]);
                else if(args[0].length() == 18)
                    guild = e.getJDA().getGuildById(args[0]);
            }
            
            if(guild == null) {
                e.getChannel().sendMessage(Emoji.ERROR + " Cannot find this guild.\n"
                        + "Either I am not in this guild or the ID you provided is invalid.").queue();
                return;
            }
            
            String icon, name, id, owner, created, region, verify;
            int txtChannel, audioChannel, member, online = 0, human = 0, bot = 0;

            icon = guild.getIconUrl();

            /* Identity */
            name = guild.getName();
            id = guild.getId();
            owner = guild.getOwner().getEffectiveName();

            /* Channel */
            txtChannel = guild.getTextChannels().size();
            audioChannel = guild.getVoiceChannels().size();

            /* Time */
            created = UtilString.formatOffsetDateTime(guild.getCreationTime());

            /* Member */
            List<Member> members = guild.getMembers();
            member = members.size();
            for(Member memberM : members)  {
                String status = memberM.getOnlineStatus().toString();
                if(status.equals(OnlineStatus.ONLINE.toString()))
                    online ++;
                if(!memberM.getUser().isBot())
                    human ++;
                else
                    bot ++;
            }

            /* Other */
            verify = guild.getVerificationLevel().toString();
            region = guild.getRegion().toString();
            
            /*
            //Get Invite of the server
            e.getJDA().getGuildById(guild.getId()).getPublicChannel().createInvite()
                .setMaxAge(120).setMaxUses(1).setTemporary(true)
                .queue(
                    (Invite i) -> {
                        embed.setAuthor(name, "https://discord.gg/" + i.getCode(), null);
                    }
                );
            */

            EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(name, null, null).setColor(Color.blue).setThumbnail(icon).setTimestamp(Instant.now())
                .setFooter("Server Info", null);
            
            embed.addField("ID", id, true);
            embed.addField("Owner", owner, true);

            embed.addField(Emoji.STOPWATCH+"Created In", created, true);

            embed.addField(Emoji.TEXT+"Channel", "Text `"+txtChannel+"` | Voice `"+audioChannel+"`", true);
            embed.addField(Emoji.SPY + " Member", "User `"+member+"` | "+Emoji.GUILD_ONLINE+" `"+online+"`\nHuman `"+human+"` | Bot `"+bot+"`", true);
            embed.addField("Other", "Region `" + region + "` | Verification `" + verify + "`", true);

            e.getTextChannel().sendMessage(embed.build()).queue();
        }
    }
}
