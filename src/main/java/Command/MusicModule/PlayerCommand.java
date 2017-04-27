/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Command.MusicModule;

import AISystem.Selector.EmojiSelection;
import Audio.Music;
import Command.Command;
import static Command.Command.embed;
import Constants.Constants;
import Constants.Emoji;
import Listener.SelectorListener;
import Main.Main;
import Setting.Prefix;
import Utility.UtilBot;
import Utility.UtilString;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.awt.Color;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class PlayerCommand implements Command {

    public final static String HELP = "This command is for contolling the music player.\n"
                                    + "Command Usage: `"+ Prefix.getDefaultPrefix() +"player` or `"+ Prefix.getDefaultPrefix() +"pl`\n"
                                    + "Parameter: `-h | null`";
    
    private final List<String> reactions = Arrays.asList(Emoji.PLAYER, Emoji.NEXT_TRACK, Emoji.SHUFFLE, Emoji.REPEAT);

    @Override
    public void help(MessageReceivedEvent e) {
        embed.setColor(Color.red);
        embed.setTitle("Music Module", null);
        embed.addField("Player -Help", HELP, true);
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
            if(!Main.guilds.get(e.getGuild().getId()).getScheduler().getNowPlayingTrack().isEmpty())
            {
                AudioPlayer player = Main.guilds.get(e.getGuild().getId()).getPlayer();
                AudioTrack track = player.getPlayingTrack();
                String progress = Music.positionToString(e) + Emoji.STOP;
                String volume = Music.volumeToEmoji(e);
                String posdur = "[`"+UtilString.formatDurationToString(track.getPosition())
                        +"`/`"+UtilString.formatDurationToString(track.getDuration())+"`]";
                
                String state = player.isPaused() ? "Paused" : UtilString.VariableToString("_", player.getPlayingTrack().getState().toString());
                String playing = "**["+track.getInfo().title+"]("+track.getInfo().uri+")**\n";
                
                EmbedBuilder embedplayer = new EmbedBuilder();
                embedplayer.addField(state+"...", playing+"\n"+progress+"\n"+volume+"  "+posdur, true);
                embedplayer.setColor(UtilBot.randomColor());
                e.getChannel().sendMessage(embedplayer.build()).queue((Message msg) -> {
                    if(e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_ADD_REACTION)) {
                        for(String em : reactions) {
                            msg.addReaction(em).queue();
                        }
                    }
                    
                    SelectorListener.addEmojiSelection(e.getAuthor().getId(), 
                            new EmojiSelection(msg, e.getMember(), reactions) {
                        @Override
                        public void action(int chose) {
                            switch(chose) {
                                case 0:
                                    Music.pauseOrPlay(e);
                                case 1:
                                    SkipCommand sc = new SkipCommand();
                                    sc.action(args, e);
                                    break;
                                case 2:
                                    ShuffleCommand shc = new ShuffleCommand();
                                    shc.action(args, e);
                                    break;
                                case 3:
                                    RepeatCommand rc = new RepeatCommand();
                                    rc.action(args, e);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                });
            }
            else
                e.getChannel().sendMessage(Emoji.ERROR + " No song is playing!").queue();
        }
        else if("-h".equals(args[0]))
        {
            help(e);
        }
    }
    
}
