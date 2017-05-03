/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import Audio.AudioTrackWrapper;
import Audio.Music;
import Command.Command;
import Main.Main;
import Constants.Emoji;
import Constants.Constants;
import Setting.Prefix;
import java.awt.Color;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;

import Utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class SkipCommand extends Command {
    public final static String HELP = "This command is for skipping the current song.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"skip`\n"
                                    + "Parameter: `-h | -f | [Number/Index of the queue] | null`\n"
                                    + "null: Vote to skipp current song.\n"
                                    + "-f: Force skipp current song. Requires Administrator, Manage Channel Permission or Server Owner.\n"
                                    + "[Number/Index of the queue]: Skip a song in the queue. Only Server Owner, members with Administrator"
                                    + "Permission, or song requester can skip.\n";
    

    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Skip -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
            return;
        }
        
        else if(args.length == 0)
        {
            if(!Music.checkVoiceChannel(e))
                return;
            
            int skip = Music.skip(e, 0, false);
            if(skip > 0)
                e.getChannel().sendMessage(Emoji.UP_VOTE + " Added your vote. Still require " + skip + " votes to skip the song.").queue();
            else if(skip == 0)
                e.getChannel().sendMessage(Emoji.NEXT_TRACK + " Skipped current song.").queue();
            else if(skip == -1)
                e.getChannel().sendMessage(Emoji.ERROR + " Your vote is already added.").queue();
            else if(skip == -2)
                e.getChannel().sendMessage(Emoji.ERROR + " There is no song playing.\nSkip what? Don't skip school.").queue();
        }
        
        else if("-f".equals(args[0]))
        {
            if(UtilBot.isMod(e.getMember()))
            {
                Music.skip(e, 0, true);
                e.getChannel().sendMessage(Emoji.NEXT_TRACK + " Force skipped current song.").queue();
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Only server owner and members with \n"
                        + "`Administrator` or `Manage Server` permission can force skip a song.").queue();
            }
        }
        
        else if(args[0].length() == 1)
        {   
            BlockingQueue<AudioTrackWrapper> queue = Main.getGuild(e.getGuild()).getScheduler().getQueue();
            int count = 0, target = Integer.parseInt(args[0]);
            AudioTrackWrapper rapsong = null;

            if(target > queue.size()) {
                e.getChannel().sendMessage(Emoji.ERROR + " The position exceeds the range of this queue.").queue();
                return;
            }
            
            for(AudioTrackWrapper song : queue) {
                count++;
                if(count == target) {
                    rapsong = song;
                }   
            }
            
            if((rapsong.getRequester() != null && rapsong.getRequester().equals(e.getAuthor().getId())) ||
                UtilBot.isMod(e.getMember()))
            {
                if(Music.skip(e, target, false) == 0) {
                    e.getChannel().sendMessage(Emoji.NEXT_TRACK + " Skipped track in queue index " + target + ".").queue();
                }
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Only server owner, members with `Administrator` permission "
                        + "\nand the song requester can skip a song at the given queue place.").queue();
            }
        }
    }

}
