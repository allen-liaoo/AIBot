/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.Command;
import Resource.Constants;
import Setting.Prefix;
import Resource.Emoji;
import java.awt.Color;
import java.util.List;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.requests.restaction.InviteAction;

/**
 *
 * @author liaoyilin
 */
public class InfoServerCommand implements Command{
    public final static String HELP = "This command is for getting informations about this server.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "serverinfo` or `" + Prefix.getDefaultPrefix() + "si` \n"
                                    + "Parameter: `-h | -m | [ID] | -m [ID] | null`\n"
                                    + "-m: Get more info about the current server.\n"
                                    + "[ID]: Get a server by ID (The Bot must be in that server).\n"
                                    + "-m [ID]: Get more info about a server by ID.\n";
    private final EmbedBuilder embed = new EmbedBuilder();
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Server Information -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0]))
        {
            help(e);
        }
        
        else if(args.length >= 0 || "-m".equals(args[0]))
        {
            Guild guild = e.getGuild();
            
            //Detects ID
            if(args.length > 0)
            {
                if("-m".equals(args[0]) && args.length > 1 && args[1].length() == 18)
                    guild = e.getJDA().getGuildById(args[1]);
                else if(args[0].length() == 18)
                    guild = e.getJDA().getGuildById(args[0]);
            }
            
            if(guild == null)
            {
                e.getChannel().sendMessage(Emoji.ERROR + " Cannot find this guild.\n"
                        + "Either I am not in this guild or the ID you provided is invalid.").queue();
                return;
            }
            
            EmbedBuilder embedsi = new EmbedBuilder();
            
            String name, id, owner, region, icon, verify;
            int txtChannel, audioChannel, member, role, online = 0, human = 0, bot = 0;
            
            name = guild.getName();
            id = guild.getId();
            owner = guild.getOwner().getEffectiveName();
            region = guild.getRegion().toString();
            icon = guild.getIconUrl();
            txtChannel = guild.getTextChannels().size();
            audioChannel = guild.getVoiceChannels().size();
            member = guild.getMembers().size();
            role = guild.getRoles().size();
            
            /*
            //Get Invite of the server
            e.getJDA().getGuildById(guild.getId()).getPublicChannel().createInvite()
                    .setMaxAge(120).setMaxUses(1).setTemporary(true)
                    .queue(
                        (Invite i) -> {
                            embedsi.setAuthor(name, "https://discord.gg/" + i.getCode(), Constants.I_INFO);
                        }
                    );*/

            embedsi.setAuthor(name, null, Constants.I_INFO);
            embedsi.setColor(Color.blue);
            embedsi.setThumbnail(icon);
            embedsi.setTimestamp(Instant.now());
            embedsi.setFooter("Server Information", null);
            
            embedsi.addField("ID", id, true);
            embedsi.addField("Owner", owner, true);
            embedsi.addField("Region", region, true);
            embedsi.addField("Text Channels", txtChannel + "", true);
            embedsi.addField("Audio Channels", audioChannel + "", true);
            embedsi.addField("Members", member + "", true);
            embedsi.addField("Roles", role + "", true);
            
            List<Member> members = guild.getMembers();
            for(Member memberM : members) 
            {
                String status = memberM.getOnlineStatus().toString();
                if(status.equals(OnlineStatus.ONLINE.toString()))
                    online ++;
                if(!memberM.getUser().isBot())
                    human ++;
                else
                    bot ++;
            }
            
            if(args.length > 0 && ("-m".equals(args[0])))
            {
                //More Information
                verify = guild.getVerificationLevel().toString();
                embedsi.addField("Verification Level", verify, true);
                embedsi.addField("Online", online + "", true);
                embedsi.addField("Human", human + "", true);
                embedsi.addField("Bot", bot + "", true);

                // Server Roles
                List<Role> roles = guild.getRoles();

                String roleString = "";
                for(Role roleR : roles) 
                {
                    roleString += roleR.getName() + ", ";
                }
                roleString = roleString.substring(0, roleString.length()-2);

                embedsi.addField("Roles", roleString, false);
            }

            MessageEmbed mer = embedsi.build();
            e.getTextChannel().sendMessage(mer).queue();
            embedsi.clearFields();
            
        }
    }

    
}
