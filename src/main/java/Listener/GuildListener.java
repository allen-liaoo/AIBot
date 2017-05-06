/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package Listener;

import Constants.Global;
import Main.Main;
import Utility.UtilBot;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
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
        // Only send welcome message if the bot is new (10 sec) to the server. 
        // Note that this event may be triggered due to Discord downtime.
        if(ChronoUnit.SECONDS.between(event.getGuild().getSelfMember().getJoinDate(), ZonedDateTime.now())<10) {
            welcome(event.getGuild());
            System.out.println("Joined guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
            UtilBot.setGame("default");
        }
    }
    
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        System.out.println("Left guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
        UtilBot.setGame("default");
    }
    
    public void welcome(Guild g) {
        if(!g.getPublicChannel().canTalk() || !g.getSelfMember().hasPermission(g.getPublicChannel(), Permission.MESSAGE_EMBED_LINKS))
            return;
        
        EmbedBuilder embedmsg = new EmbedBuilder();
        embedmsg.setAuthor("Thanks for Adding AIBot!!", Global.B_GITHUB, Global.B_AVATAR);
        embedmsg.setColor(UtilBot.randomColor());
        embedmsg.setDescription(welcome);
        embedmsg.setThumbnail(Global.B_AVATAR);
        embedmsg.addField("Links", links, false);
        g.getPublicChannel().sendMessage(embedmsg.build()).queue();
        embedmsg.clearFields();
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
        System.out.println("Guild Available:" + event.getGuild().getName());
    }
    
    /**
     * Voice Channel Listener
     */
    
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        onVoiceJoin(e.getGuild(), e.getChannelJoined());
    }
    
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        onVoiceLeave(e.getGuild(), e.getChannelLeft());
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        onVoiceJoin(e.getGuild(), e.getChannelJoined());
        onVoiceLeave(e.getGuild(), e.getChannelLeft());
    }
    
    /**
     * Inform user that the player is resumed
     * @param guild the guild that this event happened in
     * @param joined the channel that this event happened in
     */
    private void onVoiceJoin(Guild guild, VoiceChannel joined)
    {
        if(guild.getSelfMember().getVoiceState().getChannel() == joined) {
            
            //Get members
            List<Member> members = joined.getMembers();
            int mem = 0;
            for(Member m : members) {
                if(!m.getUser().isBot())
                    mem++;
            }
            
            //Check is the player is playing, if it is not, resume
            if(Main.getGuild(guild).getPlayer().isPaused()
                    && Main.getGuild(guild).getPlayer().getPlayingTrack() != null
                    && mem > 0) {
                Main.getGuild(guild).getPlayer().setPaused(false);
            }
        }
    }
    
    /**
     * Inform user when the player is paused
     * @param guild the guild that this event happened in
     * @param left the channel that this event happened in
     */
    private void onVoiceLeave(Guild guild, VoiceChannel left)
    {
        if(guild.getSelfMember() == null) //Left guild
            return;

        if(guild != null && guild.getSelfMember().getVoiceState().getChannel() == left) {
            int mem = 0;
            List<Member> members = left.getMembers();
            for(Member m : members) {
                if(!m.getUser().isBot())
                    mem++;
            }
            
            if(!Main.getGuild(guild).getPlayer().isPaused()
                    && Main.getGuild(guild).getPlayer().getPlayingTrack() != null
                    && mem == 0) {
                Main.getGuild(guild).getPlayer().setPaused(true);
            }
        }
    }
    
}
