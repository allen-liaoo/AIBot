/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Command.*;
import static Command.InformationModule.InfoBotCommand.HELP;
import Resource.Emoji;
import Resource.Info;
import Setting.Prefix;
import Main.Main;
import Utility.UtilTool;
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
        embed.setFooter("Command Help/Usage", Info.I_HELP);
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
            String upinfo;
            uptime = timeCurrent - Main.timeStart;
            String hours = Long.toString(uptime/3600000), minutes = Long.toString((uptime/60000)%60), seconds = Long.toString((uptime/1000)%60);
            if(Integer.parseInt(hours) < 10)
            {
                hours = "0" + hours;
            }
            if(Integer.parseInt(minutes) < 10)
            {
                minutes = "0" + minutes;
            }
            if(Integer.parseInt(seconds) < 10)
            {
                seconds = "0" + seconds;
            }
            
            upinfo = hours + " hours, " + minutes + " minutes, and " + seconds + " seconds.";
            
            if("uptime".equals(type))
            {
                e.getChannel().sendMessage(Emoji.stopwatch + " AIBot has been up for: " + upinfo).queue();
            }

            else if("status".equals(type))
            {
                JDA bot = e.getJDA();
                String avatar, status, game, shard, more;
                int guild;
                

                avatar = bot.getSelfUser().getAvatarUrl();
                status = bot.getPresence().getStatus().getKey();
                status = status.substring(0, 1).toUpperCase() + status.substring(1);
                guild = bot.getGuilds().size();
                try{shard = bot.getShardInfo().getShardString();} catch(NullPointerException ne) {shard = "None";}
                

                OperatingSystemMXBean os = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
                double loadAverage = Math.round(os.getSystemLoadAverage()*1000)/1000;
                int processors = os.getAvailableProcessors();
                String osversion = os.getVersion();

                MemoryUsage osx = (MemoryUsage)ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
                String used = UtilTool.convertBytes(osx.getUsed(), true);
                String available = UtilTool.convertBytes(osx.getCommitted(), true);
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
