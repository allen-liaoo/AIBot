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
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class KickCommand implements Command{
    public final static  String HELP = "This command is for kicking members.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"kick` or `" + Prefix.getDefaultPrefix() + "k`\n"
                                     + "Parameter: `-h | @Member(s)`";
    private final EmbedBuilder embed = new EmbedBuilder();
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Moderation Module", null);
        embed.addField("Kick -Help", HELP, true);
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
            e.getTextChannel().sendMessage(Emoji.E_error + " You need to mention 1 or more members to kick!").queue();
        }

        else if("-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if(e.getChannelType() != e.getChannelType().PRIVATE)
        {
            Guild guild = e.getGuild();
            Member selfMember = guild.getSelfMember(); 
            
            //Check if the bot have permission to kick.
            if (!selfMember.hasPermission(Permission.KICK_MEMBERS))
                e.getTextChannel().sendMessage(Emoji.E_error + " I need to have **Kick Members* Permission to kick members.").queue();
            
            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            
            for (User user : mentionedUsers)
            {
                Member member = guild.getMember(user);
                if(!selfMember.canInteract(member))
                {
                    if(mentionedUsers.size() > 1)
                    {
                        e.getTextChannel().sendMessage(Emoji.E_error + " Cannot kick member: " + member.getEffectiveName()
                                      + ". They are in a higher role than I am!").queue();
                    }
                    else
                    {
                        e.getTextChannel().sendMessage(Emoji.E_error + " Cannot kick member: " + member.getEffectiveName()
                                      + ". the person is in a higher role than I am!").queue();
                    }
                }
                
                guild.getController().kick(member).queue(
                    success -> e.getTextChannel().sendMessage(Emoji.E_success + " Kicked " + member.getEffectiveName() + "! Bye!").queue(),
                    error -> 
                    {
                        if (error instanceof PermissionException)
                        {
                            PermissionException pe = (PermissionException) error;
                            Permission missingPermission = pe.getPermission();
                            
                            e.getTextChannel().sendMessage(Emoji.E_error + " PermissionError kicking " + member.getEffectiveName()
                                            + ": " + error.getMessage()).queue();
                        }
                        else
                        {
                            e.getTextChannel().sendMessage(Emoji.E_error + " Unknown error while kicking " + member.getEffectiveName()
                                    + ": <" + error.getClass().getSimpleName() + ">: " + error.getMessage()).queue();
                        }
                    });
            }
        }
    }
    

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
