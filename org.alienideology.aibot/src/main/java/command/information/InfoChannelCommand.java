/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.Command;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import system.AILogger;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import utility.UtilBot;
import utility.UtilString;

import javax.rmi.CORBA.Util;

/**
 *
 * @author liaoyilin
 */
public class InfoChannelCommand extends Command{
    public final static String HELP = "This command is for getting informations about this text channel.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "channelinfo` or `" + Prefix.getDefaultPrefix() + "ci` \n"
                                    + "Parameter: `-h | audio or voice or vc | null`\n"
                                    + "null: Will get the current text channel info.\n"
                                    + "audio: Will get the voice channel info that requested user are in.";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("ChannelInfo -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            TextChannel txtchannel = e.getTextChannel();
            
            String name, id, icon, type, create, topic;
            int member, human = 0, bot = 0, position;

            icon = e.getGuild().getIconUrl();

            /* Identity */
            name = txtchannel.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            id = txtchannel.getId();
            type = txtchannel.getType().toString();
            topic = txtchannel.getTopic().isEmpty() ? "N/A" : txtchannel.getTopic();
            position = txtchannel.getPosition();

            /* Time */
            create = UtilString.formatOffsetDateTime(txtchannel.getCreationTime());

            /* Member */
            List<Member> members = txtchannel.getMembers();
            member = members.size();

            for(Member memberM : members) {
                if(!memberM.getUser().isBot())
                    human ++;
                else
                    bot ++;
            }
            
            EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(name, null, null)
                .setColor(UtilBot.randomColor()).setThumbnail(icon).setTimestamp(Instant.now())
                .setFooter("Channel Info", null);
            
            embed.addField("Identity", "ID `"+id+"`\n"+
                "Type `"+type+"` | Topic `"+topic+"`\n"+
                "Position `"+position+"`\n", true);
            embed.addField(Emoji.STOPWATCH+"Created In", create, true);

            embed.addField(Emoji.SPY+"Members", "Total `"+member + "`\n"+
                "Human `"+human+"` | "+
                "Bot `"+bot+"`", true);

            e.getTextChannel().sendMessage(embed.build()).queue();
        }
        
        else if("audio".equals(args[0]) || "voice".equals(args[0]) || "vc".equals(args[0]))
        {
            VoiceChannel voiceChannel;
            try {
                voiceChannel = e.getMember().getVoiceState().getChannel();
            } catch (NullPointerException npe) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " You need to join a voice channel first "
                        + "to see the channel's information.").queue();
                return;
            }

            String name, id, icon, type, create;
            int member, human = 0, bot = 0, position, bitrate, user_limit;

            icon = e.getGuild().getIconUrl();

            /* Identity */
            name = voiceChannel.getName();
            id = voiceChannel.getId();
            type = "Audio";
            position = voiceChannel.getPosition();

            /* Audio */
            bitrate = voiceChannel.getBitrate();
            user_limit = voiceChannel.getUserLimit();

            /* Time */
            create = UtilString.formatOffsetDateTime(voiceChannel.getCreationTime());

            /* Member */
            List<Member> members = voiceChannel.getMembers();
            member = members.size();
            for(Member memberM : members) {
                if(!memberM.getUser().isBot())
                    human ++;
                else
                    bot ++;
            }

            EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(name, null, null)
                .setColor(UtilBot.randomColor()).setThumbnail(icon).setTimestamp(Instant.now())
                .setFooter("Voice Channel Info", null);

            embed.addField("Identity", "ID `"+id+"`\n"+
                "Type `"+type+"` | Position `"+position+"`\n", true);

            embed.addField(Emoji.STOPWATCH+"Created In ", create, true);

            embed.addField(Emoji.SPY+"Member", "Total `"+member+"`\n"+
                "Human `"+human+"` | "+
                "Bot `"+bot+"`\n", true);

            embed.addField(Emoji.NOTES+"Audio", "Bit Rate `"+bitrate+"` | User Limit `"+user_limit+"`", true);

            e.getTextChannel().sendMessage(embed.build()).queue();
        }
    }

    
}
