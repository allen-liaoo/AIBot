/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

//Set to SUPPORT PRIVATE CHANNEL.

import constants.Emoji;
import constants.Global;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.User;
import setting.Prefix;
import command.Command;
import java.awt.Color;
import java.time.Instant;
import java.util.List;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.Permission;
import utility.UtilBot;
import utility.UtilString;

import javax.rmi.CORBA.Util;

/**
 *
 * @author liaoyilin
 */
public class InfoBotCommand extends Command{

    public final static String HELP = "This command is for getting this bot's information.\n"
                              + "Command Usage: `" + Prefix.getDefaultPrefix() + "botinfo` or `" + Prefix.getDefaultPrefix() + "bi`\n"
                              + "Parameter: `-h | null`";

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
            User user = bot.getSelfUser();
            
            String id, name, nickname, dis, avatar, owner, join, register, game, statusEmoji, status;
            
            // Identities
            name = bot.getSelfUser().getName();
            nickname = e.isFromType(ChannelType.PRIVATE) || e.getGuild().getSelfMember().getNickname() == null ? "N/A" : e.getGuild().getSelfMember().getEffectiveName();
            id = user.getId();
            dis = user.getDiscriminator();

            // Owner
            owner = bot.getUserById(Global.D_ID).getName();
            avatar = user.getEffectiveAvatarUrl();

            // Time
            register = UtilString.formatOffsetDateTime(user.getCreationTime());
            join = e.isFromType(ChannelType.PRIVATE) ? "N/A" : UtilString.formatOffsetDateTime(e.getGuild().getSelfMember().getJoinDate());
            
            // Status
            game = bot.getPresence().getGame().getName();
            statusEmoji = UtilBot.getStatusEmoji(bot.getPresence().getStatus());
            status = UtilString.VariableToString("_", bot.getPresence().getStatus().getKey());

            EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(name, null, avatar)
                .setColor(UtilBot.randomColor()).setThumbnail(avatar).setTimestamp(Instant.now())
                .setFooter("Bot Info", Global.B_DISCORD_BOT);
            
            embed.addField("Identity","Name `"+name+"` | Nickname `"+nickname+"`\n"+
                "ID `"+id+"` | Discrim `#"+dis+"`\n", true);

            embed.addField(Emoji.SPY + " Owner", owner, true);

            embed.addField(Emoji.STOPWATCH+ " Time", "Register `"+register+"`\n"+
                "Join `"+join+"`\n", true);

            embed.addField("Status", Emoji.GAME+" `"+game+"`\n"
                +statusEmoji+" `"+status+"`\n", true);

            e.getChannel().sendMessage(embed.build()).queue();
        }
    }
    
}
