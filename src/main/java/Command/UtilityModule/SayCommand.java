/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.UtilityModule;

import Command.Command;
import Setting.Prefix;
import AISystem.AILogger;
import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SayCommand extends Command{
public final static  String HELP = "This command is for letting a bot say something for you.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"say`\n"
                                     + "Parameter: `-h | [Content] | embed [Content]| null`\n"
                                     + "[Content]: The sentence you want AIBot to say in normal message form.\n"
                                     + "embed [Content]: The sentence you want AIBot to say in embed message form.\n"
                                     + "Support @mention(s): @everyone, @here, and @user.";
            

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Utility Module", null);
        embed.addField("Say -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if("embed".equals(args[0]))
        {
            String input = "";
            AILogger.commandLog(e, "SayCommand", "Embed");
            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            int mencount = 0;
            for(int i = 1; i < args.length; i++)
            {
                if("@everyone".equals(args[i]) || "@here".equals(args[i]) || !args[i].startsWith("@"))
                        input += args[i] + " ";
                else
                {
                    if(mentionedUsers.size()>0)
                        input += mentionedUsers.get(mencount).getAsMention() + " ";
                    mencount++;
                }
            }
            
            if (e.getChannelType() != e.getChannelType().PRIVATE &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                e.getTextChannel().deleteMessageById(e.getMessage().getId()).queue();
            }
            
            EmbedBuilder embedmsg = new EmbedBuilder();
            embedmsg.setColor(Color.red);
            embedmsg.setAuthor("Said", null, e.getJDA().getSelfUser().getAvatarUrl());
            embedmsg.setDescription(input);
            embedmsg.setFooter("Requested by " + e.getAuthor().getName(), null);
            e.getChannel().sendMessage(embedmsg.build()).queue();
        }
        
        else
        {
            AILogger.commandLog(e, "SayCommand", "Regular");
            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            int mencount = 0;
            String input = "";
            
            for(int i = 0; i < args.length; i++)
            {   
                if("@everyone".equals(args[i]) || "@here".equals(args[i]) || !args[i].startsWith("@")) //Mention @everyone, @here, or normal message
                    input += args[i] + " ";
                
                else //Mention Users
                {
                    if(mentionedUsers.size() > 0)
                    {
                        input += mentionedUsers.get(mencount).getAsMention() + " ";
                        mencount++;
                    }
                }
            }
            
            if (e.getChannelType() != e.getChannelType().PRIVATE &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                e.getTextChannel().deleteMessageById(e.getMessage().getId()).queue();
            }
            e.getChannel().sendMessage(input).queue();
        }
    }

    
}
