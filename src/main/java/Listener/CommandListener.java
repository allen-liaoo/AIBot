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
import Setting.GuildWrapper;
import Audio.Music;
import static Main.Main.commands;
import Constants.Emoji;
import Setting.RateLimiter;
import AISystem.AILogger;
import net.dv8tion.jda.core.entities.ChannelType;

import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.dv8tion.jda.core.requests.ErrorResponse;
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
         * does not have permission to send message or fake Private Channels.
         */
        if(e.getChannelType().isGuild() && !e.getGuild().isAvailable() ||
            (e.getChannelType().isGuild() && !e.getTextChannel().canTalk()) || 
            (!e.getChannelType().isGuild() && e.getPrivateChannel().isFake()))
            return;
        
        /**
         * Create GuildSetting for each Guild.
         */
        if(!e.isFromType(ChannelType.PRIVATE) && !Main.guilds.containsKey(e.getGuild().getId()))
        {
            GuildWrapper newGuild = new GuildWrapper(Music.playerManager, e.getGuild().getId(), "=");
            Main.guilds.put(e.getGuild().getId(), newGuild);
            e.getGuild().getAudioManager().setSendingHandler(newGuild.getSendHandler());
            AILogger.updateLog("\tNew Server: " + e.getGuild().getId() + " " + e.getGuild().getName());
        }
        
        /**
         * Detect Trigger Words and Respond.
         */
        Main.respond.checkRespond(e.getMessage().getContent(), e);
        Main.respond.checkDynamicRespond(Main.parser.parseRespond(e.getMessage().getRawContent(), e), e);
        
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
                    handleCommand(Main.parser.parse(e.getMessage().getContent(), e));
                } catch (Exception ex) {
                    e.getChannel().sendMessage(Emoji.ERROR + " An error occurred!"+"```\n\n"+AILogger.stackTractToString(ex)+"```").queue();
                }
            }
             
            else if (e.getChannelType() == ChannelType.PRIVATE)
            {
                if(RateLimiter.isSpam(e)) return;
                handleCommand(Main.parser.parsePrivate(e.getMessage().getContent(), e));
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
                    if(!errorResponseHandler(ere,e))
                        throw ere;
                } catch (Exception ex) {
                    e.getChannel().sendMessage(Emoji.ERROR + " An error occurred!"+"```\n\n"+AILogger.stackTractToString(ex)+"```").queue();
                } 
            });
        }
    }

    public static boolean errorResponseHandler(ErrorResponseException ere, MessageReceivedEvent e) {
        boolean handled = true;
        String error = Emoji.ERROR + " ";
        switch (ere.getErrorResponse()) {
            case CANNOT_SEND_TO_USER:
                e.getChannel().sendMessage(error+"I can not send message to "+e.getAuthor().getName()).queue();
                break;
            case EMBED_DISABLED:
                e.getChannel().sendMessage(error+"Please enable embed so I can talk freely.").queue();
                break;
            case INVALID_BULK_DELETE:
            case INVALID_BULK_DELETE_MESSAGE_AGE:
                e.getChannel().sendMessage(error+"Error while deleting messages.\n" +
                        "The messages might be too old (older than 2 weeks).").queue();
                break;
            case MISSING_ACCESS:
                e.getChannel().sendMessage(error+"Missing access.").queue();
                break;
            case UNKNOWN_GUILD:
            case UNKNOWN_CHANNEL:
            case UNKNOWN_MEMBER:
                AILogger.errorLog(ere,e,"ErrorResponseHandler","Unknown guild,channel,or member");
                break;
            default:
                handled = false;
                break;
        }

        return handled;
    }
    
}
