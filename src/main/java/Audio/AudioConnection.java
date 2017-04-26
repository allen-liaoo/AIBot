/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Constants.Emoji;
import Main.*;
import Utility.AILogger;

import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class AudioConnection {
    
    /**
     * Connect to a Voice Channel
     * @param e
     * @param inform
     */
    public static void connect(MessageReceivedEvent e, boolean inform)
    {
        try {
            //If the message is from Private Channel, return
            if(e.getChannelType() == e.getChannelType().PRIVATE)
                return;
            
            //Prevent user from requesting to join another voice channel
            if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel() //When the user and the bot are in a different channel
                    && Main.guilds.get(e.getGuild().getId()).getPlayer().getPlayingTrack() != null //When the PLAYER is playing
                    && !Main.guilds.get(e.getGuild().getId()).getPlayer().isPaused() ) { //When the PLAYER is not paused
                e.getChannel().sendMessage(Emoji.ERROR + " I am already playing songs in a voice channel.").queue();
                return;
            }
            
            //Set Voice Channel and Text Channel
            Main.guilds.get(e.getGuild().getId()).setVc(e.getMember().getVoiceState().getChannel());
            Main.guilds.get(e.getGuild().getId()).setTc(e.getTextChannel());
            
            //Open Connection
            AudioManager am = e.getGuild().getAudioManager();
            am.openAudioConnection(Main.guilds.get(e.getGuild().getId()).getVc());
            
        } catch (IllegalArgumentException iea) {
            e.getChannel().sendMessage(Emoji.ERROR + " You must connect to a voice channel first.").queue();
            return;
        } catch (PermissionException pe) {
            e.getChannel().sendMessage(Emoji.ERROR + " I don't have the permission to join `" + Main.guilds.get(e.getGuild().getId()).getVc().getName() + "`.").queue();
            AILogger.errorLog(pe, e, "AudioConnection", "Do not have permission to join a voice channel");
            return;
        }
        
        //Inform the users that the bot joined a voice channel
        if(inform)
            e.getChannel().sendMessage(Emoji.GLOBE + " Joined Voice Channel `" + Main.guilds.get(e.getGuild().getId()).getVc().getName() + "`").queue();
    }
    
    public static void disconnect(MessageReceivedEvent e, boolean inform)
    {
        try {
            //Inform user that the bot is not in a Voice Channel
            if(e.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
                e.getChannel().sendMessage(Emoji.ERROR + " I am not in a voice channel.").queue();
                return;
            }
            
            //Prevent user from commanding the bot to leave a voice channel
            if(e.getGuild().getSelfMember().getVoiceState().getChannel() != e.getMember().getVoiceState().getChannel()) {
                e.getChannel().sendMessage(Emoji.ERROR + " Do not tell me to leave a voice channel when you are not in it.").queue();
                return;
            }
            
            //Close Connection
            AudioManager am = e.getGuild().getAudioManager();
            am.closeAudioConnection();
            
            //Inform the users that the bot joined a voice channel
            if(inform)
                e.getChannel().sendMessage(Emoji.GLOBE + " Left Voice Channel `" + Main.guilds.get(e.getGuild().getId()).getVc().getName() + "`").queue();
        } catch (NullPointerException npe) { 
            e.getChannel().sendMessage(Emoji.ERROR + " I am not in a voice channel.").queue();
        }
    }
    
}
