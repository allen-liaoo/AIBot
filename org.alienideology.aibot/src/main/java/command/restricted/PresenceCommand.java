/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.restricted;

import command.*;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import system.AILogger;
import utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PresenceCommand extends Command {

    public final static String HELP = "This command is for setting the bot's nickname, status and game.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "setNick` or `"+ Prefix.getDefaultPrefix() + "setStatus` or `"+ Prefix.getDefaultPrefix() + "setGame`\n"
                                    + "Parameter: `-h | [Status] | [game | default] | null`\n"
                                    + "[NickName]: String of the nickname or null.\n"
                                    + "[Status]: Online, idle, dnd, invisible, offline. (Bot Owner Only)\n"
                                    + "[game]: String of the game, default, fix, update or null. (Bot Owner Only)\n";
    private String type = "";
    
    public PresenceCommand(String invoke) {
        type = invoke;
    }
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Restricted Module", null);
        embed.addField("SetPresence -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0) 
        {
            //Set NickName
            if("nick".equals(type))
            {
                if((Global.D_ID.equals(e.getAuthor().getId()) ||
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
                        e.getChannel().sendMessage(Emoji.ERROR + " Cannot set a nickname that is more than 32 characters. "
                                + "(Char count: "+nick.length()+")").queue();
                        return;
                    }
                    
                    e.getGuild().getController().setNickname(e.getGuild().getSelfMember(), nick).queue();

                    if(nick == null) nick = "null";
                    e.getChannel().sendMessage(Emoji.SUCCESS + " NickName set to `"+ nick + "`").queue();
                }
                else
                    e.getChannel().sendMessage(Emoji.ERROR + " This command is for `Bot Owner`, `Server Owner` or members with `NickName Change Permission` only!").queue();
            }
            
            //Set Status
            if("status".equals(type))
            {   
                if(Global.D_ID.equals(e.getAuthor().getId()))
                {
                    try {
                        UtilBot.setStatus(args[0]);
                    } catch (IllegalArgumentException iae) {
                        e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid status.").queue();
                        AILogger.errorLog(iae, e, this.getClass().getName(), "Unknown Status");
                        return;
                    }
                    
                    e.getChannel().sendMessage(Emoji.SUCCESS + " Status set to "+ UtilBot.getStatusString(e.getJDA().getPresence().getStatus())).queue();
                }
                else
                    e.getChannel().sendMessage(Emoji.ERROR + " This command is for `Bot Owner` only!").queue();
            }
            
            //Set game
            if("game".equals(type))
            {
                if(Global.D_ID.equals(e.getAuthor().getId()))
                {
                    String game = "";
                    for(String g : args) { game += g + " ";}
                    e.getChannel().sendMessage(Emoji.SUCCESS + " game set to `"+ UtilBot.setGame(game) + "`").queue();
                }
                else
                    e.getChannel().sendMessage(Emoji.ERROR + " This command is for `Bot Owner` only!").queue();
            }   
        }
    }

    
}
