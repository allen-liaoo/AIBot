/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.ModerationModule;

import Resource.Emoji;
import Setting.Prefix;
import Resource.Constants;
import Command.Command;
import Utility.AILogger;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class PruneCommand implements Command{
    
    public final static  String HELP = "This command is for deleting messages.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"prune`\n"
                                     + "Parameter: `-h | Number`";
    private final EmbedBuilder embed = new EmbedBuilder();


    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Utility Module", null);
        embed.setTitle("Prune -Help", null);
        embed.setDescription(HELP);
        embed.setFooter("Command Help/Usage", Constants.I_HELP);
        embed.setTimestamp(Instant.now());

        MessageEmbed me = embed.build();
        e.getChannel().sendMessage(me).queue();
        embed.clearFields();
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            e.getChannel().sendMessage(Emoji.error + " You must add a number after Prune command to delete an amount of messages.\n"
                                         + "Use `" + Prefix.getDefaultPrefix() + "prune -h` for help.").queue();
        }
        else if("-h".equals(args[0]))
        {
            help(e);
        }
        else
        {
            TextChannel chan = e.getTextChannel();
            if (!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE) ||
                !e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY)) {
                chan.sendMessage(Emoji.error + " I do not have the `Manage Message` and `Message History` Permission!").queue();
                return;
            }
            
            //Parse String to int, detect it the input is valid.
            Integer msgs = new Integer(0);
            try {
                msgs = Integer.parseInt(args[0]);
                AILogger.commandLog(e, "PruneCommand", "Called to prune " + msgs + " messages.");
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.error + " Please enter a valid number.").queue();
            }
            
            if(msgs <= 1 || msgs > 100) {
                e.getChannel().sendMessage(Emoji.error + " Please enter a number between **2 ~ 100**.").queue();
                return;
            }
            
            //Delete command call
            e.getTextChannel().deleteMessageById(e.getMessage().getId()).complete();
            
            chan.getHistory().retrievePast(msgs).queue((List<Message> mess) -> {
                e.getTextChannel().deleteMessages(mess).queue(
                        success -> chan.sendMessage(Emoji.success + " `" + args[0] + "` messages deleted.")
                                .queue( message -> {
                                    message.delete().queueAfter(2,TimeUnit.SECONDS);
                                }));
            });
        }
    }

    
}
