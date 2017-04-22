/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.UtilityModule;

import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import com.vdurmont.emoji.EmojiManager;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class EmojiCommand implements Command {
    public final static String HELP = "This command is for emoji utilities.\n"
                                    + "Command Usage: `" + Prefix.getDefaultPrefix() + "emoji`\n"
                                    + "Parameter: `-h | [Words and Letters] | -m [Emoji Name] | null`\n"
                                    + "[Words and Letters]: Let the bot say [Words and Letters] in emoji language for you.\n"
                                    + "-m [Emoji Name]: Get infotmations about an emoji.";
    
    private EmbedBuilder embedemo = new EmbedBuilder();
    private String roles = "";
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.addField("Emoji -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length > 0 && "-h".equals(args[0])) 
        {
            help(e);
        }
        
        else if("-m".equals(args[0]))
        {
            try{
                
                System.out.print("Emoji input -> " + args[1] + "\n");
                com.vdurmont.emoji.Emoji emo = EmojiManager.getForAlias(":" + args[1] + ":");

                String emoji = emo.getUnicode() + " `" + emo.getUnicode() + "`";
                String description = emo.getDescription().substring(0, 1).toUpperCase() + emo.getDescription().substring(1);
                String html = "`" + emo.getHtmlDecimal() + "`\n`" + emo.getHtmlHexadecimal() + "`";
                String alias = "";
                
                for(String a : emo.getAliases())
                {
                    alias += a.substring(0, 1).toUpperCase() + a.substring(1) + ", \n";
                }
                alias = alias.substring(0, alias.length() - 3);

                String tag = "";
                for(String t : emo.getTags())
                {
                    tag += t.substring(0, 1).toUpperCase() + t.substring(1) + ", \n";
                }
                if("".equals(tag))
                    tag = "None";
                else
                    tag = tag.substring(0, tag.length() - 3);

                embedemo.setColor(Color.green);
                embedemo.addField("Emoji", emoji, true);
                embedemo.addField("Description", description, true);
                embedemo.addBlankField(true);
                embedemo.addField("Aliases", alias, true);
                embedemo.addField("Tags", tag, true);
                embedemo.addField("Html", html, true);
                embedemo.setFooter("Emoji Information", null);
                embedemo.setTimestamp(Instant.now());
                MessageEmbed meem = embedemo.build();
                
                e.getChannel().sendMessage(meem).queue();
                embedemo.clearFields();
            } catch (NullPointerException npe) {
                e.getChannel().sendMessage(Emoji.error + " Please enter a valid alias for that emoji.").queue();
                return;
            }
        }
        
        else 
        {
            String input = "";
            for(int i = 0; i < args.length; i++)
            {
                input += args[i] + " ";
            }
            
            if(input.length() != 0)
            {
                if (e.getChannelType() != e.getChannelType().PRIVATE &&
                e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    e.getTextChannel().deleteMessageById(e.getMessage().getId()).queue();
                }
                
                String output = Emoji.stringToEmoji(input);
                e.getChannel().sendMessage(output).queue();
            }
            else
                help(e);
        }
    }

    
}
