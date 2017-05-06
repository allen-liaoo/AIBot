/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.*;
import constants.Emoji;
import constants.Global;
import Setting.Prefix;
import main.AIBot;
import utility.UtilBot;
import utility.UtilString;
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
public class StatusCommand extends Command{

    public final static String HELP = "This command is for getting this bot's status.\n"
                              + "command Usage: `" + Prefix.getDefaultPrefix() + "status` or `" + Prefix.getDefaultPrefix() + "uptime`\n"
                              + "Parameter: `-h | null`";
    
    private EmbedBuilder embedstatus = new EmbedBuilder();
    private String type = "";
    
    public StatusCommand(String invoke)
    {
        if("status".equals(invoke)) type = "status";
        else if("uptime".equals(invoke)) type = "uptime";
    }
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Status -Help", HELP, true);
        embed.setFooter("command Help/Usage", Global.I_HELP);
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
            long timeCurrent = System.currentTimeMillis(), uptime;
            uptime = timeCurrent - AIBot.timeStart;
            String upinfo = UtilString.formatTime(uptime);
            
            
            if("uptime".equals(type))
            {
                e.getChannel().sendMessage(Emoji.STOPWATCH + " AIBot has been up for: " + upinfo).queue();
            }

            else if("status".equals(type))
            {
                JDA bot = e.getJDA();
                String avatar, status, shard, more;
                int guild, textchannels, privatechannels, audiochannels, voicechannels;

                avatar = bot.getSelfUser().getAvatarUrl();
                status = UtilBot.getStatusString(e.getJDA().getPresence().getStatus()) + ", " + UtilString.VariableToString("_", bot.getStatus().name());
                guild = bot.getGuilds().size();
                textchannels = bot.getTextChannels().size();
                privatechannels = bot.getPrivateChannels().size();
                audiochannels = bot.getVoiceChannels().size();
                voicechannels = UtilBot.getConnectedVoiceChannels() == null ? 0 : UtilBot.getConnectedVoiceChannels().size();
                try{shard = bot.getShardInfo().getShardString();} catch(NullPointerException ne) {shard = "None";}

                //More info
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

                embedstatus.addField(Emoji.STOPWATCH + " Uptime", upinfo, true);
                embedstatus.addField(Emoji.STATUS + " Status", status, true);
                embedstatus.addField(Emoji.GUILDS + " Servers", String.valueOf(guild), true);
                embedstatus.addField(Emoji.SHARDS + " Shards", shard, true);
                embedstatus.addField(Emoji.TEXT + " Text Channels", String.valueOf(textchannels), true);
                embedstatus.addField(Emoji.SPY + " PrivateConstant Channels", String.valueOf(privatechannels), true);
                embedstatus.addField(Emoji.MUSIC + " Voice Channels", String.valueOf(audiochannels), true);
                embedstatus.addField(Emoji.NOTES + " Playing Music in", String.valueOf(voicechannels) + " voice channel(s)", true);
                embedstatus.addField("More...", more, true);
                embedstatus.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());
                embedstatus.setTimestamp(Instant.now());

                MessageEmbed me = embedstatus.build();
                e.getChannel().sendMessage(me).queue();
                embedstatus.clearFields();   
            }
        }
    }

    
    
}
