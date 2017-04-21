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
import Resource.Emoji;
import Setting.RateLimiter;
import Utility.SmartLogger;
import net.dv8tion.jda.core.entities.ChannelType;

import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;

/**
 *
 * @author liaoyilin
 */
public class CommandListener extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        /**
         * Reject Commands from Bots and Fake Users.
         */
        if(e.getAuthor().isBot() || e.getAuthor().isFake())
            return;
        
        /**
         * Reject Commands from unavailable guild, Text Channels that the bot 
         * does not have permission to send message or fake Private Channels.
         */
        if(e.getChannelType().isGuild() && !e.getGuild().isAvailable() ||
            (e.getChannelType().isGuild() && !e.getTextChannel().canTalk()) || 
            (!e.getChannelType().isGuild() && e.getPrivateChannel().isFake()))
            return;
        
        /**
         * Create GuildSetting for each Guild
         */
        if(!e.isFromType(ChannelType.PRIVATE) && !Main.guilds.containsKey(e.getGuild().getId()))
        {
            GuildSetting newGuild = new GuildSetting(Music.playerManager, e.getGuild().getId(), "=", 50);
            Main.guilds.put(e.getGuild().getId(), newGuild);
            e.getGuild().getAudioManager().setSendingHandler(newGuild.getSendHandler());
            SmartLogger.updateLog("\tNew Server: " + e.getGuild().getId() + " " + e.getGuild().getName());
        }
        
        /*
        * Detect commands
        */
        
        if(!e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            //Message from Guild that starts with Prefix or mention
            if (e.getChannelType() == ChannelType.TEXT &&
               (e.getMessage().getContent().startsWith(Prefix.getDefaultPrefix()) ||
                e.getMessage().getStrippedContent().startsWith("@" + e.getGuild().getSelfMember().getEffectiveName())))
            {
                if(RateLimiter.isSpam(e)) return;
                handleCommand(Main.parser.parse(e.getMessage().getContent(), e));
            }
             
            else if (e.getChannelType() == ChannelType.PRIVATE)
            {
                if(RateLimiter.isSpam(e)) return;
                try {
                    handleCommand(Main.parser.parsePrivate(e.getMessage().getContent(), e));
                } catch (NullPointerException npe) {
                    e.getChannel().sendMessage(Emoji.error + " This command is not supported in dm.").queue();
                }
            }
        }
    }
    
    public static void handleCommand(CommandParser.CommandContainer cmd)
    {
        if(commands.containsKey(cmd.invoke)) {
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
            
            cmd.event.getChannel().sendTyping().queue();
            
            if(safe) {
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
            else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
    }
}
