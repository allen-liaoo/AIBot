/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

//Setted to SUPPORT PRIVATE CHANNEL.

import Command.*;
import Config.*;
import Main.*;
import java.awt.Color;
import java.time.Instant;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.Permission;
/**
 *
 * @author liaoyilin
 */
public class InfoBotCommand implements Command{

    public final static String HELP = "This command is for getting this bot's information.\n"
                              + "Command Usage: `" + Prefix.getDefaultPrefix() + "botinfo`\n"
                              + "Parameter: `-h | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embedinfo = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.addField("BotInfo -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            JDA bot = e.getJDA();
            
            String id, name, nickname = "", dis, avatar, owner, status, game, shard, join = "", register, perm, permString = "", roles;
            int guild;
            long timeCurrent = System.currentTimeMillis(), uptime;
            
            //Bot Information
            name = bot.getSelfUser().getName();
            dis = bot.getSelfUser().getDiscriminator();
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                nickname = e.getGuild().getSelfMember().getEffectiveName();
            
            owner = bot.getUserById("248214880379863041").getName();
            avatar = bot.getSelfUser().getAvatarUrl();
            id = bot.getSelfUser().getId();
            register = bot.getSelfUser().getCreationTime().toString();
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                join = e.getGuild().getSelfMember().getJoinDate().toString();
            
            //Bot Status
            game = bot.getPresence().getGame().getName();
            int seconds = (int) (timeCurrent - Main.timeStart/ 1000) % 60 ;
            int minutes = (int) ((timeCurrent - Main.timeStart / (1000*60)) % 60);
            int hours   = (int) ((timeCurrent - Main.timeStart / (1000*60*60)) % 24);
            
            status = bot.getPresence().getStatus().getKey();
            try{shard = bot.getShardInfo().getShardString();} catch(NullPointerException ne) {shard = "None";}
            
            //Bot /w Guild Info
            guild = bot.getGuilds().size();
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {
                perm = e.getGuild().getSelfMember().getPermissions(e.getTextChannel()).toString();
            
                List<Permission> perms = e.getGuild().getSelfMember().getPermissions();
                
                for(Permission permP : perms) {
                    permString += permP.getName() + ", ";
                }
                permString = permString.substring(0, permString.length()-2);
            }
            
            embedinfo.setAuthor(name, null, Info.I_info);
            embedinfo.setColor(Color.blue);
            embedinfo.setThumbnail(avatar);
            embedinfo.setTimestamp(Instant.now());
            embedinfo.setFooter("AIBot Information", Info.B_URL);
            
            embedinfo.addField("ID", id, true);
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                embedinfo.addField("Nickname", nickname, true);
            embedinfo.addField("Discriminator", dis, true);
            embedinfo.addField("Owner", owner, true);
            embedinfo.addField("Game", game, true);
            embedinfo.addField("Up Time", hours + " hours, " + minutes + " minutes, " + seconds + " seconds", true);
            embedinfo.addField("Registered", register, true);
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                embedinfo.addField("Joined", join, true);
            
            embedinfo.addField("Status", status, true);
            embedinfo.addField("Shard", shard, true);
            embedinfo.addField("Servers", String.valueOf(guild), true);
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                embedinfo.addField("Permissions", permString, false);
            
            MessageEmbed mein = embedinfo.build();
            e.getChannel().sendMessage(mein).queue();
            embedinfo.clearFields();
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
