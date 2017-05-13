/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import main.AIBot;
import constants.Emoji;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;
import main.GuildWrapper;
import system.AILogger;
;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Music  {
    /**
     * Check if the user is in the same voice channel than the bot
     * @param e
     * @return true if the bot can play music
     */
    public static boolean checkVoiceChannel(MessageReceivedEvent e)
    {
        //Check if the user is in a voice channel
        if(!e.getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
            e.getChannel().sendMessage(Emoji.ERROR + " I am not in a voice channel.").queue();
            return false;
        }

        //Prevent user that is not in the same voice channel from executing a command
        if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel()) {
            e.getChannel().sendMessage(Emoji.ERROR + " You need to be in my voice channel.").queue();
            return false;
        }
        return true;
    }

    /**
     * Get the amount of non bot members in a VoiceChannel
     * @param vc
     * @return
     */
    public static int getNonBotMember(VoiceChannel vc)
    {
        int mem = 0;
        for(Member m : vc.getMembers()) {
            if(!m.getUser().isBot())
                mem++;
        }
        return mem;
    }

    /**
     * Check if the mode is not restricted(controversial).
     * Decide between Normal, AutoPlay or FM Mode.
     * @param e
     * @param mode the mode wish to proceed
     * @return true if can proceed
     */
    public static boolean checkMode(MessageReceivedEvent e, PlayerMode mode)
    {
        /**
         * PlayerMode Rules:
         * Default mode is only when no music is playing.
         * Normal is the general mode, can be override by Repeat or AutoPlay.
         * FM is strictly unique. It does not work with any mode.
         * Repeat or AutoPlay can not work with each other.
         */
        PlayerMode current = AIBot.getGuild(e.getGuild()).getGuildPlayer().getMode();
        if((mode == PlayerMode.AUTO_PLAY && !current.canAutoPlay()) ||
            (mode == PlayerMode.REPEAT && !current.canRepeat()) ||
            (mode == PlayerMode.REPEAT_SINGLE && !current.canRepeatSingle()) ||
            (mode == PlayerMode.FM && !current.canFM())) {
            e.getChannel().sendMessage(Emoji.ERROR + " Cannot activate `" + mode.toString()
                    + " Mode` because ` " + current.toString() + " Mode` is already on.").queue();
            return false;
        }
        return true;
    }

    /**
     * Connect to a Voice Channel
     * @param e
     * @param inform
     */
    public static void connect(MessageReceivedEvent e, boolean inform)
    {
        try {
            GuildWrapper guild = AIBot.getGuild(e.getGuild());
            AudioPlayer player = AIBot.getGuild(e.getGuild()).getPlayer();
            VoiceChannel vc = e.getMember().getVoiceState().getChannel();
            Member self = e.getGuild().getSelfMember();

            if(!e.getMember().getVoiceState().inVoiceChannel()) {
                e.getChannel().sendMessage(Emoji.ERROR + " You must connect to a voice channel first.").queue();
                return;
            }

            //Prevent user from requesting to join another voice channel
            if(self.getVoiceState().inVoiceChannel()
                && !self.getVoiceState().getChannel().getId().equals(vc.getId()) //When the user and the bot are in a different channel
                && player.getPlayingTrack() != null //When the PLAYER is playing
                && !player.isPaused() ) { //When the PLAYER is not paused
                e.getChannel().sendMessage(Emoji.ERROR + " I am already playing songs in a voice channel.").queue();
                return;
            }

            //Prevent joining a channel that exceeds user limit
            if(vc.getUserLimit() != 0 &&
                vc.getUserLimit() <= vc.getMembers().size()) {
                e.getChannel().sendMessage(Emoji.ERROR + " Cannot connect to the voice channel due to the user limit.").queue();
                return;
            }

            //Set Text Channel and connect
            guild.setTc(e.getTextChannel());
            guild.setVc(vc);
            guild.getGuildPlayer().connect(vc);

        } catch (PermissionException pe) {
            e.getChannel().sendMessage(Emoji.ERROR + " I don't have the permission to join `" + AIBot.getGuild(e.getGuild()).getVc().getName() + "`.").queue();
            AILogger.errorLog(pe, e, "Connection", "Do not have permission to join a voice channel");
            return;
        }

        //Inform the users that the bot joined a voice channel
        if(inform)
            e.getChannel().sendMessage(Emoji.GLOBE + " Joined Voice Channel `" + AIBot.getGuild(e.getGuild()).getVc().getName() + "`").queue();
    }

    public static void disconnect(MessageReceivedEvent e, boolean inform)
    {
        VoiceChannel vc = e.getMember().getVoiceState().getChannel();
        //Inform user that the bot is not in a Voice Channel
        if(e.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
            e.getChannel().sendMessage(Emoji.ERROR + " I am not in a voice channel.").queue();
            return;
        }

        //Prevent user from commanding the bot to leave a voice channel
        if(!e.getGuild().getSelfMember().getVoiceState().getChannel().getId().equals(vc.getId())) {
            e.getChannel().sendMessage(Emoji.ERROR + " Do not tell me to leave a voice channel when you are not in it.").queue();
            return;
        }

        GuildWrapper guild = AIBot.getGuild(e.getGuild());
        guild.getGuildPlayer().disconnect();

        //Inform the users that the bot joined a voice channel
        if(inform)
            e.getChannel().sendMessage(Emoji.GLOBE + " Left Voice Channel `" + AIBot.getGuild(e.getGuild()).getVc().getName() + "`").queue();
    }
    
    /**
     * Turn the position of the current player to a String, i.e. "▬ ▬ ▬ O ▬ ▬ ▬"
     * @param e
     * @return
     */
    public static String positionToString(MessageReceivedEvent e)
    {
        String start = "", progress = "";
        
        //Inverse play and pause button, like a media player would.
        if(!AIBot.getGuild(e.getGuild()).getPlayer().isPaused()) {
            start = Emoji.PAUSE;
        } else if (AIBot.getGuild(e.getGuild()).getPlayer().isPaused()) {
            start = Emoji.RESUME;
        }
        
        long duration = AIBot.getGuild(e.getGuild()).getPlayer().getPlayingTrack().getDuration();
        long position = AIBot.getGuild(e.getGuild()).getPlayer().getPlayingTrack().getPosition();
        long unit = duration/10;
        int pos = (int) ((int) position/unit);
        
        for(int i = 0; i < 10; i ++) {
            progress += i==pos ? Emoji.RADIO + " " : i==9 ? "▬" : "▬ ";
        }
        
        return start+" "+progress;
    }
    
    /**
     * Turn the volume of the current player to a String
     * @param e
     * @return
     */
    public static String volumeToString(MessageReceivedEvent e) {
        int vol = AIBot.getGuild(e.getGuild()).getPlayer().getVolume();
        return (vol>49 ? Emoji.VOLUME_HIGH : vol>0 ? Emoji.VOLUME_LOW : Emoji.VOLUME_NONE)+" "+vol;
    }

}
