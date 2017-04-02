/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command;

import Config.Emoji;
import Config.Info;
import Config.Prefix;
import Main.*;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class UnbanCommand implements Command{

    public final static  String HELP = "This command is for unbanning members.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"unban` or `" + Prefix.getDefaultPrefix() + "ub`\n"
                                     + "Parameter: `-h | Member(s)' ID`\n"
                                     + "**Note: ** To get ID, put `\\` infront of a @menton.";
   
    private final EmbedBuilder embed = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Moderation Module", null);
        embed.setTitle("Unban -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE) 
        {
            e.getTextChannel().sendMessage(Emoji.error + " You need to mention 1 or more members to unban!").queue();
        }

        else if("-h".equals(args[0])) 
        {
            help(e);
        }
        
        else
        {
            try {
                Guild guild = e.getGuild();
                Member selfMember = guild.getSelfMember(); 

                //Check if the bot have permission to kick.
                if (!selfMember.hasPermission(Permission.BAN_MEMBERS))
                    e.getTextChannel().sendMessage(Emoji.error + " I need to have **Ban Members* Permission to unban members.").queue();


                for (String userId : args)
                {
                    Member member = guild.getMemberById(userId);
                    boolean hasError = false;

                    try
                    {
                        guild.getController().unban(userId).queue();
                    } catch(Exception ex) {
                        hasError = true;
                        if (ex instanceof PermissionException)
                            {
                                PermissionException pe = (PermissionException) ex;
                                Permission missingPermission = pe.getPermission();

                                e.getTextChannel().sendMessage(Emoji.error + " PermissionError unbanning: " + ex.getMessage()).queue();
                            }
                            else
                            {
                                e.getTextChannel().sendMessage(Emoji.error + " Unknown error while unbanning: " + ex.getClass().getSimpleName() + ">: " + ex.getMessage()).queue();
                            }
                    }
                    if(hasError == false)
                        e.getTextChannel().sendMessage(Emoji.success + " Succesfully unbanned! Join back!\n").queue();
                }
            } catch (Exception ex) {
                e.getTextChannel().sendMessage(Emoji.error + " Cannot find this (these) member!\n").queue();
            }
            
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
