/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationCommand;

import Command.Command;
import Config.Info;
import Config.Prefix;
import Main.*;
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

/**
 *
 * @author liaoyilin
 */
public class InfoServerCommand implements Command{
    public final static String HELP = "This command is for getting informations about this server.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "serverinfo` or `" + Prefix.getDefaultPrefix() + "si` \n"
                                    + "Parameter: `-h | -m | null`\n"
                                    + "-m: Get more info.";
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embedsi = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Server Information -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 || "-m".equals(args[0]))
        {
            Guild guild = e.getGuild();
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
            
            embedsi.setAuthor(name, null, Info.I_info);
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
        
        else if("-h".equals(args[0]))
        {
            help(e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
