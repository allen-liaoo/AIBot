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
import Setting.Prefix;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import java.time.Instant;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SongCommand implements Command{

    public final static  String HELP = "This command is for getting informations about a current playing or queued song.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"nowplaying` or `"+ Prefix.getDefaultPrefix() +"song` or `"+ Prefix.getDefaultPrefix() +"np`\n"
                                     + "Parameter: `-h | [Queue Position] | null`";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Song -Help", HELP, true);
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
            if(args.length == 0)
            {
                try {
                   AudioTrack nowplaying = Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack();
                   Music.trackInfo(e, nowplaying, "Now Playing");
                } catch (NullPointerException npe) {
                    e.getChannel().sendMessage(Emoji.error + " No song is playing.").queue();
                    return;
                }
            }
            else if(args.length >= 1 && Character.isDigit(args[0].charAt(0)))
            {
                BlockingQueue<AudioTrack> queue = Main.guilds.get(e.getGuild().getId()).getScheduler().getQueue();
                int count = 0, target = Integer.parseInt(args[0]);
                AudioTrack songinfo = null;
                
                if(target > queue.size()) 
                {
                    e.getChannel().sendMessage(Emoji.error + " The position exceeds the range of this queue.").queue();
                    return;
                }

                for(AudioTrack song : queue)
                {
                    count++;
                    if(count == target)
                        songinfo = song;
                }

                Music.trackInfo(e, songinfo, "Queue Song (Position " + args[0] + ")");
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
