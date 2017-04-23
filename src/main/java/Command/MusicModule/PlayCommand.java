/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.AudioTrackWrapper;
import Audio.Music;
import Main.Main;
import Command.Command;
import Resource.Emoji;
import Resource.Constants;
import Setting.Prefix;
import Utility.SearchResult;
import Utility.Search;
import Utility.AILogger;
import java.awt.Color;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PlayCommand implements Command{

    public static HashMap<String, User> selecter = new HashMap<String, User>(); //Guild ID and User
    private static List<SearchResult> results;
    
    public final static  String HELP = "This command is for playing an youtube music in the voice channel.\n"
                                     + "Command Usage: `" + Prefix.getDefaultPrefix() +"play` or `" + Prefix.getDefaultPrefix() + "p`\n"
                                     + "Parameter: `-h | [Keywords or Youtube Url] | -m [Keywords] | null`\n"
                                     + "[Keywords or Youtube Url]: Play the first video of a YouTube search or a specified YouTube video.\n"
                                     + "-m [Keywords]: Get the top 5 search results and choose one to play.\n";
    private String num = "5";
    

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Play -Help", HELP, true);
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
            if(!e.getMember().getVoiceState().inVoiceChannel()) {
                e.getChannel().sendMessage(Emoji.error + " You are not in a voice channel.").queue();
                return;
            }
            
            if(Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused())
                Music.resume(e);
        }
        
        else if ("-h".equals(args[0]))
        {
            help(e);
        }
        
        else if(args.length > 0 && "-m".equals(args[0]))
        {
            String input = "";
            for(int i = 0; i < args.length; i++){
                if(i != 0)
                    input += args[i] + "+";
            }
            
            try {
                results = Search.youtubeSearch(num, input);
                
                String choices = "Top 5 results for `" + input.replaceAll("\\+", " ") + "` on YouTube:\n";
                
                for(int i = 0; i < 5; i++)
                {
                    SearchResult choice = results.get(i);
                    choices += "\n**" + (i+1) + ":** " + choice.getTitle();
                }
                
                choices += "\nUse `1~5` to select the song to play. Type `c` or `cancel` to cancel this selection.";
                
                selecter.put(e.getGuild().getId(), e.getAuthor());
                
                e.getChannel().sendMessage(choices).queue( message -> {
                    message.delete().queueAfter(60, TimeUnit.SECONDS);
                });
            } catch (IOException ioe) {
                AILogger.errorLog(ioe, e, this.getClass().getName(), "IOException at getting Youtube search result (-m).");
            } catch (IndexOutOfBoundsException ioobe) {
                e.getChannel().sendMessage(Emoji.error + " No results.").queue();
                AILogger.errorLog(ioobe, e, this.getClass().getName(), "Cannot get Yt search result correctly (-m). Input: " + input);
            }
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
                    List<SearchResult> result = Search.youtubeSearch(num, input);
                    Music.play(result.get(0).getLink(), e, AudioTrackWrapper.TrackType.NORMAL_REQUEST);
                    result.clear();
                } catch (IOException ioe) {
                    AILogger.errorLog(ioe, e, this.getClass().getName(), "IOException at getting Youtube search result.");
                } catch (IndexOutOfBoundsException ioobe) {
                    e.getChannel().sendMessage(Emoji.error + " No results.").queue();
                    AILogger.errorLog(ioobe, e, this.getClass().getName(), "Cannot get Yt search result correctly. Input: " + input);
                }
            }
            else
                Music.play(args[0], e, AudioTrackWrapper.TrackType.NORMAL_REQUEST);
        }
    }

    
    public static void selector(String message, char character, MessageReceivedEvent e) {
        if(e.getChannelType() == e.getChannelType().PRIVATE)
            return;
        
        if(!selecter.containsKey(e.getGuild().getId()) 
                    || !selecter.containsValue(e.getAuthor()))
            return;
        
        if("cancel".equals(message) || character == 'c')
        {
            e.getTextChannel().deleteMessageById(e.getMessage().getId()).complete();
            e.getChannel().sendMessage("Selection Cancelled.").queue();
            AILogger.commandLog(e, "PlayCommand#selector", "Video selection cancelled.");
        }
        
        else if(!Character.isDigit(character))
            return;
        
        else
        {
            int i = Character.getNumericValue(character);
            AILogger.commandLog(e, "PlayCommand#selector", "Video selected: " + results.get(i - 1).getLink());
            e.getTextChannel().deleteMessageById(e.getMessage().getId()).complete();
        
            Music.play(results.get(i - 1).getLink(), e, AudioTrackWrapper.TrackType.NORMAL_REQUEST);
        }
        
        selecter.remove(e.getGuild().getId(), e.getAuthor());
        results.clear();
    }
    
}
