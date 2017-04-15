/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.UtilityModule;

import Command.Command;
import Resource.Info;
import Setting.Prefix;
import Utility.SmartLogger;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SayCommand implements Command{
public final static  String HELP = "This command is for letting a bot say something for you.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"say`\n"
                                     + "Parameter: `-h | [Content] | embed [Content]| null`\n"
                                     + "[Content]: The sentence you want AIBot to say in normal message form.\n"
                                     + "embed [Content]: The sentence you want AIBot to say in embed message form.\n"
                                     + "Support @mention(s): @everyone, @here, and @user.";
    private final EmbedBuilder embed = new EmbedBuilder();
    private final EmbedBuilder embedmsg = new EmbedBuilder();
            
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.addField("Say -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Info.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        String input = "";
        if(args.length > 0 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if("embed".equals(args[0]))
        {
            SmartLogger.commandLog(e, "SayCommand", "Embed");
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
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {    if (e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
                {
                    //Delete the command message.
                    e.getChannel().getHistory().retrievePast(1).queue((List<Message> messages) -> messages.forEach((Message msg2) -> 
                    {
                        msg2.delete().queue();
                    }));
                }
            }
            
            embedmsg.setColor(Color.red);
            embedmsg.setAuthor("Said", null, e.getJDA().getSelfUser().getAvatarUrl());
            embedmsg.setDescription(input);
            embedmsg.setFooter("Requested by " + e.getAuthor().getName(), null);

            MessageEmbed memsg = embedmsg.build();
            e.getChannel().sendMessage(memsg).queue();
            embedmsg.clearFields();
        }
        
        else 
        {
            SmartLogger.commandLog(e, "SayCommand", "Regular");
            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            int mencount = 0;
            
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
            
            if(e.getChannelType() != e.getChannelType().PRIVATE)
            {
                if (e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
                {
                    try {
                        //Delete the command message.
                        e.getChannel().getHistory().retrievePast(1).queue((List<Message> messages) -> messages.forEach((Message msg2) -> 
                        {
                            msg2.delete().queue();
                        }));

                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        SmartLogger.errorLog(ex, e, this.getClass().getName(), "Process interrupted.");
                    }
                }
            }
            
            e.getChannel().sendMessage(input).queue();
            input = "";
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
