/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import constants.Emoji;
import constants.Global;
import setting.Prefix;
import command.Command;
import utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class InviteCommand extends Command {

    public final static  String HELP = "This command is for inviting the bot to your own server.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"invite`\n"
                                     + "Parameter: `-h | null`";        

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Information Module", null);
        embed.addField("Invite -Help", HELP, true);
        embed.setFooter("Command Help/Usage", Global.I_HELP);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        else
        {
            EmbedBuilder links = new EmbedBuilder();
            links.setAuthor(e.getMember().getEffectiveName() + ", some spicy links", Global.B_INVITE, e.getAuthor().getEffectiveAvatarUrl());
            links.setColor(UtilBot.randomColor());
            links.appendDescription(Emoji.INVITE + " **[Invite Link](" + Global.B_INVITE + "&guild_id=" + e.getGuild().getId() + ")  |  " +
                    "[AIBot Support Server]("+ Global.B_SERVER+")**");
            e.getChannel().sendMessage(links.build()).queue();
        }
    }

    
}
