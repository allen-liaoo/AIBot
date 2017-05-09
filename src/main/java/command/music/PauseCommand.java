/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import audio.GuildPlayer;
import command.*;
import audio.Music;
import constants.Emoji;
import main.AIBot;
import setting.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PauseCommand extends Command{
    public final static  String HELP = "This command is for pausing/resuming the bot if the bot is playing music.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"pause` or `"+ Prefix.getDefaultPrefix() +"resume` or `"+ Prefix.getDefaultPrefix() +"unpause`\n"
                                     + "Parameter: `-h | null`";

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Pause -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }

        try {
            if (Music.checkVoiceChannel(e)) {
                GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
                player.setTc(e.getTextChannel());
                player.pauseOrPlay();
            }
        } catch (NullPointerException npe) {
            e.getTextChannel().sendMessage(Emoji.ERROR + " No song is playing!").queue();
        }
        
    }

}
