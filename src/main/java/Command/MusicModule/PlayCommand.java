/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.AudioConnection;
import Audio.Music;
import Command.Command;
import static Command.MusicModule.JoinCommand.HELP;
import Resource.Emoji;
import Resource.Info;
import Resource.Prefix;
import Resource.SearchResult;
import Resource.Web;
import Main.Main;
import Setting.SmartLogger;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PlayCommand implements Command{

    public final static  String HELP = "This command is for playing an youtube music in the voice channel.\n"
                                     + "Command Usage: `"+ Prefix.getDefaultPrefix() +"play`\n"
                                     + "Parameter: `-h | [Youtube Url] | null`";
    private String num = "1";
    
    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return true;
    }

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Play -Help", HELP, true);
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
            if(!args[0].startsWith("http"))
            {
                String input = "";
                for(int i = 0; i < args.length; i++){
                    input += args[i] + "+";
                }
                
                input = input.substring(0, input.length() - 1);
            
                try {
                    List<SearchResult> result = Web.youtubeSearch(num, input);
                    //e.getChannel().sendMessage("**" + result.get(0).getTitle() + "**\n" + result.get(0).getLink()).queue();
                    Music.play(result.get(0).getLink(), e);
                } catch (IOException ioe) {
                    SmartLogger.errorLog(ioe, e.getGuild().getName(), this.getClass().getName(), "IOException at getting Youtube search result.");
                }
            }
            else
                Music.play(args[0], e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {
        
    }
    
}
