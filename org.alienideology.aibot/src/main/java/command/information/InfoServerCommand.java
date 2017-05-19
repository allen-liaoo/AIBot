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
        embed.setFooter("Command Help/Usage", Global.I_HELP);
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
            
            String name, id, owner, region, icon, verify;
            int txtChannel, audioChannel, member, online = 0, human = 0, bot = 0;
            
            name = guild.getName();
            id = guild.getId();
            owner = guild.getOwner().getEffectiveName();
            icon = guild.getIconUrl();
            txtChannel = guild.getTextChannels().size();
            audioChannel = guild.getVoiceChannels().size();
            member = guild.getMembers().size();
            verify = guild.getVerificationLevel().toString();
            region = guild.getRegion().toString();

            List<Member> members = guild.getMembers();
            for(Member memberM : members)  {
                String status = memberM.getOnlineStatus().toString();
                if(status.equals(OnlineStatus.ONLINE.toString()))
                    online ++;
                if(!memberM.getUser().isBot())
                    human ++;
                else
                    bot ++;
            }
            
            /*
            //Get Invite of the server
            e.getJDA().getGuildById(guild.getId()).getPublicChannel().createInvite()
                .setMaxAge(120).setMaxUses(1).setTemporary(true)
                .queue(
                    (Invite i) -> {
                        embedsi.setAuthor(name, "https://discord.gg/" + i.getCode(), Global.I_INFO);
                    }
                );
            */

            EmbedBuilder embedsi = new EmbedBuilder()
                .setAuthor(name, null, Global.I_INFO).setColor(Color.blue).setThumbnail(icon).setTimestamp(Instant.now())
                .setFooter("Server Information", null);
            
            embedsi.addField("ID", id, true)
                .addField("Owner", owner, true);

            embedsi.addField(Emoji.TEXT + " Channel", "Text `"+txtChannel+"` | Voice `"+audioChannel+"`", true)
                .addField(Emoji.SPY + " Member", "User `"+member+"` | "+Emoji.GUILD_ONLINE+" `"+online+"\nHuman `"+human+"` | Bot `"+bot+"`", true)
                .addField("Other", "Region `" + region + "` | Verification `" + verify + "`", true);

            e.getTextChannel().sendMessage(embedsi.build()).queue();
            embedsi.clearFields();
            
        }
    }

    
}
