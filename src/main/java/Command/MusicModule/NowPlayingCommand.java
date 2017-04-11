/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.Music;
import Command.Command;
import static Command.Command.embed;
import Main.Main;
import Resource.Emoji;
import Resource.Info;
import Resource.Prefix;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import java.time.Instant;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class NowPlayingCommand implements Command{

    public final static  String HELP = "This command is for getting informations about a current playing song.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"nowplaying` or `"+ Prefix.getDefaultPrefix() +"current` or `"+ Prefix.getDefaultPrefix() +"np`\n"
                                     + "Parameter: `-h | null`";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("NowPlaying -Help", HELP, true);
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
        else
        {
            AudioTrack nowplaying = Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack();
            //System.out.println(nowplaying.getInfo().title);
            //try {
                Music.trackInfo(e, nowplaying);
            //} catch (NullPointerException npe) {
                e.getChannel().sendMessage(Emoji.error + " No song is playing.").queue();
                return;
            //}
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
