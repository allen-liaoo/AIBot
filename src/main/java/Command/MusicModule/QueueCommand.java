/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Command.MusicModule;

import AISystem.AIPages;
import AISystem.Selector.EmojiSelection;
import Audio.AudioTrackWrapper;
import Audio.QueueList;
import Command.Command;
import Constants.Global;
import Constants.Emoji;
import Listener.SelectorListener;
import Main.Main;
import Setting.Prefix;
import Utility.UtilBot;
import Utility.UtilString;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class QueueCommand extends Command{
    public final static String HELP = "This command is getting a list of queued songs\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"queue`\n"
                                    + "Parameter: `-h | [Number] | null`\n"
                                    + "[Number]: Page number of the queue.\n";

    private static final List<String> reactions = Arrays.asList(Emoji.LEFT, Emoji.INFORMATION, Emoji.RIGHT);
    
    @Override
    public EmbedBuilder help(MessageReceivedEvent e) {
        EmbedBuilder embed = super.help(e);
        embed.setTitle("Music Module", null);
        embed.addField("Queue -Help", HELP, true);
        return embed;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 1 && "-h".equals(args[0])) {
            e.getChannel().sendMessage(help(e).build()).queue();
        }
        else
        {
            try {
                int page = 1;
                if(args.length != 0)
                    page = Integer.parseInt(args[0]);
                queueList(e, page);
            } catch (IllegalArgumentException  | IndexOutOfBoundsException ex) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " Please enter a valid page number.").queue();
                return;
            }
        }
    }

    /**
     * Queue List
     * @param e
     * @param page
     */
    public void queueList(MessageReceivedEvent e, int page)
    {
        QueueList queue = Main.getGuild(e.getGuild()).getScheduler().getQueue();

        EmbedBuilder embed = new EmbedBuilder();
        //Now Playing
        AudioTrackWrapper playing = Main.getGuild(e.getGuild()).getScheduler().getNowPlayingTrack();
        Long position = 0L;
        Long duration = 0L;
        if (playing.isEmpty()) {
            embed.addField("Now Playing", "None", false);
        } else {
            String ptitle = playing.getTrack().getInfo().title;
            String purl = playing.getTrack().getInfo().uri;
            String mode = Main.getGuild(e.getGuild()).getScheduler().getMode().toString();
            String text = "**Player Mode:** " + mode + "\n" + "**[" + ptitle + "](" + purl + ")**\n" + "Requested by `" + playing.getRequester() + "`\nType: `" + playing.getType().toString() + "`\n";
            embed.addField("Now Playing", text, false);
            //Current Position / Total Duration
            position = playing.getTrack().getPosition();
            AudioTrackWrapper.TrackType TrackType = null;
            if (playing.getType() != TrackType.RADIO) {
                duration += playing.getTrack().getDuration();
            }
        }
        int count = 0;
        String songs = "";
        List<AudioTrackWrapper> queueList = new ArrayList();
        if (queue.peek() == null) {
            songs += "The queue is currently empty.";
            if (playing.isEmpty()) {
                e.getChannel().sendMessage("The queue is currently empty, and there is no song playing.").queue();
                return;
            }
            embed.addField("Coming Next", songs, false);
        } else {
            //Initialize QueueList for AIPages. Add duration
            for(AudioTrackWrapper wrap : queue) {
                count++;
                AudioTrack track = wrap.getTrack();
                queueList.add(wrap);
                if (wrap.getType() != AudioTrackWrapper.TrackType.RADIO) {
                    duration += track.getDuration();
                }
            }

            songs += "**Queue Count:** " + count + "\n";
            //AIPages
            AIPages pages = new AIPages(queueList);
            List<AudioTrackWrapper> song = pages.getPage(page);
            //Add each queued songs to songQueue
            for (int i = 0; i < song.size(); i++) {
                AudioTrackWrapper wrap = song.get(i);
                String title = wrap.getTrack().getInfo().title;
                String url = wrap.getTrack().getInfo().uri;
                int index = (page - 1) * pages.getPageSize() + 1;
                songs += "`" + (i + index) + ".` **[" + title + "](" + url + ")**\n";
            }
            embed.addField("Coming Next (Page " + page + " / " + pages.getPages() + ")", songs, false);
        }

        String durationWithoutRadio = "";
        if ("00:00".equals(UtilString.formatDurationToString(duration))) {
            durationWithoutRadio = "";
        } else {
            durationWithoutRadio = " / " + UtilString.formatDurationToString(duration);
        }

        embed.setAuthor("Queue List (" + UtilString.formatDurationToString(position) + durationWithoutRadio + ")", Global.B_INVITE, Global.B_AVATAR);
        embed.setColor(UtilBot.randomColor()).setThumbnail(Global.B_AVATAR).setTimestamp(Instant.now());
        embed.setFooter("Requested by " + e.getAuthor().getName(), e.getAuthor().getEffectiveAvatarUrl());

        e.getChannel().sendMessage(embed.build()).queue((Message msg) -> {
            SelectorListener.addEmojiSelection(e.getAuthor().getId(), new EmojiSelection(msg, e.getMember(), reactions) {
                @Override
                public void action(int chose) {
                    switch(chose) {
                        case 0:
                            QueueCommand qc = new QueueCommand();
                            qc.action(new String[]{(page-1)+""}, e);
                            break;
                        case 1:
                            SongCommand sc = new SongCommand();
                            sc.action(new String[0], e);
                            break;
                        case 2:
                            QueueCommand qc2 = new QueueCommand();
                            qc2.action(new String[]{(page+1)+""}, e);
                            break;
                        default:
                            break;
                    }
                }
            });
        });
    }
    
}
