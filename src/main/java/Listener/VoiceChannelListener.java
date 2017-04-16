/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Listener;

import Main.Main;
import Resource.Emoji;
import java.util.List;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class VoiceChannelListener extends ListenerAdapter {
    
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);
        onJoin(e.getGuild(), e.getChannelJoined());
    }
    
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);
        onLeave(e.getGuild(), e.getChannelLeft());
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);
        onJoin(e.getGuild(), e.getChannelJoined());
        onLeave(e.getGuild(), e.getChannelLeft());
    }
    
    /**
     * Inform user that the player is resumed
     * @param guild the guild that this event happened in
     * @param joined the channel that this event happened in
     */
    private void onJoin(Guild guild, VoiceChannel joined)
    {
        if(guild.getSelfMember().getVoiceState().getChannel() == joined) {
            
            //Get members
            List<Member> members = joined.getMembers();
            int mem = 0;
            for(Member m : members)
            {
                if(!m.getUser().isBot())
                    mem++;
            }
            
            //Check is the player is playing, if it is not, resume
            if(Main.guilds.get(guild.getId()).getPlayer().isPaused() 
                    && Main.guilds.get(guild.getId()).getPlayer().getPlayingTrack() != null
                    && mem > 0) {
                
                String msg = Emoji.resume + " Player resumed because someone joined the voice channel.";
                if(Main.guilds.get(guild.getId()).getScheduler().getTc() != null)
                    Main.guilds.get(guild.getId()).getScheduler().getTc().sendMessage(msg).queue();
                else
                    guild.getPublicChannel().sendMessage(msg).queue();
                
                Main.guilds.get(guild.getId()).getPlayer().setPaused(false);
            }
        }
    }
    
    /**
     * Inform user when the player is paused
     * @param guild the guild that this event happened in
     * @param left the channel that this event happened in
     */
    private void onLeave(Guild guild, VoiceChannel left)
    {
        if(guild.getSelfMember().getVoiceState().getChannel() == left) {
            int mem = 0;
            List<Member> members = left.getMembers();
            for(Member m : members)
            {
                if(!m.getUser().isBot())
                    mem++;
            }
            
            if(!Main.guilds.get(guild.getId()).getPlayer().isPaused() 
                    && Main.guilds.get(guild.getId()).getPlayer().getPlayingTrack() != null
                    && mem == 0) {
                String msg = Emoji.pause + " Player paused because no user is in the voice channel.";
                if(Main.guilds.get(guild.getId()).getScheduler().getTc() != null)
                    Main.guilds.get(guild.getId()).getScheduler().getTc().sendMessage(msg).queue();
                else
                    guild.getPublicChannel().sendMessage(msg).queue();
                
                Main.guilds.get(guild.getId()).getPlayer().setPaused(true);
            }
        }
    }
    
}
