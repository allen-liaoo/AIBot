/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Listener;

import Main.Main;
import Resource.Constants;
import Resource.Emoji;
import Utility.UtilBot;
import Utility.UtilNum;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GuildListener extends ListenerAdapter {

    private final EmbedBuilder embedmsg = new EmbedBuilder();
    private final String welcome = "Use `=help 1, =help 2, =help 3, or =help 4` for more commands.\n"
                                + "Use `=about` to see the basic informations about this bot.\n"
                                + "Want to invite me to your server? type `=invite` to get the invite link.\n"
                                + "By the way, if you wanna see some awesome servers, just type in `=support`!\n"
                                + "Enjoy the awesome features like hangman, fm(automatic playlist), or radio~~\n";
    
    private final String links = "[Discord Bots Link](https://bots.discord.pw/bots/294327785512763392)\n"
                                + "[Invite Link](https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567)\n"
                                + "[GitHub](https://github.com/AlienIdeology/AIBot/)\n"
                                + "[Support Server](https://discordapp.com/invite/EABc8Kc) for bug reports, suggestions and help";
    
    /**
     * Guild Listener
     */
    
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        super.onGuildJoin(event);
        welcome(event.getGuild().getPublicChannel());
        
        System.out.println("Joined guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
        UtilBot.setGame("default");
    }
    
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        super.onGuildLeave(event);
        System.out.println("Left guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
        UtilBot.setGame("default");
    }
    
    public void welcome(TextChannel c) {
        if(!c.canTalk())
            return;
        
        embedmsg.setAuthor("Thanks for Adding AIBot!!", Constants.B_GITHUB, Constants.B_AVATAR);
        embedmsg.setColor(UtilNum.randomColor());
        embedmsg.setDescription(welcome);
        embedmsg.setThumbnail(Constants.B_AVATAR);
        embedmsg.addField("Links", links, false);
        c.sendMessage(embedmsg.build()).queue();
    }
    
    /**
     * Bot Listener
     */
    
    @Override
    public void onReady(ReadyEvent e) {
        System.out.println("Status - Logged in as: " + e.getJDA().getSelfUser().getName());
    }
    
    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        super.onGuildAvailable(event);
        System.out.println("Guild Avaliable:" + event.getGuild().getName());
    }
    
    /**
     * Voice Channel Listener
     */
    
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
        if(guild != null && guild.getSelfMember().getVoiceState().getChannel() == left) {
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
