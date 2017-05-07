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
        embed.setFooter("command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE)
        {
            TextChannel txtchannel = e.getTextChannel();
            
            String name, id, icon, type, create, topic;
            int member, human = 0, bot = 0, position, msgcount;
            
            name = txtchannel.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            id = txtchannel.getId();
            icon = e.getGuild().getIconUrl();
            type = txtchannel.getType().toString();
            create = txtchannel.getCreationTime().toString();
            topic = txtchannel.getTopic();
            
            member = txtchannel.getMembers().size();
            position = txtchannel.getPosition();
            //msgcount = txtchannel.getHistory().getRetrievedHistory().size(); Not Working!!!
            
            List<Member> members = txtchannel.getMembers();
            for(Member memberM : members) 
            {
                if(!memberM.getUser().isBot())
                    human ++;
                else
                    bot ++;
            }
            
            EmbedBuilder embedci = new EmbedBuilder();
            embedci.setAuthor(name, null, Global.I_INFO);
            embedci.setColor(Color.blue);
            embedci.setThumbnail(icon);
            embedci.setTimestamp(Instant.now());
            embedci.setFooter("Text Channel Information", null);
            
            embedci.addField("ID", id, true);
            embedci.addField("Type", type, true);
            embedci.addField("Created In", create, true);
            embedci.addField("Channel Topic/Description", topic, true);
            embedci.addField("Member", member + "", true);
            embedci.addField("Humans", human + "", true);
            embedci.addField("Bots", bot + "", true);
            embedci.addField("Position", position + "", true);
            e.getTextChannel().sendMessage(embedci.build()).queue();
            embedci.clearFields();
        }
        
        else if("audio".equals(args[0]) || "voice".equals(args[0]) || "vc".equals(args[0]) 
                && e.getChannelType() != e.getChannelType().PRIVATE)
        {
            try {
                VoiceChannel voichannel = e.getMember().getVoiceState().getChannel();

                String name, id, icon, type, create;
                int member, human = 0, bot = 0, position, bitrate, user_limit;

                name = voichannel.getName();
                id = voichannel.getId();
                icon = e.getGuild().getIconUrl();
                type = "AUDIO";
                create = voichannel.getCreationTime().toString();

                member = voichannel.getMembers().size();
                position = voichannel.getPosition();
                bitrate = voichannel.getBitrate();
                user_limit = voichannel.getUserLimit();

                List<Member> members = voichannel.getMembers();
                for(Member memberM : members) 
                {
                    if(!memberM.getUser().isBot())
                        human ++;
                    else
                        bot ++;
                }
                
                EmbedBuilder embedci = new EmbedBuilder();
                embedci.setAuthor(name, null, Global.I_INFO);
                embedci.setColor(Color.blue);
                embedci.setThumbnail(icon);
                embedci.setTimestamp(Instant.now());
                embedci.setFooter("audio Channel Information", null);

                embedci.addField("ID", id, true);
                embedci.addField("Type", type, true);
                embedci.addField("Created In ", create, true);
                embedci.addField("Member", member + "", true);
                embedci.addField("Humans", human + "", true);
                embedci.addField("Bots", bot + "", true);
                embedci.addField("Position", position + "", true);
                embedci.addField("Bitrate", bitrate + "", true);
                embedci.addField("User Limit", user_limit + "", true);
                e.getTextChannel().sendMessage(embedci.build()).queue();
                embedci.clearFields();
            } catch (RuntimeException rte) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " You need to join a voice channel first "
                                        + "to see the channel's information.").queue();
                AILogger.errorLog(rte, e, this.getClass().getName(), "Requested AudioChannel Info when the user is not in a VoiceChannel.");
            }
        }
    }

    
}
