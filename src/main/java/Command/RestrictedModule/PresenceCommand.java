/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.RestrictedModule;

import Main.Main;
import Command.*;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Utility.AILogger;
import Utility.UtilBot;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PresenceCommand implements Command {
    public final static String HELP = "This command is for setting the bot's nickname, status and game.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "setNick` or `"+ Prefix.getDefaultPrefix() + "setStatus` or `"+ Prefix.getDefaultPrefix() + "setGame`\n"
                                    + "Parameter: `-h | [Status] | [Game | default] | null`\n"
                                    + "[NickName]: String of the nickname or null.\n"
                                    + "[Status]: Online, idle, dnd, invisible, offline. (Bot Owner Only)\n"
                                    + "[Game]: String of the game, default, fix, update or null. (Bot Owner Only)\n";
    
    private final EmbedBuilder embed = new EmbedBuilder();
    private String type = "";
    
    public PresenceCommand(String invoke)
    {
        if("setStatus".equals(invoke)) type = "status";
        else if("setGame".equals(invoke)) type = "game";
        else if("setNick".equals(invoke)) type = "nick";

    }
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Restricted Module", null);
        embed.addField("SetPresence -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
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
            //Set NickName
            if("nick".equals(type))
            {
                if((Constants.D_ID.equals(e.getAuthor().getId()) ||
                    e.getMember().isOwner() ||
                    e.getMember().hasPermission(Permission.NICKNAME_MANAGE)) &&
                    (e.getGuild().getSelfMember().hasPermission(Permission.NICKNAME_CHANGE)))
                {
                    String nick = "";

                    if(args.length == 1 && "null".equals(args[0]))
                        nick = null;
                    else
                        for(String g : args) { nick += g + " ";}
                    
                    //Check if the nickname is more than 32 characters
                    if(nick != null && nick.length()>32) {
                        e.getChannel().sendMessage(Emoji.error + " Cannot set a nickname that is more than 32 characters. "
                                + "(Char count: "+nick.length()+")").queue();
                        return;
                    }
                    
                    e.getGuild().getController().setNickname(e.getGuild().getSelfMember(), nick).queue();

                    if(nick == null) nick = "null";
                    e.getChannel().sendMessage(Emoji.success + " NickName set to `"+ nick + "`").queue();
                }
                else
                    e.getChannel().sendMessage(Emoji.error + " This command is for `Bot Owner`, `Server Owner` or members with `NickName Change Permission` only!").queue();
            }
            
            //Set Status
            if("status".equals(type))
            {   
                if(Constants.D_ID.equals(e.getAuthor().getId()))
                {
                    OnlineStatus status;
                    try {
                        status = UtilBot.setStatus(args[0]);
                    } catch (IllegalArgumentException iae) {
                        e.getChannel().sendMessage(Emoji.error + " Please enter a valid status.").queue();
                        AILogger.errorLog(iae, e, this.getClass().getName(), "Unknown Status");
                        return;
                    }
                    
                    e.getChannel().sendMessage(Emoji.success + " Status set to "+ UtilBot.getStatusString(e.getJDA().getPresence().getStatus())).queue();
                }
                else
                    e.getChannel().sendMessage(Emoji.error + " This command is for `Bot Owner` only!").queue();
            }
            
            //Set Game
            if("game".equals(type))
            {
                if(Constants.D_ID.equals(e.getAuthor().getId()))
                {
                    String game = "";
                    for(String g : args) { game += g + " ";}
                    e.getChannel().sendMessage(Emoji.success + " Game set to `"+ UtilBot.setGame(game) + "`").queue();
                }
                else
                    e.getChannel().sendMessage(Emoji.error + " This command is for `Bot Owner` only!").queue();
            }   
        }
    }

    
}
