/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Audio;

import Resource.Emoji;
import Command.*;
import Main.*;
import Setting.SmartLogger;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class AudioConnection {
    
    private static VoiceChannel vc;
    private static AudioManager am;
    
    public static void connect(MessageReceivedEvent e, boolean inform)
    {
        try {
            Main.guilds.get(e.getGuild().getId()).setVc(e.getMember().getVoiceState().getChannel());
            Main.guilds.get(e.getGuild().getId()).setTc(e.getTextChannel());
            
            vc = Main.guilds.get(e.getGuild().getId()).getVc();
            am = e.getGuild().getAudioManager();
            am.openAudioConnection(Main.guilds.get(e.getGuild().getId()).getVc());
        } catch (IllegalArgumentException iea) {
            e.getChannel().sendMessage(Emoji.error + " You must connect to a voice channel first.").queue();
            return;
        } catch (PermissionException pe) {
            e.getChannel().sendMessage(Emoji.error + " I don't have the permission to join `" + Main.guilds.get(e.getGuild().getId()).getVc().getName() + "`.").queue();
            SmartLogger.errorLog(pe, e, "AudioConnection", "Do not have permission to join a voice channel");
            return;
        }
        
        //Inform the users that the bot joined a voice channel
        if(inform)
            e.getChannel().sendMessage(Emoji.globe + " Joined Voice Channel `" + vc.getName() + "`").queue();
    }
    
    public static void disconnect(MessageReceivedEvent e, boolean inform)
    {
        try {
            am = e.getGuild().getAudioManager();
            am.closeAudioConnection();
        } catch (NullPointerException npe) {
            e.getChannel().sendMessage(Emoji.error + " You are not in a voice channel.").queue();
        }
        
        //Inform the users that the bot joined a voice channel
        if(inform)
            e.getChannel().sendMessage(Emoji.globe + " Left Voice Channel `" + Main.guilds.get(e.getGuild().getId()).getVc().getName() + "`").queue();
    }
    
    
}
