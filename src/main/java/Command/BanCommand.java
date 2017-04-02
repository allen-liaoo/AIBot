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
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class BanCommand implements Command{

    public final static  String HELP = "This command is for banning members.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"ban` or `" + Prefix.getDefaultPrefix() + "b`\n"
                                     + "Parameter: `-h | @Member(s)`";
    private final int delDays = 7;
    private final EmbedBuilder embed = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Moderation Module", null);
        embed.addField("Ban -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_help);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        if(e.getChannelType() != e.getChannelType().PRIVATE)
            e.getTextChannel().sendMessage(me).queue();
        else
            e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(me).queue());
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE) 
        {
            e.getTextChannel().sendMessage(Emoji.error + " You need to mention 1 or more members to ban!").queue();
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
            if (!selfMember.hasPermission(Permission.BAN_MEMBERS))
                e.getTextChannel().sendMessage(Emoji.error + " I need to have **Ban Members* Permission to ban members.").queue();
            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            
            for (User user : mentionedUsers)
            {
                Member member = guild.getMember(user);
                if(!selfMember.canInteract(member))
                {
                    e.getTextChannel().sendMessage(Emoji.error + " Cannot ban member: " + member.getEffectiveName()
                                      + ", they are in a higher role than I am!").queue();
                }
                
                guild.getController().ban(member, delDays).queue(
                    success -> e.getTextChannel().sendMessage(Emoji.success + " Banned " + member.getEffectiveName() + "! Don't come back!\n").queue(),
                    error -> 
                    {
                        if (error instanceof PermissionException)
                        {
                            PermissionException pe = (PermissionException) error;
                            Permission missingPermission = pe.getPermission();
                            
                            e.getTextChannel().sendMessage(Emoji.error + " PermissionError banning " + member.getEffectiveName()
                                            + ": " + error.getMessage()).queue();
                        }
                        else
                        {
                            e.getTextChannel().sendMessage(Emoji.error + " Unknown error while banning " + member.getEffectiveName()
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
