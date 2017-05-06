/* 
 * AIBot by AlienIdeology
 * 
 * CommandListener
 * Deliver commands to CommandParser, then handle the command by calling the corisponding
 * Command Class.
 */
package listener;

import setting.Prefix;
import main.*;
import setting.GuildWrapper;
import audio.Music;
import static main.AIBot.commands;
import constants.Emoji;
import setting.RateLimiter;
import system.AILogger;
import net.dv8tion.jda.core.entities.ChannelType;

import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
         * does not have permission to send message or fake PrivateConstant Channels.
         */
        if(e.getChannelType().isGuild() && !e.getGuild().isAvailable() ||
            (e.getChannelType().isGuild() && !e.getTextChannel().canTalk()) || 
            (!e.getChannelType().isGuild() && e.getPrivateChannel().isFake()))
            return;
        
        /**
         * Create GuildSetting for each Guild.
         */
        if(!e.isFromType(ChannelType.PRIVATE) && !AIBot.guilds.containsKey(e.getGuild().getId()))
        {
            GuildWrapper newGuild = new GuildWrapper(Music.playerManager, e.getGuild().getId(), "=");
            AIBot.guilds.put(e.getGuild().getId(), newGuild);
            e.getGuild().getAudioManager().setSendingHandler(newGuild.getSendHandler());
            AILogger.updateLog("\tNew Server: " + e.getGuild().getId() + " " + e.getGuild().getName());
        }
        
        /**
         * Detect Trigger Words and Respond.
         */
        AIBot.respond.checkRespond(e.getMessage().getContent(), e);
        AIBot.respond.checkDynamicRespond(AIBot.parser.parseRespond(e.getMessage().getRawContent(), e), e);
        
        /**
         * Detect commands.
         */
        if(!e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            //Message from Guild that starts with Prefix or mention
            if (e.getChannelType() == ChannelType.TEXT &&
               (e.getMessage().getContent().startsWith(Prefix.getDefaultPrefix()) ||
                e.getMessage().getStrippedContent().startsWith("@" + e.getGuild().getSelfMember().getEffectiveName())))
            {
                try {
                    if(RateLimiter.isSpam(e)) return;
                    handleCommand(AIBot.parser.parse(e.getMessage().getContent(), e));
                } catch (Exception ex) {
                    e.getChannel().sendMessage(Emoji.ERROR + " An error occurred!"+"```\n\n"+AILogger.stackToString(ex)+"```").queue();
                }
            }
             
            else if (e.getChannelType() == ChannelType.PRIVATE)
            {
                if(RateLimiter.isSpam(e)) return;
                handleCommand(AIBot.parser.parsePrivate(e.getMessage().getContent(), e));
            }
        }
    }

    public static void handleCommand(CommandParser.CommandContainer cmd)
    {
        if(commands.containsKey(cmd.invoke)) {
            cmd.event.getChannel().sendTyping().queue(success -> 
            {
                MessageReceivedEvent e = cmd.event;
                try {
                    commands.get(cmd.invoke).action(cmd.args, e);
                } catch (NullPointerException npe) {
                    if(e.isFromType(ChannelType.PRIVATE))
                        e.getPrivateChannel().sendMessage(Emoji.ERROR + " This command is not supported in DM.").queue();
                    else
                        throw npe;
                } catch (PermissionException pe) {
                    e.getChannel().sendMessage(Emoji.ERROR + " I need the following permission to the command!\n"
                        +"`"+pe.getPermission().getName()+"`").queue();
                } catch (ErrorResponseException ere) {
                    if(!AILogger.errorResponseHandler(ere,e))
                        throw ere;
                } catch (Exception ex) {
                    String hastePaste = AILogger.toHasteBin(AILogger.stackToString(ex));
                    e.getChannel().sendMessage(Emoji.ERROR + " An error occurred! Please inform the owner.\n"+hastePaste).queue();
                    AILogger.handleExceptionLog(ex,e,hastePaste);
                }
            });
        }
    }

}
