/*
 * 
 * AIBot, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) AIBot
 */
package listener;

import audio.GuildPlayer;
import audio.Music;
import constants.Emoji;
import constants.Global;
import main.AIBot;
import main.GuildWrapper;
import net.dv8tion.jda.core.entities.*;
import setting.Prefix;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import utility.UtilBot;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
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
    
    private final String links = "[Discord Bots Link](https://bots.discord.pw/bots/294327785512763392) | "
                                + "[Invite Link](https://discordapp.com/oauth2/authorize?client_id=294327785512763392&scope=bot&permissions=368573567) | "
                                + "[GitHub](https://github.com/AlienIdeology/AIBot/) | "
                                + "[Support Server](https://discordapp.com/invite/EABc8Kc)";

    private HashMap<String, ScheduledThreadPoolExecutor> scheduleLeaver = new HashMap<>();
    
    /**
     * Guild listener
     */
    
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        // Only send welcome message if the bot is new (10 sec) to the server. 
        // Note that this event may be triggered due to Discord downtime.
        if(ChronoUnit.SECONDS.between(event.getGuild().getSelfMember().getJoinDate(), ZonedDateTime.now())<10) {
            Guild guild = event.getGuild();
            String id = guild.getId();

            welcome(guild);
            System.out.println("Joined guild: " + id + " " + guild.getName());

            AIBot.shards.get(AIBot.shards.size()-1).getGuilds().put(id,
                    new GuildWrapper(event.getJDA(), AIBot.playerManager, id, Prefix.getDefaultPrefix()));

            AIBot.setGame(Game.of(Global.defaultGame()));
            if(!AIBot.isBeta) AIBot.updateStatus();
        }
    }
    
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        System.out.println("Left guild: " + event.getGuild().getId() + " " + event.getGuild().getName());
        AIBot.setGame(Game.of(Global.defaultGame()));
        if(!AIBot.isBeta) AIBot.updateStatus();
    }
    
    private void welcome(Guild g) {
        if(!g.getPublicChannel().canTalk() || !g.getSelfMember().hasPermission(g.getPublicChannel(), Permission.MESSAGE_EMBED_LINKS))
            return;
        
        EmbedBuilder embedmsg = new EmbedBuilder();
        embedmsg.setAuthor("Thanks for Adding AIBot!!", Global.B_GITHUB, Global.B_AVATAR);
        embedmsg.setColor(UtilBot.randomColor());
        embedmsg.setDescription(welcome);
        embedmsg.setThumbnail(Global.B_AVATAR);
        embedmsg.addField("Links", links, false);
        g.getPublicChannel().sendMessage(embedmsg.build()).queue();
    }
    
    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        System.out.println("Guild Available:" + event.getGuild().getName());
    }
    
    /**
     * Voice Channel listener
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
     * Resume player when the bot join a VoiceChannel
     * or when someone moved into the VoiceChannel
     * Pause player when the bot join a empty VoiceChannel
     * @param guild the guild that this event happened in
     * @param joined the channel that this event happened in
     */
    private void onVoiceJoin(Guild guild, VoiceChannel joined)
    {
        if(guild.getSelfMember().getVoiceState().getChannel() == joined) {
            if(!AIBot.isBeta) AIBot.updateStatus();
            int mem = Music.getNonBotMember(joined);
            AudioPlayer player = AIBot.getGuild(guild).getPlayer();
            
            // Join a VoiceChannel with members
            // Check is the player is playing. Only resume when there is more than one user
            if(player.getPlayingTrack() != null
                && mem >= 1) {
                player.setPaused(false);
            }

            // Move to a empty VoiceChannel
            if(mem == 0)
                player.setPaused(true);

            // Shutdown scheduled leaver if user joined
            if(scheduleLeaver.containsKey(joined.getId()))
                scheduleLeaver.get(joined.getId()).shutdown();
        }
    }
    
    /**
     * Pause player when the bot left VoiceChannel or
     * when there is no listener in the VoiceChannel
     * @param guild the guild that this event happened in
     * @param left the channel that this event happened in
     */
    private void onVoiceLeave(Guild guild, VoiceChannel left)
    {
        try {
            if(!AIBot.isBeta) AIBot.updateStatus();
            AudioPlayer player = AIBot.getGuild(guild).getPlayer();
            Member self = guild.getSelfMember();
            if (self == null) // Left guild
                return;

            // User left VoiceChannel the bot is in
            if (self.getVoiceState().inVoiceChannel()
                    && self.getVoiceState().getChannel().getId().equals(left.getId())
                    && Music.getNonBotMember(left) == 0) {
                player.setPaused(true);
                scheduleLeaveVc(guild, left);
            }

            // Bot left VoiceChannel
            if (!self.getVoiceState().inVoiceChannel()
                    && player.getPlayingTrack() != null
                    && !player.isPaused()) {
                player.setPaused(true);
            }
        } catch (NullPointerException npe) {

        }
    }

    private void scheduleLeaveVc(Guild guild, VoiceChannel left)
    {
        ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
                Executors.newScheduledThreadPool(1);

        Runnable leave = () -> {
            GuildPlayer player = AIBot.getGuild(guild).getGuildPlayer();
            player.stopPlayer();
            player.disconnect();
            player.getTc().sendMessage("~~Five Minutes Later...~~").queue((Message msg) ->
                    msg.editMessage(Emoji.NO + " Left voice channel because no one is listening. ;-;").queueAfter(5, TimeUnit.SECONDS));
            scheduleLeaver.remove(left.getId());
            sch.shutdown();
        };

        ScheduledFuture<?> leaver = sch.schedule(leave, 5, TimeUnit.MINUTES);
        scheduleLeaver.put(left.getId(), sch);
    }

}
