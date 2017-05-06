/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.music;

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
                                    + "command Usage: `"+ Prefix.getDefaultPrefix() +"skip`\n"
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
        }
        
        else if(args.length == 0)
        {
            if(!Music.checkVoiceChannel(e))
                return;
            
            skip(e, 0, false);
        }
        
        else if("-f".equals(args[0]))
        {
            if(UtilBot.isMod(e.getMember()))
            {
                skip(e, 0, true);
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Only server owner and members with \n"
                        + "`Administrator` or `Manage Server` permission can force skip a song.").queue();
            }
        }
        
        else if(args.length == 1)
        {
            QueueList queue = AIBot.getGuild(e.getGuild()).getScheduler().getQueue();

            int target;
            String search = "";
            if(UtilNum.isInteger(args[0]))
                target = Integer.parseInt(args[0]);
            else {
                for(String s : args) { search += s; }
                target = queue.findIndex(search);
            }

            if(target == 0) { //Trying to skip the current song
                e.getChannel().sendMessage(Emoji.ERROR + " Only skip songs in the queue.").queue();
                return;
            } else if(target > queue.size()-1) {
                e.getChannel().sendMessage(Emoji.ERROR + " The position exceeds the range of this queue.").queue();
                return;
            } else if(target == -1) { //No search result
                e.getChannel().sendMessage(Emoji.ERROR + " No result of " + search + " in the queue.").queue();
                return;
            }
            
            if(queue.get(target).getRequester().equals(e.getAuthor().getName()) || UtilBot.isMod(e.getMember())) {
                skip(e, target, true);
            } else {
                e.getChannel().sendMessage(Emoji.ERROR + " Only server owner, members with `Administrator` permission "
                        + "\nand the song requester can skip a song at the given queue place.").queue();
            }
        }
    }

    /**
     * Vote Skip System
     * @param e
     * @param position The position of the song
     * @param force force skip
     */
    public void skip(MessageReceivedEvent e, int position, boolean force)
    {
        //Force skip the current song
        if(force && position == 0) {
            AIBot.getGuild(e.getGuild()).getScheduler().nextTrack();
        }

        //Skip a song in the queue
        else if(force)
        {
            QueueList queue = AIBot.getGuild(e.getGuild()).getScheduler().getQueue();
            e.getChannel().sendMessage(Emoji.NEXT_TRACK + " Skipped track in position " +
                    position + ":\n`" + queue.remove(position-1).getTrack().getInfo().title + "`").queue();
        }

        //Vote Skip for current song
        else if(position == 0)
        {
            boolean isAdded = AIBot.getGuild(e.getGuild()).getScheduler().addSkip(e.getAuthor());
            int votes = AIBot.getGuild(e.getGuild()).getScheduler().getVote().size();
            if(isAdded)
            {
                int mem = AIBot.getGuild(e.getGuild()).getScheduler().requiredVote();
                if(votes >= mem) {
                    AIBot.getGuild(e.getGuild()).getScheduler().nextTrack();
                    return;
                }
                e.getChannel().sendMessage(Emoji.UP_VOTE + " Added your vote. Still require " + (mem-votes) + " votes to skip the song.").queue();
            }
            else {
                e.getChannel().sendMessage(Emoji.ERROR + " Your vote is already added.").queue();
            }
        }
    }

}
