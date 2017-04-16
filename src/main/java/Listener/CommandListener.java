/* 
 * AIBot by AlienIdeology
 * 
 * CommandListener
 * Deliver commands to CommandParser, then handle the command by calling the corisponding
 * Command Class.
 */
package Listener;

import Setting.Prefix;
import Main.*;
import Setting.GuildSetting;
import Audio.Music;
import static Main.Main.commands;
import Setting.RateLimiter;
import Utility.SmartLogger;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;

/**
 *
 * @author liaoyilin
 */
public class CommandListener extends ListenerAdapter {
    
    private String prefix;
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        /*
        * Reject Commands from Bots and Fake Users
        */
        if(e.getAuthor().isBot() || e.getAuthor().isFake())
            return;
        
        /*
        * Create GuildSetting for each Guild
        */
        if(!e.isFromType(e.getChannelType().PRIVATE) && !Main.guilds.containsKey(e.getGuild().getId()))
        {
            GuildSetting newGuild = new GuildSetting(Music.playerManager, e.getGuild().getId(), "=", 50);
            Main.guilds.put(e.getGuild().getId(), newGuild);
            e.getGuild().getAudioManager().setSendingHandler(newGuild.getSendHandler());
            SmartLogger.updateLog("\tNew Server: " + e.getGuild().getId() + " " + e.getGuild().getName());
        }
        
        /*
        * Detect commands
        */
        
        //Private Message Without Prefix or mention
        if(e.getChannelType() == e.getChannelType().PRIVATE && !e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            if(RateLimiter.isSpam(e)) return;
            handleCommand(Main.parser.parsePrivate(e.getMessage().getContent(), e));
            return;
        }
        
        //Message starts with Prefix
        if(e.getMessage().getContent().startsWith(Prefix.getDefaultPrefix()) && !e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            if(RateLimiter.isSpam(e)) return;
            handleCommand(Main.parser.parse(e.getMessage().getContent(), e));   
        }
        
        //Message starts with Mention
        if(e.getMessage().getStrippedContent().startsWith("@" + e.getGuild().getSelfMember().getEffectiveName()) && !e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            if(RateLimiter.isSpam(e)) return;
            handleCommand(Main.parser.parseMention(e.getMessage().getContent(), e));
        }
    }
    
    public static void handleCommand(CommandParser.CommandContainer cmd)
    {
        if(commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
        
            if(safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
            else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
    }
    
    @Override
    public void onReady(ReadyEvent e) {
        System.out.println("Status - Logged in as: " + e.getJDA().getSelfUser().getName());
    }
    
    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        super.onGuildAvailable(event);
        System.out.println("Guild Avaliable:" + event.getGuild().getName());
    }
}
