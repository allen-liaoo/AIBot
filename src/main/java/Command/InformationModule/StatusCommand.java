/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.*;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Main.Main;
import Utility.UtilBot;
import Utility.UtilString;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.lang.management.*;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class StatusCommand implements Command{

    public final static String HELP = "This command is for getting this bot's status.\n"
                              + "Command Usage: `" + Prefix.getDefaultPrefix() + "status` or `" + Prefix.getDefaultPrefix() + "uptime`\n"
                              + "Parameter: `-h | null`";
    
    private EmbedBuilder embedstatus = new EmbedBuilder();
    private String type = "";
    
    public StatusCommand(String invoke)
    {
        if("status".equals(invoke)) type = "status";
        else if("uptime".equals(invoke)) type = "uptime";
    }
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.addField("Status -Help", HELP, true);
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
        
        if(args.length == 0)
        {
            long timeCurrent = System.currentTimeMillis(), uptime;
            uptime = timeCurrent - Main.timeStart;
            String upinfo = UtilString.formatTime(uptime);
            
            
            if("uptime".equals(type))
            {
                e.getChannel().sendMessage(Emoji.stopwatch + " AIBot has been up for: " + upinfo).queue();
            }

            else if("status".equals(type))
            {
                JDA bot = e.getJDA();
                String avatar, status, shard, more;
                int guild, textchannels, privatechannels, audiochannels, voicechannels;

                avatar = bot.getSelfUser().getAvatarUrl();
                status = bot.getPresence().getStatus().getKey();
                status = UtilString.capSplits("_", status);
                guild = bot.getGuilds().size();
                textchannels = bot.getTextChannels().size();
                privatechannels = bot.getPrivateChannels().size();
                audiochannels = bot.getVoiceChannels().size();
                voicechannels = UtilBot.getConnectedVoiceChannels() == null ? 0 : UtilBot.getConnectedVoiceChannels().size();
                try{shard = bot.getShardInfo().getShardString();} catch(NullPointerException ne) {shard = "None";}
                

                OperatingSystemMXBean os = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
                double loadAverage = Math.round(os.getSystemLoadAverage()*1000)/1000;
                int processors = os.getAvailableProcessors();
                String osversion = os.getVersion();

                MemoryUsage osx = (MemoryUsage)ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
                String used = UtilString.convertBytes(osx.getUsed(), true);
                String available = UtilString.convertBytes(osx.getCommitted(), true);
                String memory = used + "/" + available;

                more = "Operating System Version: Mac Sierra " + osversion
                       + "\nUsed Memory: " + memory
                       + "\nAvailable Processors: " + processors 
                       + "\nLoad Average: " + loadAverage;
                
                embedstatus.setColor(Color.blue);
                embedstatus.setTitle("AIBot Status", null);
                embedstatus.setThumbnail(avatar);

                embedstatus.addField(Emoji.stopwatch + " Uptime", upinfo, true);
                embedstatus.addField(Emoji.status + " Status", status, true);
                embedstatus.addField(Emoji.guilds + " Servers", String.valueOf(guild), true);
                embedstatus.addField(Emoji.shards + " Shards", shard, true);
                embedstatus.addField(Emoji.text + " Text Channels", String.valueOf(textchannels), true);
                embedstatus.addField(Emoji.privatespy + " Private Channels", String.valueOf(privatechannels), true);
                embedstatus.addField(Emoji.music + " Voice Channels", String.valueOf(audiochannels), true);
                embedstatus.addField(Emoji.notes + " Playing Music in", String.valueOf(voicechannels), true);
                embedstatus.addField("More...", more, true);
                embedstatus.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                embedstatus.setTimestamp(Instant.now());

                MessageEmbed me = embedstatus.build();
                e.getChannel().sendMessage(me).queue();
                embedstatus.clearFields();   
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
    
}
