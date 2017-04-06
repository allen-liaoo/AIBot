/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Resource.Emoji;
import Command.*;
import Main.*;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class AudioConnection {
    
    public static VoiceChannel vc;
    public static AudioManager am;
    
    public static void connect(MessageReceivedEvent e)
    {
        try {
            vc = e.getMember().getVoiceState().getChannel();
            am = e.getGuild().getAudioManager();
            am.openAudioConnection(vc);
            
            e.getChannel().sendMessage(Emoji.globe + " Joined Voice Channel `" + vc.getName() + "`").queue();
        } catch (IllegalArgumentException iea) {
            e.getChannel().sendMessage(Emoji.error + " You must connect to a voice channel first.").queue();
        }
    }
    
    public static void disconnect(MessageReceivedEvent e)
    {
            try {
                am = e.getGuild().getAudioManager();
                am.closeAudioConnection();
            } catch (NullPointerException npe) {
                e.getChannel().sendMessage(Emoji.error + " You are not in a voice channel.").queue();
            }
            
            e.getChannel().sendMessage(Emoji.globe + " Left Voice Channel `" + vc.getName() + "`").queue();
    }
    
    
}
