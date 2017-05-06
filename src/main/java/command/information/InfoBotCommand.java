/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

//Set to SUPPORT PRIVATE CHANNEL.

import constants.Global;
import Setting.Prefix;
import command.Command;
import java.awt.Color;
import java.time.Instant;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.Permission;
/**
 *
 * @author liaoyilin
 */
public class InfoBotCommand extends Command{

    public final static String HELP = "This command is for getting this bot's information.\n"
                              + "command Usage: `" + Prefix.getDefaultPrefix() + "botinfo` or `" + Prefix.getDefaultPrefix() + "bi`\n"
                              + "Parameter: `-h | null`";
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embedinfo = new EmbedBuilder();
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("BotInfo -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0)
        {
            JDA bot = e.getJDA();
            
            String id, name, nickname = "", dis, avatar, owner, game, join = "", register, perm, permString = "", roles;
            
            //Bot Information
            name = bot.getSelfUser().getName();
            dis = bot.getSelfUser().getDiscriminator();
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                nickname = e.getGuild().getSelfMember().getEffectiveName();
            
            owner = bot.getUserById(Global.D_ID).getName();
            avatar = bot.getSelfUser().getAvatarUrl();
            id = bot.getSelfUser().getId();
            register = bot.getSelfUser().getCreationTime().toString();
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                join = e.getGuild().getSelfMember().getJoinDate().toString();
            
            //Bot Status
            game = bot.getPresence().getGame().getName();
            
            //Bot /w Guild Global
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {
                perm = e.getGuild().getSelfMember().getPermissions(e.getTextChannel()).toString();
            
                List<Permission> perms = e.getGuild().getSelfMember().getPermissions();
                
                for(Permission permP : perms) {
                    permString += permP.getName() + ", ";
                }
                permString = permString.substring(0, permString.length()-2);
            }
            
            embedinfo.setAuthor(name, null, Global.I_INFO);
            embedinfo.setColor(Color.blue);
            embedinfo.setThumbnail(avatar);
            embedinfo.setTimestamp(Instant.now());
            embedinfo.setFooter("AIBot Information", Global.B_DISCORD_BOT);
            
            embedinfo.addField("ID", id, true);
            if(e.getChannelType() != e.getChannelType().PRIVATE)
                embedinfo.addField("Nickname", nickname, true);
            embedinfo.addField("Discriminator", dis, true);
            embedinfo.addField("Owner", owner, true);
            embedinfo.addField("game", game, true);
            embedinfo.addField("Registered", register, true);
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {
                embedinfo.addField("Joined", join, true);
                embedinfo.addField("Permissions", permString, false);
            }
            
            MessageEmbed mein = embedinfo.build();
            e.getChannel().sendMessage(mein).queue();
            embedinfo.clearFields();
        }
    }

    
}
