/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.InformationModule;

import Resource.Prefix;
import Resource.Info;
import Command.Command;
import Main.*;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class InfoUserCommand implements Command{
    public final static String HELP = "This command is for getting informations about a user.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "userinfo` or `" + Prefix.getDefaultPrefix() + "ui` \n"
                                    + "Parameter: `-h | -m | @mention(s) | null`";
                                    
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embedui = new EmbedBuilder();
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Information Module", null);
        embed.setTitle("UserInfo -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0]))
        {
            help(e);
        }
        
        else if(args.length == 0 || (args.length == 1 && ("-m".equals(args[0]))))
        {   
            boolean isMore;
            if((args.length == 1 && ("-m".equals(args[0])))) 
                isMore = true;
            else isMore = false;
            
            User userSelf = e.getAuthor();
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {
                Member memberSelf = e.getMember();
                embedUser(userSelf, memberSelf, isMore, e);
            }
            else
            {
                embedUser(userSelf, null, isMore, e);
            }
        }
                
        else if(e.getChannelType() != e.getChannelType().PRIVATE && args.length > 1 || ("-m".equals(args[0])))
        {
            List <User> userMention = e.getMessage().getMentionedUsers();
            
            for(User user : userMention)
            {
                Member member;
                    member = e.getGuild().getMember(user);
                
                boolean isMore;
                if("-m".equals(args[0])) 
                    isMore = true;
                else isMore = false;

                embedUser(user, member, isMore, e);
            }   
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
    
    }
    
    public void embedUser(User user, Member member, boolean isMore, MessageReceivedEvent e)
    {
        String name = "", id = "", dis = "", nickname = "", icon ="", status = "", game = "", join = "", register = "";
        String bot = "", role = "", perm = "";
        boolean isBot;
        
        name = user.getName();
        id = user.getId();
        dis = user.getDiscriminator();
        if(e.getChannelType() != e.getChannelType().PRIVATE)
            nickname = member.getEffectiveName();
        icon = user.getEffectiveAvatarUrl();
        
        
        status = user.getJDA().getPresence().getStatus().toString();
        try {
            game = user.getJDA().getPresence().getGame().getName();
        } catch (NullPointerException npe) {
            game = "None";
        }
        
        if(e.getChannelType() != e.getChannelType().PRIVATE)
        {
            join = member.getJoinDate().toString();
            register = user.getCreationTime().toString();
        }
        
        embedui.setAuthor(name, null, Info.I_INFO);
        embedui.setColor(Color.blue);
        embedui.setThumbnail(icon);
        embedui.setTimestamp(Instant.now());
        embedui.setFooter("User Information", null);

        embedui.addField("ID", id, true);
        embedui.addField("Discriminator", dis, true);
        
        if(e.getChannelType() != e.getChannelType().PRIVATE)
        {
            embedui.addField("Nickname", nickname, true);
        }
            embedui.addField("Status", status, true);
            embedui.addField("Game", game, true);
            
        if(e.getChannelType() != e.getChannelType().PRIVATE)
        {
            
            embedui.addField("Joined", join, true);
            embedui.addField("Registered", register, true);
        }
        
        if(isMore && e.getChannelType() != e.getChannelType().PRIVATE)
        {
            isBot = user.isBot();
            
            if(isBot == true) 
                bot = "true";
            else
                bot = "false";
            embedui.addField("Bot", bot, true);
            
            List<Role> roles = member.getRoles();
            for(Role roleR : roles) 
            {
                role += roleR.getName() + ", ";
            }
            
            try {
                role = role.substring(0, role.length()-2);
            }
            catch(StringIndexOutOfBoundsException sioobe)
            {
                role = "No role assigned.";
            }
            
            List<Permission> perms = member.getPermissions();
            for(Permission permP : perms)
            {
                perm += permP.getName() + ", ";
            }
            perm = perm.substring(0, perm.length()-2);
            
            embedui.addField("Roles", role, true);
            embedui.addField("Permissions", perm, false);
        }
        MessageEmbed mein = embedui.build();
        
        if(e.getChannelType() != e.getChannelType().PRIVATE)
            e.getTextChannel().sendMessage(mein).queue();
        else
            e.getAuthor().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(mein).queue());
        
        embedui.clearFields();
    }
    
}
