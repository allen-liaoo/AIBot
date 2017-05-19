/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.utility;

import command.Command;
import constants.Emoji;
import net.dv8tion.jda.core.entities.ChannelType;
import setting.Prefix;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
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
            String[] tokens = e.getMessage().getRawContent().split(" ");
            String content = "";

            for(int i = 0; i < tokens.length; i++) {
                content += i==0 || i==1 ? "" : tokens[i]+" "; //Ignore first two tokens: =say and embed
            }
            
            if (e.getChannelType() != ChannelType.PRIVATE &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                e.getTextChannel().deleteMessageById(e.getMessage().getId()).queue();
            }
            
            EmbedBuilder embedmsg = new EmbedBuilder();
            embedmsg.setColor(Color.red);
            embedmsg.setAuthor("Said", null, e.getJDA().getSelfUser().getAvatarUrl());
            embedmsg.setDescription(content);
            embedmsg.setFooter("Requested by " + e.getAuthor().getName(), null);
            e.getChannel().sendMessage(embedmsg.build()).queue();
        }
        
        else
        {
            String[] tokens = e.getMessage().getRawContent().split(" ");
            String content = "";

            for(int i = 0; i < tokens.length; i++) {
                content += i==0 ? "" : tokens[i]+" "; //Ignore first token: =say
            }
            
            if (e.getChannelType() != ChannelType.PRIVATE &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE))
            {
                e.getTextChannel().deleteMessageById(e.getMessage().getId()).queue();
            }
            e.getChannel().sendMessage(Emoji.Unicode.zero_width + content).queue();
        }
    }

    
}
