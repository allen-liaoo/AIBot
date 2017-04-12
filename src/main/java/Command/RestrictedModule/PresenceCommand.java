/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.RestrictedModule;

import Main.Main;
import Command.*;
import Resource.Emoji;
import Resource.Info;
import Resource.Prefix;
import Setting.SmartLogger;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PresenceCommand implements Command {
    public final static  String HELP = "This command is for setting the bot's status and game. **(Bot Owner Only)**\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"setStatus` or `"+ Prefix.getDefaultPrefix() +"setGame`\n"
                                     + "Parameter: `-h | [Status] | [Game | default] | null`\n"
                                     + "[Status]: Online, idle, dnd, invisible, offline.\n"
                                     + "[Game]: String of the game or default\n";
    
    private final EmbedBuilder embed = new EmbedBuilder();
    private String type = "";
    
    public PresenceCommand(String invoke)
    {
        if("setStatus".equals(invoke)) type = "status";
        else if("setGame".equals(invoke)) type = "game";

    }
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Restricted Module", null);
        embed.addField("SetPresence -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 || "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(args.length > 0) 
        {
            if("248214880379863041".equals(e.getAuthor().getId()))
            {
                if("status".equals(type))
                {
                    OnlineStatus status = OnlineStatus.ONLINE;

                    switch(args[0]) {
                        case "online":
                            status = OnlineStatus.ONLINE;
                            break;
                        case "idle":
                            status = OnlineStatus.IDLE;
                            break;
                        case "dnd":
                            status = OnlineStatus.DO_NOT_DISTURB;
                            break;
                        case "invisible":
                            status = OnlineStatus.INVISIBLE;
                            break;
                        case "offline":
                            status = OnlineStatus.OFFLINE;
                            break;
                        default:
                            status = OnlineStatus.UNKNOWN;
                            break;
                    }
                    
                    try {
                        Main.setStatus(status);
                        SmartLogger.updateLog("Bot setStatus Attempt");
                    } catch (IllegalArgumentException iae) {
                        e.getChannel().sendMessage(Emoji.error + " Please enter a valid status.").queue();
                        SmartLogger.errorLog(iae, e, "PresenceCommand", "Unknown Status");
                        return;
                    }
                    
                    e.getChannel().sendMessage(Emoji.success + " Status set to `"+ status.toString() + "`").queue();
                }
                else if("game".equals(type))
                {
                    String game = "";
                    for(String g : args) { game += g + " ";}

                    Main.setGame(game);
                    SmartLogger.updateLog("Bot setGame Attempt");
                    e.getChannel().sendMessage(Emoji.success + " Game set to `"+ e.getJDA().getPresence().getGame().getName() + "`").queue();
                }
            }
            else
                e.getChannel().sendMessage(Emoji.error + " This command is for **Bot Owner** only!").queue();
                
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
