/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import audio.GuildPlayer;
import audio.Music;
import command.Command;
import main.AIBot;
import constants.Emoji;
import setting.Prefix;
import utility.UtilBot;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class StopCommand extends Command{

    public final static String HELP = "This command is for stopping the bot from playing music and unbind to the voice channel.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"stop`\n"
                                    + "Parameter: `-h | null`";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Stop -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(!Music.checkVoiceChannel(e))
            return;

        int mem = 0;
        //Only count non-Bot Users
        List<Member> members = AIBot.getGuild(e.getGuild()).getVc().getMembers();
        for(Member m : members) {
            if(!m.getUser().isBot())
                mem++;
        }

        if(UtilBot.isMajority(e.getMember()) ||
            UtilBot.isMod(e.getMember()))
        {
            stop(e);
        }
        else
        {
            e.getChannel().sendMessage(Emoji.ERROR + " This command is for server owner, bot owner, or "
                    + "members with `Administrator` or `Manage Server` permissions only.\n"
                    + "You can also stop the player if there is less than 3 members in the voice channel.").queue();
        }
    }

    public void stop(MessageReceivedEvent e)
    {
        //Prevent user that is not in the same voice channel from stopping the Player
        if(!Music.checkVoiceChannel(e)) {
            return;
        }

        if (AIBot.getGuild(e.getGuild()).getPlayer().getPlayingTrack() == null)
            e.getChannel().sendMessage(Emoji.STOP + " Stopped and reset the player.").queue();

        GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
        AIBot.getGuild(e.getGuild()).setTc(e.getTextChannel());
        player.stopPlayer();
        Music.disconnect(e, false);
    }
    
}
