/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

import audio.AudioTrackWrapper;
import audio.GuildPlayer;
import audio.Music;
import audio.QueueList;
import command.Command;
import main.AIBot;
import constants.Emoji;
import setting.Prefix;

import utility.UtilBot;
import utility.UtilNum;
import net.dv8tion.jda.core.EmbedBuilder;
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
        if(!Music.checkVoiceChannel(e))
            return;

        GuildPlayer player = AIBot.getGuild(e.getGuild()).getGuildPlayer();
        AIBot.getGuild(e.getGuild()).setTc(e.getTextChannel());

        if(args.length == 0)
        {
            skip(e, player, false);
        }
        
        else if("-f".equals(args[0]))
        {
            if(UtilBot.isMod(e.getMember()))
            {
                skip(e, player, true);
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Only server owner and members with \n"
                        + "`Administrator` or `Manage Server` permission can force skip a song.").queue();
            }
        }
        
        else if(args.length >= 1)
        {
            QueueList queue = AIBot.getGuild(e.getGuild()).getGuildPlayer().getQueue();
            if(UtilNum.isInteger(args[0]) && args.length == 1) {
                int target = Integer.parseInt(args[0]);
                if (target > queue.size() || target < 0) { //Trying to skip songs out of the queue
                    e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid position of the queue.").queue();
                    return;
                } else if(target == 0) {  //Trying to skip the current song
                    e.getChannel().sendMessage(Emoji.ERROR + " Only skip songs in the queue.").queue();
                    return;
                }
                skipPos(e, player, target-1);
            }
            else {
                StringBuilder search = new StringBuilder();
                for(int i = 0; i < args.length; i++) {
                    search.append(i == args.length - 1 ? args[i] : args[i] + " ");
                }
                int target = queue.find(search.toString());
                if(target == -1) { //No search result
                    e.getChannel().sendMessage(Emoji.ERROR + " No result of `" + search + "` in the queue.").queue();
                    return;
                }
                skipPos(e, player, target);
            }
        }
    }

    /**
     * Skip the current song
     * @param e
     * @param force force skip
     */
    public void skip(MessageReceivedEvent e, GuildPlayer player, boolean force)
    {
        if(force) {
            player.nextTrack();
        }

        //Vote Skip for current song
        else
        {
            if(player.addSkip(e.getAuthor())) //If the user hadn't vote yet
            {
                int mem = player.requiredVote();
                int votes = player.getVote().size();
                if(votes >= mem) {
                    player.nextTrack();
                    return;
                }
                e.getChannel().sendMessage(Emoji.UP_VOTE + " Added your vote. Still require " + (mem-votes) + " votes to skip the song.").queue();
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Your vote is already added.").queue();
            }
        }
    }

    /**
     * Skip a song in the queue
     * @param e
     * @param player
     * @param position Starting from 0
     */
    public void skipPos(MessageReceivedEvent e, GuildPlayer player, int position)
    {
        if(!player.getQueue().get(position).getRequester().equals(e.getAuthor().getName()) || !UtilBot.isMod(e.getMember()))
        {
            e.getChannel().sendMessage(Emoji.ERROR + " Only server owner, members with `Administrator` permission "
                    + "\nand the song requester can skip a song at the given queue place.").queue();
            return;
        }

        AudioTrackWrapper removed = player.getQueue().remove(position);
        e.getChannel().sendMessage(Emoji.NEXT_TRACK + " Skipped track in position " + (position+1) + ":\n`" +
                removed.getTrack().getInfo().title + "`").queue();
    }

}
