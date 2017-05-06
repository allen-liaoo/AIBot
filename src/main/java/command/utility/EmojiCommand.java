/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.utility;

import command.Command;
import constants.Emoji;
import setting.Prefix;
import com.vdurmont.emoji.EmojiManager;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class EmojiCommand extends Command {
    public final static String HELP = "This command is for emoji utilities.\n"
                                    + "command Usage: `" + Prefix.getDefaultPrefix() + "emoji`\n"
                                    + "Parameter: `-h | [Words and Letters] | -m [Emoji Name] | null`\n"
                                    + "[Words and Letters]: Let the bot say [Words and Letters] in emoji language for you.\n"
                                    + "-m [Emoji Name]: Get infotmations about an emoji.";
    
    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("utility Module", null);
        embed.addField("Emoji -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        if("-m".equals(args[0]))
        {
            try{
                if(!EmojiManager.isEmoji(args[1])) {
                    List<Emote> emotesByName = e.getJDA().getEmotesByName(args[1].substring(1, args[1].length()-1), true);
                    e.getChannel().sendMessage("Name: **" + emotesByName.get(0).getName() + "**\n`" + emotesByName.get(0).getAsMention() + "`\n"
                            + emotesByName.get(0).getImageUrl()).queue();
                    return;
                }
                
                com.vdurmont.emoji.Emoji emo = EmojiManager.getByUnicode(args[1]);

                String emoji = emo.getUnicode() + " `" + emo.getUnicode() + "`";
                String description = emo.getDescription().substring(0, 1).toUpperCase() + emo.getDescription().substring(1);
                String html = "`" + emo.getHtmlDecimal() + "`\n`" + emo.getHtmlHexadecimal() + "`";
                String alias = "";
                
                for(String a : emo.getAliases()) {
                    alias += a.substring(0, 1).toUpperCase() + a.substring(1) + ", \n";
                }
                alias = alias.substring(0, alias.length() - 3);

                String tag = "";
                for(String t : emo.getTags()) {
                    tag += t.substring(0, 1).toUpperCase() + t.substring(1) + ", \n";
                }
                if("".equals(tag))
                    tag = "None";
                else
                    tag = tag.substring(0, tag.length() - 3);

                EmbedBuilder embedemo = new EmbedBuilder();
                embedemo.setColor(Color.green);
                embedemo.addField("Emoji", emoji, true);
                embedemo.addField("Description", description, false);
                embedemo.addField("Aliases", alias, true);
                embedemo.addField("Tags", tag, true);
                embedemo.addField("Html", html, true);
                embedemo.setFooter("Emoji Information", null);
                embedemo.setTimestamp(Instant.now());
                MessageEmbed meem = embedemo.build();
                
                e.getChannel().sendMessage(meem).queue();
                embedemo.clearFields();
            } catch (NullPointerException npe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid alias for that emoji.").queue();
                return;
            } catch (IndexOutOfBoundsException ioobe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid server emoji.").queue();
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
